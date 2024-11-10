-- ******************************* CREACION DE LA BASE DE DATOS *************************************
-- 1 ejecucion
-- CREATE DATABASE proyectoBD;

-- ******************************* USO DE LA BASE DE DATOS ********************************************
-- 2 ejecucion
USE proyectoBD;

-- ******************************** CREACIONES DE TABLAS******************************************************

select * from usuario;

-- 3 ejecucion
/*CREATE TABLE usuario (
	dni BIGINT NOT NULL,
    nombre VARCHAR(45) NOT NULL,
	apellido VARCHAR(45),
    contrasena VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL,
	telefono VARCHAR(100),
	rol VARCHAR(20) NOT NULL DEFAULT 'Cliente';
    PRIMARY KEY(dni)
);*/

-- 4 ejecucion
/*CREATE TABLE cliente (
	dni BIGINT NOT NULL,
	fechaIngreso DATETIME DEFAULT GETDATE(),
    domicilio VARCHAR(100),
	RegimenLaboral VARCHAR(100) NOT NULL,
    PRIMARY KEY(dni), 
    CONSTRAINT fk_cliente FOREIGN KEY(dni) REFERENCES usuario(dni) ON DELETE CASCADE
);*/

-- 5 ejecucion
/*CREATE TABLE mecanico (
	dni BIGINT NOT NULL,
	fechaIngreso DATETIME DEFAULT GETDATE(),
	sueldo DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY(dni),
    CONSTRAINT fk_administrador FOREIGN KEY(dni) REFERENCES usuario(dni) ON DELETE CASCADE
);*/

-- 6 ejecucion
/*CREATE TABLE vehiculo (
    id_vehiculo INT IDENTITY(1,1),
    marca VARCHAR(45) NOT NULL,
    modelo VARCHAR(45) NOT NULL,
    anio INT NOT NULL,
    dni_cliente BIGINT NOT NULL,
	PRIMARY KEY(id_vehiculo),
    CONSTRAINT fk_vehiculo_cliente FOREIGN KEY (dni_cliente) REFERENCES cliente(dni)
);*/

-- 7 ejecucion
/*CREATE TABLE servicio (
    id_servicio INT IDENTITY(1,1),
    nombre VARCHAR(45) NOT NULL,
    costo DECIMAL(10, 2) NOT NULL,
    estado VARCHAR(45) DEFAULT 'DISPONIBLE',
	PRIMARY KEY(id_servicio)
);*/

-- 8 ejecucion
/*CREATE TABLE turno (
    id_turno INT IDENTITY(1,1),
    fecha DATE DEFAULT GETDATE() NOT NULL,
    id_vehiculo INT NOT NULL,
    dni_cliente BIGINT NOT NULL,
    PRIMARY KEY(id_turno),
    CONSTRAINT fk_turno_vehiculo FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id_vehiculo),
    CONSTRAINT fk_turno_cliente FOREIGN KEY (dni_cliente) REFERENCES cliente(dni),
);*/

-- 9 ejecucion
/*CREATE TABLE turno_servicio (
    id_turno INT NOT NULL,
    id_servicio INT NOT NULL,
    PRIMARY KEY (id_turno, id_servicio),
    CONSTRAINT fk_turnoservicio_turno FOREIGN KEY (id_turno) REFERENCES turno(id_turno),
    CONSTRAINT fk_turnoservicio_servicio FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio)
);*/

-- ************************************ SEDEERS ***********************************************

-- Prueba de eliminacion de cliente y sus relaciones con vehiculo, turno y turno_servicio aun  HECHA
-- CLIENTE SOFIA ELIMINADO

