OPTIONS (SKIP=1)
LOAD DATA
APPEND INTO TABLE PERSONAHABILITADA
(
idPersonaHabilitada INTEGER EXTERNAL TERMINATED BY ",",
nombre CHAR TERMINATED BY ",",
telefono INTEGER EXTERNAL TERMINATED BY ",",
email CHAR TERMINATED BY ","
)