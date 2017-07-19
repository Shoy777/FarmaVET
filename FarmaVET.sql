/*Creacion de la BD*/
IF EXISTS(SELECT * FROM SYSDATABASES WHERE NAME = 'FarmaVETBD')
	BEGIN
		USE MASTER
		DROP DATABASE FarmaVETBD
	END
GO
CREATE DATABASE FarmaVETBD
GO
USE FarmaVETBD
GO

/*Tabla Especie*/
CREATE TABLE Especie
(
EspecieId int IDENTITY(1,1) NOT NULL,
Descripcion nvarchar(50) NOT NULL
)
ALTER TABLE Especie ADD CONSTRAINT EspeciePK PRIMARY KEY(EspecieId)
ALTER TABLE Especie ADD CONSTRAINT DescripcionEspecieUK UNIQUE(Descripcion)
GO
/*Tabla Laboratorio*/
CREATE TABLE Laboratorio
(
LaboratorioId int IDENTITY(1,1) NOT NULL,
Descripcion nvarchar(50) NOT NULL
)
ALTER TABLE Laboratorio ADD CONSTRAINT LaboratorioPK PRIMARY KEY(LaboratorioId)
ALTER TABLE Laboratorio ADD CONSTRAINT DescripcionLaboratorioUK UNIQUE(Descripcion)
GO
/*Tabla Tipo de Medicamento*/
CREATE TABLE TipoMedicamento
(
TipoMedicamentoId int IDENTITY(1,1) NOT NULL,
Descripcion nvarchar(50) NOT NULL
)
ALTER TABLE TipoMedicamento ADD CONSTRAINT TipoMedicamentoPK PRIMARY KEY(TipoMedicamentoId)
ALTER TABLE TipoMedicamento ADD CONSTRAINT DescripcionTipoMedicamentoUK UNIQUE(Descripcion)
GO
/*Tabla Medicamento*/
CREATE TABLE Medicamento
(
MedicamentoId int IDENTITY(1,1) NOT NULL,
Nombre nvarchar(100) NOT NULL,
EspecieId int NOT NULL,
TipoMedicamentoId int NOT NULL,
LaboratorioId int NOT NULL,
Descripcion nvarchar(300) NULL,
Precio decimal(6,2) NOT NULL,
Stock int NOT NULL,
Estado tinyint NOT NULL
)
ALTER TABLE Medicamento ADD CONSTRAINT MedicamentoPK PRIMARY KEY(MedicamentoId)
ALTER TABLE Medicamento ADD CONSTRAINT NombreEspecieUK UNIQUE(EspecieId,Nombre)
ALTER TABLE Medicamento ADD CONSTRAINT MedicamentoEspecieFK FOREIGN KEY(EspecieId) REFERENCES Especie
ALTER TABLE Medicamento ADD CONSTRAINT MedicamentoTipoMedicamentoFK FOREIGN KEY(TipoMedicamentoId) REFERENCES TipoMedicamento
ALTER TABLE Medicamento ADD CONSTRAINT MedicamentoLaboratorioFK FOREIGN KEY(LaboratorioId) REFERENCES Laboratorio
ALTER TABLE Medicamento ADD CONSTRAINT DefaultStockDK DEFAULT 0 FOR Stock
ALTER TABLE Medicamento ADD CONSTRAINT DefaultEstadoDK DEFAULT 1 FOR Estado
GO
/*Tabla Boleta*/
CREATE TABLE Boleta
(
BoletaId int NOT NULL IDENTITY(1,1),
Cliente nvarchar(50) NULL,
MontoTotal decimal(10,2) NOT NULL,
CantidadItems int NULL,
FechaVenta Datetime NOT NULL
)
ALTER TABLE Boleta ADD CONSTRAINT BoletaPK PRIMARY KEY(BoletaId)
GO
/*Tabla Detalle Boleta*/
CREATE TABLE DetalleBoleta
(
BoletaId int NOT NULL,
MedicamentoId int NOT NULL,
Precio decimal(6,2) NOT NULL,
Cantidad int NOT NULL,
SubTotal decimal(8,2) NOT NULL
)
ALTER TABLE DetalleBoleta ADD CONSTRAINT DetalleBoletaPK PRIMARY KEY(BoletaId,MedicamentoId)
ALTER TABLE DetalleBoleta ADD CONSTRAINT DetalleBoletaFK FOREIGN KEY(BoletaId) REFERENCES Boleta
ALTER TABLE DetalleBoleta ADD CONSTRAINT DetalleMedicamentoFK FOREIGN KEY(MedicamentoId) REFERENCES Medicamento
GO
/*
INSERT INTO Laboratorio VALUES('BAYER2')
INSERT INTO Especie VALUES('Perro2')
INSERT INTO TipoMedicamento VALUES('Antiestaminico2')
INSERT INTO Medicamento(Nombre,EspecieId,TipoMedicamentoId,LaboratorioId,Descripcion,Precio)
VALUES('Medicamento 1',1,1,1,'Descripcion 1',15.0)
exec SP_RegistrarMedicamento 'Medicamento 2',1,2,2,'Descripcion 1',15.0
SELECT * FROM MEDICAMENTO
GO
*/
/*Registrar Medicamento*/
CREATE PROCEDURE SP_RegistrarMedicamento(
	@Nombre nvarchar(100),
	@EspecieId int,
	@TipoMedicamentoId int,
	@LaboratorioId int,
	@Descripcion nvarchar(300),
	@Precio decimal(6,2)
)
AS
BEGIN
	INSERT INTO Medicamento(Nombre,EspecieId,TipoMedicamentoId,LaboratorioId,Descripcion,Precio)
	VALUES(
		@Nombre,
		@EspecieId,
		@TipoMedicamentoId,
		@LaboratorioId,
		@Descripcion,
		@Precio
	)
