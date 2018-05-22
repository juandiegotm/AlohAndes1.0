OPTIONS (SKIP=1)
LOAD DATA
APPEND INTO TABLE OFERTA
(
idOferta INTEGER EXTERNAL TERMINATED BY ",",
valor INTEGER EXTERNAL TERMINATED BY ",",
duracion CHAR TERMINATED BY ",",
fechaInicio DATE TERMINATED BY "," "to_date(:fechaInicio, 'DD/MM/YYYY')",
fechaFinal DATE TERMINATED BY "," "to_date(:fechaFinal, 'DD/MM/YYYY')",
cantidadDisponible INTEGER EXTERNAL TERMINATED BY ",",
direccion CHAR TERMINATED BY ",",
tipoDeOferta CHAR TERMINATED BY ",",
idOperador INTEGER EXTERNAL TERMINATED BY ",",
cantidadInicial INTEGER EXTERNAL TERMINATED BY ",",
fechaPublicacion DATE TERMINATED BY "," "to_date(:fechaPublicacion, 'DD/MM/YYYY')",
estadoOferta CHAR TERMINATED BY ","
)