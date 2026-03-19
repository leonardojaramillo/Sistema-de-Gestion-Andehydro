-- 1) Procedimiento: inserta empleado con validaciones básicas y devuelve id generado
CREATE OR REPLACE PROCEDURE sp_insert_empleado (
    p_nombres      IN  VARCHAR2,
    p_apellidos    IN  VARCHAR2,
    p_cargo        IN  VARCHAR2,
    p_area         IN  VARCHAR2,
    p_fecha_ing    IN  DATE,
    p_id_jefe      IN  VARCHAR2 DEFAULT NULL,
    p_id_central   IN  VARCHAR2,
    p_out_id_emp   OUT VARCHAR2
) AS
    v_last_num NUMBER := 0;
    v_max_len  NUMBER := 3;
    v_next     NUMBER := 0;
    v_new_id   VARCHAR2(20);
BEGIN
    IF p_nombres IS NULL OR p_apellidos IS NULL THEN
        RAISE_APPLICATION_ERROR(-20010,'Nombres y apellidos son obligatorios');
    END IF;

    -- obtener max número existente (extrae secuencia de dígitos)
    SELECT NVL(MAX(TO_NUMBER(REGEXP_SUBSTR(id_empleado, '\d+'))),0) ,
           NVL(MAX(LENGTH(REGEXP_SUBSTR(id_empleado, '\d+'))),3)
    INTO v_last_num, v_max_len
    FROM EMPLEADO;

    v_next := v_last_num + 1;
    IF v_max_len < 3 THEN
        v_max_len := 3; -- al menos 3 dígitos: E001
    END IF;

    v_new_id := 'E' || LPAD(v_next, v_max_len, '0');

    INSERT INTO EMPLEADO (id_empleado, nombres, apellidos, cargo, area, fecha_ingreso, id_jefe, id_central)
    VALUES (v_new_id, p_nombres, p_apellidos, p_cargo, p_area, p_fecha_ing, p_id_jefe, p_id_central);

    p_out_id_emp := v_new_id;

    INSERT INTO auditoria_operaciones (nombre_tabla, tipo_operacion, clave_registro, usuario_aplic, detalles)
    VALUES ('EMPLEADO','INSERT', p_out_id_emp, USER, 'Inserción mediante sp_insert_empleado');

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END sp_insert_empleado;
/


-- 2) Procedimiento: inserta compra con validación de moneda y retorna id
CREATE OR REPLACE PROCEDURE sp_registrar_compra (
    p_fecha        IN DATE,
    p_descripcion  IN VARCHAR2,
    p_monto        IN NUMBER,
    p_tipo_moneda  IN VARCHAR2,
    p_id_proveedor IN VARCHAR2,
    p_id_proyecto  IN VARCHAR2,
    p_out_id_compra OUT VARCHAR2
) AS
    v_last_num NUMBER := 0;
    v_max_len  NUMBER := 3;
    v_next     NUMBER := 0;
    v_new_id   VARCHAR2(20);
BEGIN
    IF UPPER(p_tipo_moneda) NOT IN ('PEN','USD','EUR') THEN
        RAISE_APPLICATION_ERROR(-20011,'Tipo de moneda inválido (usar PEN, USD o EUR)');
    END IF;

    SELECT NVL(MAX(TO_NUMBER(REGEXP_SUBSTR(id_compra, '\d+'))),0),
           NVL(MAX(LENGTH(REGEXP_SUBSTR(id_compra, '\d+'))),3)
    INTO v_last_num, v_max_len
    FROM COMPRA;

    v_next := v_last_num + 1;
    IF v_max_len < 3 THEN
        v_max_len := 3;
    END IF;

    v_new_id := 'CP' || LPAD(v_next, v_max_len, '0');

    INSERT INTO COMPRA (id_compra, fecha, descripcion, monto, tipo_moneda, id_proveedor, id_proyecto)
    VALUES (v_new_id, p_fecha, p_descripcion, p_monto, UPPER(p_tipo_moneda), p_id_proveedor, p_id_proyecto);

    p_out_id_compra := v_new_id;

    INSERT INTO auditoria_operaciones (nombre_tabla, tipo_operacion, clave_registro, usuario_aplic, detalles)
    VALUES ('COMPRA','INSERT', p_out_id_compra, USER, 'Inserción mediante sp_registrar_compra');

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END sp_registrar_compra;
/