-- Seeder para la tabla Usuario
/*INSERT INTO usuario (dni, nombre, apellido, contrasena, correo, telefono, rol)
VALUES
(12345678, 'Juan', 'Gomez', 'contraseña123', 'juan.gomez@ejemplo.com', '555-1234'),
(87654321, 'María', 'Fernández', 'abc456def', 'maria.fernandez@ejemplo.com', '555-5678'),
(46924236, 'Diego', 'Gomez', 'micontra123', 'diego.gomez@ejemplo.com', '555-9012', 'Mecanico'),
(24681012, 'Ana', 'Martínez', 'mno012pqr', 'ana.martinez@ejemplo.com', '555-3456'),
(46813579, 'Pedro', 'Sánchez', 'stu345vwx', 'pedro.sanchez@ejemplo.com', '555-7890'),
(97531246, 'Sofía', 'Díaz', 'yz456abc', 'sofia.diaz@ejemplo.com', '555-2468'), -- CLIENTE SOFIA ELIMINADO
(31415926, 'Miguel', 'Torres', 'def789ghi', 'miguel.torres@ejemplo.com', '555-6789', 'Mecanico'),
(65432109, 'Laura', 'Ramírez', 'jkl012mno', 'laura.ramirez@ejemplo.com', '555-0123', 'Mecanico'),
(98765432, 'David', 'Castillo', 'pqr345stu', 'david.castillo@ejemplo.com', '555-4567'),
(54321098, 'Alejandra', 'Herrera', 'vwx678yz', 'alejandra.herrera@ejemplo.com', '555-8901');*/

-- Seeder para la tabla Cliente
/*INSERT INTO cliente (dni, fechaIngreso, domicilio, RegimenLaboral)
VALUES
(12345678, GETDATE(), '123 Calle Principal', 'Exento'),
(87654321, GETDATE(), '456 Avenida del Robler', 'Responsable Inscripto'),
(24681012, GETDATE(), '789 Calle del Olmo', 'Monotributista'),
(46813579, GETDATE(), '321 Calle del Pino', 'Exento'),
(97531246, GETDATE(), '321 Calle 3', 'Exento'), -- CLIENTE SOFIA ELIMINADO
(98765432, GETDATE(), '321 Calle 4', 'Exento')
(54321098, GETDATE(), '321 Calle 5', 'Exento');*/

-- Seeder para la tabla Mecanico
/*INSERT INTO mecanico (dni, fechaIngreso, sueldo)
VALUES
(46924236, GETDATE(), 50000.00),
(31415926, GETDATE(), 55000.00),
(65432109, GETDATE(), 60000.00);*/

-- Insertar en la tabla servicio
/*INSERT INTO servicio(nombre, costo)
VALUES
('Cambio de Aceite', 15000),
('Servi Completo', 60000),
('Alineacion y Balanceo', 40000),
('Revision General', 25000);*/

DELETE FROM servicio WHERE id_servicio = 2;

select * from usuario;

-- Cargo mas vehiculos y turnos
INSERT INTO vehiculo(marca, modelo, anio, dni_cliente)
VALUES
('Toyota', 'Hilux', 2024, 54321098), -- Cliente Alejandra
('Toyota', 'Corolla', 2022, 54321098), -- Cliente Alejandra
('Ford', 'Ranger', 2018, 97531246); -- CLIENTE SOFIA ELIMINADO

-- Ver todos los vehiculos registrados
select c.dni AS 'DNI_CLIENTE', u.nombre AS 'NOMBRE_CLIENTE',v.id_vehiculo AS 'ID_VEHICULO', v.marca AS 'MARCA_VEHICULO', v.modelo AS 'MODELO_VEHICULO', v.anio AS 'AÑO_VEHICULO' from vehiculo v
left join cliente c on v.dni_cliente = c.dni
left join usuario u on c.dni = u.dni;

-- Cargo turnos
INSERT INTO turno(id_vehiculo, dni_cliente)
VALUES
(6, 54321098), -- Vehiculo 1 de Alejandra
(7, 54321098), -- Vehiculo 2 de Alejandra
(8, 97531246); -- CLIENTE SOFIA ELIMINADO

delete from vehiculo where id_vehiculo = 6;

select * from turno where dni_cliente = 54321098;

-- asigno los servicios a los turnos
INSERT INTO turno_servicio(id_turno, id_servicio)
VALUES
(6, 1),  -- Asocia el turno 1 solicitado por Alejandra con el servicio 1
(6, 3),  -- Asocia el turno 1 solicitado por Alejandra con el servicio 3
(7, 2),  -- Asocia el turno 2 solicitado por Alejandra con el servicio 2
(8, 4);  -- CLIENTE SOFIA ELIMINADO

-- ver la informacion completa de los turnos
SELECT t.id_turno, t.fecha, v.id_vehiculo, v.marca, v.modelo, u.nombre, u.apellido, s.nombre AS servicio, s.costo
FROM turno t
JOIN turno_servicio ts ON t.id_turno = ts.id_turno
JOIN servicio s ON ts.id_servicio = s.id_servicio
JOIN vehiculo v ON t.id_vehiculo = v.id_vehiculo
JOIN cliente c ON t.dni_cliente = c.dni
JOIN usuario u ON c.dni = u.dni;