END
GO
/*Modificar Medicamento*/
CREATE PROCEDURE SP_ModificarMedicamento(
	@MedicamentoId int,
	@Nombre nvarchar(100),
	@EspecieId int,
	@TipoMedicamentoId int,
	@LaboratorioId int,
	@Descripcion nvarchar(300),
	@Precio decimal(6,2)
)
AS
BEGIN
	UPDATE Medicamento SET
		Nombre = @Nombre,
		EspecieId = @EspecieId,
		TipoMedicamentoId = @TipoMedicamentoId,
		LaboratorioId = @LaboratorioId,
		Descripcion = @Descripcion,
		Precio = @Precio
	WHERE MedicamentoId = @MedicamentoId
END
GO
/*Dar de Baja Medicamento*/
CREATE PROCEDURE SP_DarBajaMedicamento(
	@MedicamentoId int
)
AS
BEGIN
	UPDATE Medicamento SET Estado = 0 WHERE MedicamentoId = @MedicamentoId AND Stock = 0
END
GO
/*Listado de medicamentos stock*/
CREATE PROCEDURE SP_ListarMedicamentosVenta
AS
BEGIN
	SELECT * FROM Medicamento WHERE Estado = 1 AND Stock > 0
END
GO
/*Listado de medicamentos eliminados*/
CREATE PROCEDURE SP_ListarMedicamentosDeBaja
AS
BEGIN
	SELECT * FROM Medicamento WHERE Estado = 0
END
GO
/*Listado de medicamentos activos*/
CREATE PROCEDURE SP_ListarMedicamentosActivos
AS
BEGIN
	SELECT * FROM Medicamento WHERE Estado = 1
END
GO
/*Agregar Stock*/
CREATE PROCEDURE SP_AgregarStock
(
@MedicamentoId INT,
@StockIngresado INT
)
AS
BEGIN
	IF @StockIngresado < 0
		PRINT 'Ingresa un valor positivo'
	ELSE IF @StockIngresado = 0
		PRINT 'Ingresa una cantidad mayor a 0'
	ELSE
		UPDATE Medicamento SET Stock += @StockIngresado WHERE MedicamentoId = @MedicamentoId
END
GO
/*Reducir Stock*/
CREATE PROCEDURE SP_ReducirStockMedicamento(
@MedicamentoId INT,
@Stock_Descontado INT
)
AS
BEGIN
	DECLARE @Stock_Actual INT
	SELECT @Stock_Actual = Stock FROM Medicamento WHERE MedicamentoId = @MedicamentoId
	IF @Stock_Descontado > @Stock_Actual
		PRINT 'No puede seleccionar una cantidad mayor al stock actual'
	ELSE IF @Stock_Descontado = 0
		PRINT 'Ingrese una cantidad'
	ELSE
		UPDATE Medicamento SET Stock = @Stock_Actual - @Stock_Descontado WHERE MedicamentoId = @MedicamentoId
END
GO