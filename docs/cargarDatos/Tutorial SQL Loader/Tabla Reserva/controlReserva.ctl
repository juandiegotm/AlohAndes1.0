OPTIONS (SKIP=1)
LOAD DATA
APPEND INTO TABLE RESERVA
(
idOferta INTEGER EXTERNAL TERMINATED BY ",",
idPersonaHabilitada INTEGER EXTERNAL TERMINATED BY ",",
fechaInicioReserva DATE TERMINATED BY "," "to_date(:fechaInicioReserva, 'DD/MM/YYYY')",
estadoReserva CHAR TERMINATED BY ",",
idReserva INTEGER EXTERNAL TERMINATED BY ",",
fechaFinalReserva DATE TERMINATED BY "," "to_date(:fechaFinalReserva, 'DD/MM/YYYY')",
cantidad INTEGER EXTERNAL TERMINATED BY ","
)