SELECT id_turno FROM turno WHERE id_vehiculo = 6;

select * from vehiculo;
select * from usuario;

-- eliminar usuarios que no son clientes ni mecanicos
delete from usuario where rol <> 'Cliente' and rol <> 'Mecanico';

-- ******************************************** PROCEDIMIENTOS ALMACENADOS ********************************************************************
-- De la tabla MECANICO
-- esta
/*CREATE PROCEDURE agregar_mecanico
	@dni BIGINT,
	@nombre VARCHAR(100),
	@apellido VARCHAR(100),
    @contrasena VARCHAR(100),
    @correo VARCHAR(100),
	@telefono VARCHAR(100),
	@rol VARCHAR(20),
	@sueldo DECIMAL(10, 2)
AS
BEGIN
	INSERT INTO usuario(dni, nombre, apellido, contrasena, correo, telefono, rol)
	VALUES(@dni, @nombre, @apellido, @contrasena, @correo, @telefono, @rol);
	INSERT INTO mecanico(dni, sueldo)
	VALUES(@dni, @sueldo);
END*/

-- esta
/*CREATE PROCEDURE listar_mecanicos
AS
BEGIN
	SELECT 
		u.dni,
		u.nombre,
		u.apellido,
		u.telefono,
		m.fechaIngreso,
		m.sueldo
	FROM mecanico m
	LEFT JOIN usuario u ON m.dni = u.dni
END*/

-- esta
/*CREATE PROCEDURE actualizar_mecanico
	@dni BIGINT,
	@contrasena VARCHAR(100),
	@telefono VARCHAR(100),
	@correo VARCHAR(100),
	@sueldo DECIMAL(10, 2)
AS
BEGIN
	UPDATE usuario SET contrasena = @contrasena, telefono = @telefono, correo = @correo WHERE dni = @dni;
	UPDATE mecanico SET sueldo = @sueldo WHERE dni = @dni;
END*/

-- esta
/*CREATE PROCEDURE buscar_mecanico
	@dni BIGINT
AS
BEGIN
	SELECT 
		u.dni,
		u.nombre,
		u.apellido,
		u.contrasena,
		u.correo,
		u.telefono,
		m.fechaIngreso,
		m.sueldo
	FROM mecanico m
	LEFT JOIN usuario u ON m.dni = u.dni
	WHERE m.dni = @dni
END*/

-- De la tabla CLIENTE
-- esta
/*CREATE PROCEDURE agregar_cliente
	@dni BIGINT,
	@nombre VARCHAR(100),
	@apellido VARCHAR(45),
    @contrasena VARCHAR(100),
    @correo VARCHAR(100),
	@telefono VARCHAR(100),
	@domicilio VARCHAR(100),
	@RegimenLaboral VARCHAR(100),
	@rol VARCHAR(20)
AS
BEGIN
	INSERT INTO usuario(dni, nombre, apellido, contrasena, correo, telefono, rol)
	VALUES(@dni, @nombre, @apellido, @contrasena, @correo, @telefono, @rol);
	INSERT INTO cliente(dni, domicilio, RegimenLaboral)
	VALUES(@dni, @domicilio, @RegimenLaboral);
END*/

-- esta (Del lado del mecanico)
/*CREATE PROCEDURE listar_clientes
AS
BEGIN
	select
		u.dni,
		u.nombre,
		u.apellido,
		u.contrasena,
		u.correo,
		u.telefono,
		c.fechaIngreso,
		c.domicilio,
		c.RegimenLaboral
	from cliente c
	left join usuario u on c.dni = u.dni
END*/

-- esta (Del lado del mecanico)
/*CREATE PROCEDURE buscar_cliente
	@dni BIGINT
AS
BEGIN
	SELECT
		u.dni,
		u.nombre,
		u.apellido,
		u.telefono,
		c.fechaIngreso,
		c.domicilio,
		c.RegimenLaboral AS 'regimen'
	FROM cliente c
	LEFT JOIN usuario u ON c.dni = u.dni
	WHERE c.dni = @dni
	ORDER BY u.nombre
END*/

