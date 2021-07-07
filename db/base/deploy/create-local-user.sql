DO
$do$
    BEGIN
        IF NOT EXISTS(SELECT FROM pg_catalog.pg_roles WHERE rolname = 'disputeworkflow') THEN
            CREATE ROLE disputeworkflow LOGIN NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION VALID UNTIL 'infinity' PASSWORD 'disputeworkflow';
            GRANT disputeworkflow_default_rw TO disputeworkflow;
        END IF;
    END
$do$;
