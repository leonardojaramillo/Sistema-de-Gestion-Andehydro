-- Insertar empleado de prueba (creará E### coherente)
VAR v_emp_id VARCHAR2(20);
BEGIN
  sp_insert_empleado('Ana','Lopez','Analista','Comercial', TO_DATE('2022-03-01','YYYY-MM-DD'), NULL, 'C001', :v_emp_id);
END;
/
PRINT v_emp_id;

-- Registrar compra (creará CP###)
VAR v_cmp_id VARCHAR2(20);
BEGIN
  sp_registrar_compra(SYSDATE, 'Cambio de transformador', 2500000, 'PEN', 'PR01', 'P01', :v_cmp_id);
END;
/
PRINT v_cmp_id;

-- Crear contrato (creará CT###)
VAR v_cnt_id VARCHAR2(20);
BEGIN
  sp_crear_contrato(TO_DATE('2025-01-01','YYYY-MM-DD'), TO_DATE('2026-12-31','YYYY-MM-DD'), 0.15, 'PEN', 'CL01', :v_cnt_id);
END;
/
PRINT v_cnt_id;

-- Generar factura (creará F###) 
VAR v_fac_id VARCHAR2(20);
BEGIN
  sp_generar_factura_auto('CT01', SYSDATE, 1200.50, 'PENDIENTE', :v_fac_id);
END;
/
PRINT v_fac_id;

