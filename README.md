# Sistema de Gestión Andehydro 🌊⚡

Sistema de gestión integral para la Central Hidroeléctrica **ANDEHYDRO**. Este proyecto combina una robusta base de datos Oracle con una aplicación de escritorio Java (Swing) para gestionar empleados, facturación, contratos de energía y proyectos de responsabilidad social.

## 🚀 Características
- **Arquitectura Multicapa:** Separación clara entre UI, Lógica de Negocio (Service) y Acceso a Datos (DAO).
- **Lógica de Base de Datos:** Uso avanzado de PL/SQL incluyendo Triggers para auditoría, Procedimientos Almacenados para inserciones complejas y Funciones para cálculos de deuda.
- **Validaciones:** Capa de servicio que asegura la integridad de los datos antes de la persistencia.
- **Interfaz Gráfica:** Gestión intuitiva mediante formularios Java Swing (JFrame).

## 🛠️ Tecnologías Utilizadas
- **Java:** JDK 17+
- **Swing:** Para la interfaz de usuario.
- **Oracle Database:** Motor de base de datos.
- **JDBC:** Conector para la comunicación Java-SQL.

## 📂 Estructura del Proyecto
- `src/main/java/com/andehydro/model`: Clases de entidad (POJOs).
- `src/main/java/com/andehydro/service`: Lógica de negocio y validaciones.
- `src/main/java/com/andehydro/dao`: Consultas SQL y operaciones CRUD.
- `src/main/java/com/andehydro/util`: Configuración de la conexión JDBC.
- `src/main/java/com/andehydro/ui`: Ventanas y formularios (JFrames).
- `/sql`: Scripts de creación de tablas, procedimientos, funciones y triggers.

## ⚙️ Configuración e Instalación
1. **Base de Datos:**
   - Ejecuta los scripts en la carpeta `/sql` en tu instancia de Oracle (usuario `ANDEHYDRO`).
2. **Conexión Java:**
   - En la clase `ConnectionUtil`, asegúrate de configurar las credenciales correctas mediante el método `setCredentials`.
   - Asegúrate de incluir el archivo `ojdbc8.jar` (o superior) en las librerías del proyecto.

## 📝 Ejemplo de Código (Capa Service)
```java
// Ejemplo de búsqueda de empleado por ID usando JDBC
public Empleado findById(Connection conn, String id) throws Exception {
    String sql = "SELECT * FROM EMPLEADO WHERE id_empleado = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Empleado(rs.getString("nombres"), rs.getString("apellidos"), ...);
        }
    }
    return null;
}
