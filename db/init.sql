CREATE user deposito with password 'deposito';
DROP database if exists deposito_db;
CREATE DATABASE deposito_db;
GRANT ALL PRIVILEGES ON DATABASE deposito_db TO deposito;
