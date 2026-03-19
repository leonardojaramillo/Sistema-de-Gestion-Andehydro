---------------------------------------------------------
-- ÍNDICES PARA OPTIMIZACIÓN (NO PRIMARY KEY)
---------------------------------------------------------

-- EMPLEADO
CREATE INDEX idx_empleado_area ON EMPLEADO(area);
CREATE INDEX idx_empleado_cargo ON EMPLEADO(cargo);
CREATE INDEX idx_empleado_jefe ON EMPLEADO(id_jefe);
CREATE INDEX idx_empleado_central ON EMPLEADO(id_central);

-- EQUIPO
CREATE INDEX idx_equipo_central ON EQUIPO(id_central);
CREATE INDEX idx_equipo_estado ON EQUIPO(estado);

-- CLIENTE
CREATE INDEX idx_cliente_ruc ON CLIENTE(ruc);
CREATE INDEX idx_cliente_tipo ON CLIENTE(tipo_cliente);
CREATE INDEX idx_cliente_central ON CLIENTE(id_central);

-- PROYECTO_RSE
CREATE INDEX idx_proyecto_estado ON PROYECTO_RSE(estado);
CREATE INDEX idx_proyecto_central ON PROYECTO_RSE(id_central);

-- EMPLEADO_PROYECTO (TABLA INTERMEDIA m:N)
CREATE INDEX idx_emp_proy_empleado ON EMPLEADO_PROYECTO(id_empleado);
CREATE INDEX idx_emp_proy_proyecto ON EMPLEADO_PROYECTO(id_proyecto);

-- PROVEEDOR
CREATE INDEX idx_proveedor_ruc ON PROVEEDOR(ruc);
CREATE INDEX idx_proveedor_tipo ON PROVEEDOR(tipo_servicio);

-- COMPRA
CREATE INDEX idx_compra_proveedor ON COMPRA(id_proveedor);
CREATE INDEX idx_compra_proyecto ON COMPRA(id_proyecto);
CREATE INDEX idx_compra_fecha ON COMPRA(fecha);

-- CONTRATO_ENERGIA
CREATE INDEX idx_contrato_cliente ON CONTRATO_ENERGIA(id_cliente);
CREATE INDEX idx_contrato_fecha_ini ON CONTRATO_ENERGIA(fecha_inicio);

-- FACTURACION
CREATE INDEX idx_facturacion_contrato ON FACTURACION(id_contrato);
CREATE INDEX idx_facturacion_estado ON FACTURACION(estado_pago);
CREATE INDEX idx_facturacion_fecha ON FACTURACION(fecha_emision);
