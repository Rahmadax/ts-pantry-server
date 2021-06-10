-- Deploy template:create-database to pg

CREATE
    DATABASE disputeworkflow TEMPLATE template1;

GRANT TEMP
    ON DATABASE disputeworkflow TO disputeworkflow_default_rw;
GRANT CONNECT
    ON DATABASE disputeworkflow TO disputeworkflow_default_ro;

\connect disputeworkflow

BEGIN;

GRANT USAGE ON SCHEMA
    public TO disputeworkflow_default_ro;

ALTER
    DEFAULT PRIVILEGES FOR ROLE depopdba GRANT
    SELECT
    ON TABLES TO disputeworkflow_default_ro;
ALTER
    DEFAULT PRIVILEGES FOR ROLE depopdba GRANT INSERT,
    UPDATE,
    DELETE, TRUNCATE
    ON TABLES TO disputeworkflow_default_rw;
ALTER
    DEFAULT PRIVILEGES FOR ROLE depopdba GRANT USAGE,
    SELECT
    ON SEQUENCES TO disputeworkflow_default_ro;
ALTER
    DEFAULT PRIVILEGES FOR ROLE depopdba GRANT
    UPDATE ON SEQUENCES TO disputeworkflow_default_rw;

COMMIT;
