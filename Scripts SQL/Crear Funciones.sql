-- 1) Funcion: suma montos pendientes (ej: facturas con estado distinto a 'Pagado')
CREATE OR REPLACE FUNCTION fn_deuda_total_cliente (
    p_id_cliente IN VARCHAR2
) RETURN NUMBER IS
    v_total NUMBER := 0;
BEGIN
    SELECT NVL(SUM(f.monto_total),0)
      INTO v_total
      FROM FACTURACION f
      JOIN CONTRATO_ENERGIA c ON f.id_contrato = c.id_contrato
      JOIN CLIENTE cl ON c.id_cliente = cl.id_cliente
     WHERE cl.id_cliente = p_id_cliente
       AND NVL(UPPER(f.estado_pago),'PENDIENTE') <> 'PAGADO';

    RETURN v_total;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 0;
    WHEN OTHERS THEN
        -- Propagar error para que el llamante lo maneje
        RAISE;
END fn_deuda_total_cliente;
/

-- 2) Funcion: años completos desde fecha_ingreso hasta hoy
CREATE OR REPLACE FUNCTION fn_antiguedad_empleado (
    p_id_empleado IN VARCHAR2
) RETURN NUMBER IS
    v_fecha_ingreso DATE;
    v_anos NUMBER := 0;
BEGIN
    SELECT fecha_ingreso INTO v_fecha_ingreso FROM EMPLEADO WHERE id_empleado = p_id_empleado;

    v_anos := TRUNC(MONTHS_BETWEEN(SYSDATE, v_fecha_ingreso) / 12);

    RETURN NVL(v_anos, 0);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 0;
    WHEN OTHERS THEN
        RAISE;
END fn_antiguedad_empleado;
/

-- 3) Funcion: devuelve 1 si equipo operativo, 0 si no existe o está inactivo
CREATE OR REPLACE FUNCTION fn_equipo_operativo (
    p_id_equipo IN VARCHAR2
) RETURN NUMBER IS
    v_estado VARCHAR2(20);
BEGIN
    SELECT estado INTO v_estado FROM EQUIPO WHERE id_equipo = p_id_equipo;
    IF UPPER(NVL(v_estado,' ')) = 'OPERATIVO' THEN
        RETURN 1;
    ELSE
        RETURN 0;
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 0;
    WHEN OTHERS THEN
        RAISE;
END fn_equipo_operativo;
/