-- esta (Del lado del cliente)
/*CREATE PROCEDURE ver_datos_del_cliente
	@dni BIGINT
AS
BEGIN
	SELECT
		u.dni,
		u.nombre,
		u.apellido,
		u.contrasena,
		u.correo,
		u.telefono,
		c.fechaIngreso,
		c.domicilio,
		c.RegimenLaboral AS 'regimen'
	FROM cliente c
	LEFT JOIN usuario u ON c.dni = u.dni
	WHERE c.dni = @dni
	ORDER BY u.nombre
END*/

-- esta (Del lado del cliente)
/*CREATE PROCEDURE actualizar_cliente
	@dni BIGINT,
	@contrasena VARCHAR(100),
	@telefono VARCHAR(100),
	@correo VARCHAR(100),
	@domicilio VARCHAR(100)
AS
BEGIN
	UPDATE usuario SET contrasena = @contrasena, telefono = @telefono, correo = @correo WHERE dni = @dni;
	UPDATE cliente SET domicilio = @domicilio WHERE dni = @dni;
END*/

-- De la tabla USUARIO
-- esta
/*CREATE PROCEDURE listar_usuarios
AS
BEGIN
	SELECT
		u.dni,
		u.nombre,
		u.apellido,
		u.contrasena,
		u.correo,
		u.telefono
	FROM usuario u
	LEFT JOIN cliente c ON u.dni = c.dni
	LEFT JOIN mecanico m ON u.dni = m.dni
	WHERE c.dni IS NULL AND m.dni IS NULL;
END*/

-- esta
/*CREATE PROCEDURE buscar_usuario
	@dni BIGINT
AS
BEGIN
	SELECT
		u.dni,
		u.nombre,
		u.apellido,
		u.contrasena,
		u.correo,
		u.telefono
	FROM usuario u
	LEFT JOIN cliente c ON u.dni = c.dni
	LEFT JOIN mecanico m ON u.dni = m.dni
	WHERE u.dni = @dni AND c.dni IS NULL AND m.dni IS NULL;
END*/

-- De la tabla VEHICULOS
-- esta -- usado
/*CREATE PROCEDURE agregar_vehiculo
	@marca VARCHAR(45),
	@modelo VARCHAR(45),
	@anio INT,
	@dni_cliente BIGINT
AS
BEGIN
	INSERT INTO vehiculo(marca, modelo, anio, dni_cliente)
	VALUES(@marca, @modelo, @anio, @dni_cliente)
END*/

-- esta -- usado
/*CREATE PROCEDURE actualizar_vehiculo
	@id_vehiculo INT,
	@marca VARCHAR(45),
	@modelo VARCHAR(45),
	@anio INT,
	@dni_cliente BIGINT
AS
BEGIN
	UPDATE vehiculo 
	SET 
	marca = @marca, 
	modelo = @modelo, 
	anio = @anio
	WHERE id_vehiculo = @id_vehiculo AND dni_cliente = @dni_cliente
END*/

-- esta -- usado
/*CREATE PROCEDURE buscar_vehiculo
	@id_vehiculo INT
AS
BEGIN
	SELECT 
		id_vehiculo,
		marca,
		modelo,
		anio,
		dni_cliente
	FROM vehiculo
	WHERE id_vehiculo = @id_vehiculo;
END*/

-- esta -- usado
/*CREATE PROCEDURE verificar_vehiculo_a_modificar
	@id_vehiculo INT,
	@dni_cliente BIGINT
AS
BEGIN
	SELECT 
		1
	FROM vehiculo
	WHERE id_vehiculo = @id_vehiculo AND dni_cliente = @dni_cliente;
END*/

-- esta -- usado
/*CREATE PROCEDURE listar_vehiculos_de_un_cliente
	@dni_cliente BIGINT
AS
BEGIN
	select
		v.id_vehiculo,
		v.marca,
		v.modelo,
		v.anio
	from vehiculo v
	join cliente c on v.dni_cliente = c.dni
	where c.dni = @dni_cliente;
END*/

-- DE LA TABLA CLIENTE
-- esta
/*CREATE PROCEDURE eliminacion_completa_de_cliente
    @dni_cliente BIGINT
AS
BEGIN
    -- Eliminar turnos asociados
    DELETE FROM turno_servicio
    WHERE id_turno IN (
        SELECT id_turno FROM turno
        WHERE dni_cliente = @dni_cliente
    );

    DELETE FROM turno
    WHERE dni_cliente = @dni_cliente;

    -- Eliminar vehículos asociados
    DELETE FROM vehiculo
    WHERE dni_cliente = @dni_cliente;

	-- antes de eliminar el cliente le sacamos los roles posibles y le pongo vacio
	-- porque aun existira en tabla usuario
	UPDATE usuario SET rol = '' WHERE dni = @dni_cliente;

    -- Eliminar el cliente
    DELETE FROM cliente
    WHERE dni = @dni_cliente;
END;*/

