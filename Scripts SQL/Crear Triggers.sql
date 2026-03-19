-- 1) Triggers: antes de insertar factura, si monto_total es NULL calcula usando tarifa
CREATE OR REPLACE TRIGGER trg_facturacion_bi
BEFORE INSERT ON FACTURACION
FOR EACH ROW
DECLARE
    v_tarifa NUMBER := 0;
BEGIN
    IF :NEW.monto_total IS NULL THEN
        SELECT tarifa_kw INTO v_tarifa
          FROM CONTRATO_ENERGIA
         WHERE id_contrato = :NEW.id_contrato;
        :NEW.monto_total := NVL(:NEW.energia_consumida,0) * NVL(v_tarifa,0);
    END IF;
END;
/
-- 2) Triggers: antes de insertar compra, normaliza moneda y valida monto positivo
CREATE OR REPLACE TRIGGER trg_compra_bi
BEFORE INSERT ON COMPRA
FOR EACH ROW
BEGIN
    :NEW.tipo_moneda := UPPER(:NEW.tipo_moneda);

    IF :NEW.tipo_moneda NOT IN ('PEN','USD','EUR') THEN
        RAISE_APPLICATION_ERROR(-20021, 'Tipo de moneda no permitido en COMPRA');
    END IF;

    IF :NEW.monto <= 0 THEN
        RAISE_APPLICATION_ERROR(-20022, 'Monto de compra debe ser mayor que 0');
    END IF;
END;
/

-- 3) Triggers: evita eliminación de central si hay dependencias
CREATE OR REPLACE TRIGGER trg_block_del_central
BEFORE DELETE ON CENTRAL_HIDROELECTRICA
FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM EMPLEADO WHERE id_central = :OLD.id_central;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20030,'No se puede eliminar CENTRAL: existen empleados asignados');
    END IF;

    SELECT COUNT(*) INTO v_count FROM EQUIPO WHERE id_central = :OLD.id_central;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20031,'No se puede eliminar CENTRAL: existen equipos asignados');
    END IF;

    SELECT COUNT(*) INTO v_count FROM CLIENTE WHERE id_central = :OLD.id_central;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20032,'No se puede eliminar CENTRAL: existen clientes asociados');
    END IF;

    SELECT COUNT(*) INTO v_count FROM PROYECTO_RSE WHERE id_central = :OLD.id_central;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20033,'No se puede eliminar CENTRAL: existen proyectos RSE asociados');
    END IF;
END;
/