-- 3) Procedimiento: crea contrato con validación y retorno de id
CREATE OR REPLACE PROCEDURE sp_crear_contrato (
    p_fecha_inicio IN DATE,
    p_fecha_fin    IN DATE,
    p_tarifa_kw    IN NUMBER,
    p_tipo_moneda  IN VARCHAR2,
    p_id_cliente   IN VARCHAR2,
    p_out_id_contrato OUT VARCHAR2
) AS
    v_last_num NUMBER := 0;
    v_max_len  NUMBER := 3;
    v_next     NUMBER := 0;
    v_new_id   VARCHAR2(20);
BEGIN
    IF p_fecha_fin IS NOT NULL AND p_fecha_fin < p_fecha_inicio THEN
        RAISE_APPLICATION_ERROR(-20012,'fecha_fin debe ser posterior a fecha_inicio');
    END IF;

    IF UPPER(p_tipo_moneda) NOT IN ('PEN','USD','EUR') THEN
        RAISE_APPLICATION_ERROR(-20013,'Moneda inválida');
    END IF;

    SELECT NVL(MAX(TO_NUMBER(REGEXP_SUBSTR(id_contrato, '\d+'))),0),
           NVL(MAX(LENGTH(REGEXP_SUBSTR(id_contrato, '\d+'))),3)
    INTO v_last_num, v_max_len
    FROM CONTRATO_ENERGIA;

    v_next := v_last_num + 1;
    IF v_max_len < 3 THEN
        v_max_len := 3;
    END IF;

    v_new_id := 'CT' || LPAD(v_next, v_max_len, '0');

    INSERT INTO CONTRATO_ENERGIA (id_contrato, fecha_inicio, fecha_fin, tarifa_kw, tipo_moneda, id_cliente)
    VALUES (v_new_id, p_fecha_inicio, p_fecha_fin, p_tarifa_kw, UPPER(p_tipo_moneda), p_id_cliente);

    p_out_id_contrato := v_new_id;

    INSERT INTO auditoria_operaciones (nombre_tabla, tipo_operacion, clave_registro, usuario_aplic, detalles)
    VALUES ('CONTRATO_ENERGIA','INSERT', p_out_id_contrato, USER, 'Creación mediante sp_crear_contrato');

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END sp_crear_contrato;
/


-- 4) Procedimiento: genera factura usando tarifa del contrato y energía consumida
CREATE OR REPLACE PROCEDURE sp_generar_factura_auto (
    p_id_contrato       IN VARCHAR2,
    p_fecha_emision     IN DATE,
    p_energia_consumida IN NUMBER,
    p_estado_pago       IN VARCHAR2 DEFAULT 'PENDIENTE',
    p_out_id_factura    OUT VARCHAR2
) AS
    v_tarifa NUMBER := 0;
    v_last_num NUMBER := 0;
    v_max_len  NUMBER := 3;
    v_next     NUMBER := 0;
    v_new_id   VARCHAR2(20);
BEGIN
    SELECT tarifa_kw INTO v_tarifa FROM CONTRATO_ENERGIA WHERE id_contrato = p_id_contrato;

    -- generar nuevo id factura
    SELECT NVL(MAX(TO_NUMBER(REGEXP_SUBSTR(id_factura, '\d+'))),0),
           NVL(MAX(LENGTH(REGEXP_SUBSTR(id_factura, '\d+'))),3)
    INTO v_last_num, v_max_len
    FROM FACTURACION;

    v_next := v_last_num + 1;
    IF v_max_len < 3 THEN
        v_max_len := 3;
    END IF;

    v_new_id := 'F' || LPAD(v_next, v_max_len, '0');

    INSERT INTO FACTURACION (id_factura, fecha_emision, energia_consumida, monto_total, estado_pago, id_contrato)
    VALUES (v_new_id, p_fecha_emision, p_energia_consumida, NVL(p_energia_consumida,0) * NVL(v_tarifa,0), p_estado_pago, p_id_contrato);

    p_out_id_factura := v_new_id;

    INSERT INTO auditoria_operaciones (nombre_tabla, tipo_operacion, clave_registro, usuario_aplic, detalles)
    VALUES ('FACTURACION','INSERT', p_out_id_factura, USER, 'Factura generada automaticamente por sp_generar_factura_auto');

    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20020,'Contrato no encontrado');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END sp_generar_factura_auto;
/