-- DE LA TABLA SERVICIO
-- esta
/*CREATE PROCEDURE agregar_servicio
	@nombre VARCHAR(45),
	@costo DECIMAL(10,2)
AS
BEGIN
	INSERT INTO servicio(nombre, costo)
	VALUES(@nombre, @costo)
END*/

/*CREATE PROCEDURE actualizar_servicio
	@id_servicio INT,
	@nombre VARCHAR(45),
	@costo DECIMAL(10,2)
AS
BEGIN
	UPDATE servicio
	SET
		nombre = @nombre,
		costo = @costo
	WHERE id_servicio = @id_servicio
END*/

-- DE LA TABLA  TURNO
-- esta -- NO USO
/*CREATE PROCEDURE agregar_turno
	@id_vehiculo INT,
	@dni_cliente BIGINT
AS
BEGIN
	INSERT INTO turno(id_vehiculo, dni_cliente)
	VALUES(@id_vehiculo, @dni_cliente)
END*/

insert into servicio(nombre, costo) values('otro servi', 122)

select * from servicio;

delete from servicio where id_servicio = 5;

update servicio set estado = 'DISPONIBLE' where id_servicio = 2;

select 1 from turno_servicio where id_servicio = 4;

-- esta
/*CREATE PROCEDURE listar_info_completa_de_turno
AS
BEGIN
	SELECT 
		t.id_turno, 
		t.fecha, 
		v.marca, 
		v.modelo, 
		u.nombre, 
		u.apellido, 
		s.nombre AS servicio, 
		s.costo
	FROM turno t
	JOIN turno_servicio ts ON t.id_turno = ts.id_turno
	JOIN servicio s ON ts.id_servicio = s.id_servicio
	JOIN vehiculo v ON t.id_vehiculo = v.id_vehiculo
	JOIN cliente c ON t.dni_cliente = c.dni
	JOIN usuario u ON c.dni = u.dni
	ORDER BY u.nombre
END*/

-- esta
/*CREATE PROCEDURE ver_mis_turnos
	@dni BIGINT
AS
BEGIN
	SELECT 
		t.id_turno, 
		t.fecha, 
		v.marca, 
		v.modelo, 
		s.nombre AS servicio, 
		s.costo
	FROM turno t
	JOIN turno_servicio ts ON t.id_turno = ts.id_turno
	JOIN servicio s ON ts.id_servicio = s.id_servicio
	JOIN vehiculo v ON t.id_vehiculo = v.id_vehiculo
	JOIN cliente c ON t.dni_cliente = c.dni
	JOIN usuario u ON c.dni = u.dni
	WHERE c.dni = @dni;
END*/

-- esta
/*CREATE PROCEDURE listar_info_completa_de_vehiculo
AS
BEGIN
	SELECT 
		v.id_vehiculo AS 'id', 
		u.nombre AS 'cliente', 
		v.marca, 
		v.modelo, 
		v.anio AS 'año'
	FROM vehiculo v
	LEFT JOIN cliente c ON v.dni_cliente = c.dni
	LEFT JOIN usuario u ON c.dni = u.dni
	ORDER BY u.nombre;
END*/

--select * from usuario;

-- update servicio set nombre = 'Servis Completo', costo = 42000, estado = 'NO DISPONIBLE' WHERE id_servicio = 2;

select * from servicio;

--	****************************************************** CONSULTAS ******************************************************************
-- listar todos los datos de las tablas
select * from usuario u left join mecanico m on u.dni = m.dni where rol = 'Mecanico' and sueldo >= 50000;
select * from cliente;

-- select * from vehiculo;

-- listar los mecanicos
select 
	u.dni,
	u.nombre,
	u.apellido,
	u.contrasena,
	u.correo,
	u.telefono,
	m.fechaIngreso,
	m.sueldo
from mecanico m
left join usuario u on m.dni = u.dni
order by m.sueldo desc;

select * from usuario;

delete from usuario where dni = 332211;

