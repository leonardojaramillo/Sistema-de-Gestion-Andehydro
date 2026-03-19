-- Conectar como ANDEHYDRO antes de ejecutar

-- 1) CENTRAL_HIDROELECTRICA
CREATE TABLE CENTRAL_HIDROELECTRICA (
    id_central       VARCHAR2(10)   PRIMARY KEY,
    nombre           VARCHAR2(100)  NOT NULL,
    ubicacion        VARCHAR2(255)  NOT NULL,
    capacidad_MW     NUMBER(6,2)    NOT NULL,
    año_inicio       NUMBER(4)      NOT NULL,
    tipo_planta      VARCHAR2(60)   DEFAULT 'HIDRAULICA' NOT NULL,
    CONSTRAINT chk_tipo_planta CHECK (tipo_planta IN ('HIDRAULICA','MIXTA-SOLAR','MIXTA-EOLICA'))
);

-- 2) EMPLEADO
CREATE TABLE EMPLEADO (
    id_empleado      VARCHAR2(10)   PRIMARY KEY,
    nombres          VARCHAR2(50)   NOT NULL,
    apellidos        VARCHAR2(80)   NOT NULL,
    cargo            VARCHAR2(50)   NOT NULL,
    area             VARCHAR2(50)   NOT NULL,
    fecha_ingreso    DATE           NOT NULL,
    id_jefe          VARCHAR2(10),
    id_central       VARCHAR2(10)   NOT NULL,
    CONSTRAINT fk_empleado_jefe FOREIGN KEY (id_jefe) REFERENCES EMPLEADO(id_empleado),
    CONSTRAINT fk_empleado_central FOREIGN KEY (id_central) REFERENCES CENTRAL_HIDROELECTRICA(id_central)
);

-- 3) EQUIPO
CREATE TABLE EQUIPO (
    id_equipo        VARCHAR2(10)   PRIMARY KEY,
    tipo_equipo      VARCHAR2(50)   NOT NULL,
    marca            VARCHAR2(50)   NOT NULL,
    modelo           VARCHAR2(50)   NOT NULL,
    fecha_instalacion DATE          NOT NULL,
    estado           VARCHAR2(20)   DEFAULT 'Operativo' NOT NULL,
    id_central       VARCHAR2(10)   NOT NULL,
    CONSTRAINT chk_estado_equipo CHECK (estado IN ('Operativo','Mantenimiento','Inactivo')),
    CONSTRAINT fk_equipo_central FOREIGN KEY (id_central) REFERENCES CENTRAL_HIDROELECTRICA(id_central)
);

-- 4) CLIENTE
CREATE TABLE CLIENTE (
    id_cliente       VARCHAR2(10)   PRIMARY KEY,
    nombre           VARCHAR2(100)  NOT NULL,
    razon_social     VARCHAR2(100)  NOT NULL,
    tipo_cliente     VARCHAR2(30)   NOT NULL,
    ruc              VARCHAR2(20)   NOT NULL,
    direccion        VARCHAR2(250)  NOT NULL,
    telefono         VARCHAR2(20)   NOT NULL,
    id_central       VARCHAR2(10)   NOT NULL,
    CONSTRAINT fk_cliente_central FOREIGN KEY (id_central) REFERENCES CENTRAL_HIDROELECTRICA(id_central)
);

-- 5) PROYECTO_RSE
CREATE TABLE PROYECTO_RSE (
    id_proyecto      VARCHAR2(10)   PRIMARY KEY,
    nombre           VARCHAR2(100)  NOT NULL,
    descripcion      VARCHAR2(1000),
    inversion        NUMBER(12,2)   NOT NULL,
    fecha_inicio     DATE           NOT NULL,
    fecha_fin        DATE,
    estado           VARCHAR2(20)   NOT NULL,
    id_central       VARCHAR2(10)   NOT NULL,
    CONSTRAINT fk_proyecto_central FOREIGN KEY (id_central) REFERENCES CENTRAL_HIDROELECTRICA(id_central)
);

-- 6) EMPLEADO_PROYECTO (m:N)
CREATE TABLE EMPLEADO_PROYECTO (
    id_empleado      VARCHAR2(10)   NOT NULL,
    id_proyecto      VARCHAR2(10)   NOT NULL,
    rol              VARCHAR2(50)   NOT NULL,
    fecha_asignacion DATE           NOT NULL,
    CONSTRAINT pk_empleado_proyecto PRIMARY KEY (id_empleado, id_proyecto),
    CONSTRAINT fk_emp_proy_emp FOREIGN KEY (id_empleado) REFERENCES EMPLEADO(id_empleado),
    CONSTRAINT fk_emp_proy_proy FOREIGN KEY (id_proyecto) REFERENCES PROYECTO_RSE(id_proyecto)
);

-- 7) PROVEEDOR
CREATE TABLE PROVEEDOR (
    id_proveedor     VARCHAR2(10)   PRIMARY KEY,
    nombre           VARCHAR2(100)  NOT NULL,
    razon_social     VARCHAR2(100)  NOT NULL,
    ruc              VARCHAR2(20)   NOT NULL,
    tipo_servicio    VARCHAR2(50)   NOT NULL,
    pais             VARCHAR2(50)   NOT NULL
);

-- 8) COMPRA
CREATE TABLE COMPRA (
    id_compra        VARCHAR2(10)   PRIMARY KEY,
    fecha            DATE           NOT NULL,
    descripcion      VARCHAR2(1000),
    monto            NUMBER(12,2)   NOT NULL,
    tipo_moneda      VARCHAR2(5)    NOT NULL,
    id_proveedor     VARCHAR2(10)   NOT NULL,
    id_proyecto      VARCHAR2(10)   NOT NULL,
    CONSTRAINT fk_compra_proveedor FOREIGN KEY (id_proveedor) REFERENCES PROVEEDOR(id_proveedor),
    CONSTRAINT fk_compra_proyecto FOREIGN KEY (id_proyecto) REFERENCES PROYECTO_RSE(id_proyecto)
);

-- 9) CONTRATO_ENERGIA
CREATE TABLE CONTRATO_ENERGIA (
    id_contrato      VARCHAR2(10)   PRIMARY KEY,
    fecha_inicio     DATE           NOT NULL,
    fecha_fin        DATE,
    tarifa_kw        NUMBER(10,4)   NOT NULL,
    tipo_moneda      VARCHAR2(5)    NOT NULL,
    id_cliente       VARCHAR2(10)   NOT NULL,
    CONSTRAINT chk_tipo_moneda_contrato CHECK (tipo_moneda IN ('USD','PEN','EUR')),
    CONSTRAINT fk_contrato_cliente FOREIGN KEY (id_cliente) REFERENCES CLIENTE(id_cliente)
);

-- 10) FACTURACION
CREATE TABLE FACTURACION (
    id_factura       VARCHAR2(10)   PRIMARY KEY,
    fecha_emision    DATE           NOT NULL,
    energia_consumida NUMBER(12,4)  NOT NULL,
    monto_total      NUMBER(12,2)   NOT NULL,
    estado_pago      VARCHAR2(20)   NOT NULL,
    id_contrato      VARCHAR2(10)   NOT NULL,
    CONSTRAINT fk_factura_contrato FOREIGN KEY (id_contrato) REFERENCES CONTRATO_ENERGIA(id_contrato)
);

