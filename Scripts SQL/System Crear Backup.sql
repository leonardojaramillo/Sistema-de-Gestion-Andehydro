--Conéctate como SYSDBA:
CREATE OR REPLACE DIRECTORY BACKUP_DIR AS '/u01/backups';
GRANT READ, WRITE ON DIRECTORY BACKUP_DIR TO ANDEHYDRO;


SELECT username, profile
FROM dba_users
WHERE username = 'ANDEHYDRO';

SELECT * 
FROM dba_profiles
WHERE profile = 'DEFAULT'
ORDER BY resource_name;

CREATE PROFILE C##PRFLIMIT LIMIT
    SESSIONS_PER_USER 1
    IDLE_TIME 10
    PASSWORD_LIFE_TIME 60;


ALTER USER ANDEHYDRO PROFILE C##PRFLIMIT;


--Cambiar tiempo máximo inactivo (IDLE_TIME)
ALTER PROFILE C##PRFLIMIT LIMIT
    IDLE_TIME 5;

--Cambiar política de vida útil de contraseña
ALTER PROFILE C##PRFLIMIT LIMIT
    PASSWORD_LIFE_TIME 30;

--Cambiar intentos fallidos máximos
ALTER PROFILE C##PRFLIMIT LIMIT
    FAILED_LOGIN_ATTEMPTS 3;

--Agregar o cambiar límite de sesiones simultáneas
ALTER PROFILE C##PRFLIMIT LIMIT
    SESSIONS_PER_USER 2;
    
--Habilitar el bloqueo automático de cuentas 
ALTER PROFILE C##PRFLIMIT LIMIT
    PASSWORD_LOCK_TIME 1;

--Evitar que una contraseña vuelva a usarse (historial)
ALTER PROFILE PRFLIMIT LIMIT
    PASSWORD_REUSE_MAX 5
    PASSWORD_REUSE_TIME 30;