-- listar los clientes
select
	u.dni,
	u.nombre,
	u.apellido,
	u.contrasena,
	u.correo,
	u.telefono,
	c.fechaIngreso,
	c.domicilio,
	c.RegimenLaboral
from cliente c
left join usuario u on c.dni = u.dni
order by u.nombre;

-- listar un cliente en especifico
SELECT u.dni, u.nombre, u.apellido, c.domicilio
FROM cliente c
LEFT JOIN usuario u ON c.dni = u.dni
WHERE c.dni = 234567890;

-- Listar todos los usuarios que no son ni clientes ni mecanicos
-- NO USUARIAMOS
/*SELECT
	u.dni,
	u.nombre,
	u.apellido,
	u.contrasena,
	u.correo,
	u.telefono,
	u.rol
FROM usuario u
LEFT JOIN cliente c ON u.dni = c.dni
LEFT JOIN mecanico m ON u.dni = m.dni
WHERE c.dni IS NULL AND m.dni IS NULL;*/

-- Buscar usuario que no es ni cliente ni mecanico
-- NO USARIAMOS
/*SELECT
	u.dni,
	u.nombre,
	u.apellido,
	u.contrasena,
	u.correo,
	u.telefono
FROM usuario u
LEFT JOIN cliente c ON u.dni = c.dni
LEFT JOIN mecanico m ON u.dni = m.dni
WHERE u.dni = 31415926 AND c.dni IS NULL AND m.dni IS NULL;*/

-- ver los vehiculos de los clientes
select v.id_vehiculo as 'id', u.nombre as 'cliente', v.marca, v.modelo, v.anio as 'año' from vehiculo v
left join cliente c on v.dni_cliente = c.dni
left join usuario u on c.dni = u.dni
order by u.nombre;

-- Obtener Información Básica de Turnos
/*SELECT t.id_turno, t.fecha, v.marca, v.modelo, v.anio, u.nombre, u.apellido
FROM turno t
JOIN vehiculo v ON t.id_vehiculo = v.id_vehiculo
JOIN cliente c ON t.dni_cliente = c.dni
JOIN usuario u ON c.dni = u.dni;*/

-- Obtener Servicios Asociados a Cada Turno
/*SELECT t.id_turno, t.fecha, s.nombre AS servicio, s.costo
FROM turno t
JOIN turno_servicio ts ON t.id_turno = ts.id_turno
JOIN servicio s ON ts.id_servicio = s.id_servicio;*/

select * from usuario;

-- Obtener Información Completa de Turnos con Servicios
SELECT t.id_turno, t.fecha, v.marca, v.modelo, u.nombre, u.apellido, s.nombre AS servicio, s.costo
FROM turno t
JOIN turno_servicio ts ON t.id_turno = ts.id_turno
JOIN servicio s ON ts.id_servicio = s.id_servicio
JOIN vehiculo v ON t.id_vehiculo = v.id_vehiculo
JOIN cliente c ON t.dni_cliente = c.dni
JOIN usuario u ON c.dni = u.dni;


-- Calcular el Costo Total de Servicios por Turno
/*SELECT t.id_turno, u.nombre, t.fecha, SUM(s.costo) AS costo_total
FROM turno t
JOIN cliente c on t.dni_cliente = c.dni
JOIN usuario u on c.dni = u.dni
JOIN turno_servicio ts ON t.id_turno = ts.id_turno
JOIN servicio s ON ts.id_servicio = s.id_servicio
GROUP BY t.id_turno, u.nombre, t.fecha;*/

-- ******************************************** PRUEBA DE ELIMINACION DE DATOS **************************************************
--DELETE FROM cliente WHERE dni = 44745058;


-- ******************************************** ELIMINACION DE TABLAS y PROCEDIMIENTOS ****************************************************
/*DROP TABLE IF EXISTS administrador;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS usuario;*/

/*DROP PROCEDURE dbo.actualizar_administrador;
DROP PROCEDURE dbo.actualizar_cliente;
DROP PROCEDURE dbo.agregar_administrador;
DROP PROCEDURE dbo.agregar_cliente;
DROP PROCEDURE dbo.buscar_administrador;
DROP PROCEDURE dbo.buscar_cliente;
DROP PROCEDURE dbo.buscar_usuario;
DROP PROCEDURE dbo.listar_administradores;
DROP PROCEDURE dbo.listar_clientes;
DROP PROCEDURE dbo.listar_usuarios;*/

