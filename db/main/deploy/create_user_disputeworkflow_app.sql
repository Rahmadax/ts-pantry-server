-- Deploy disputeworkflow:create_user_disputeworkflow_app to pg

BEGIN;

SET statement_timeout = '15s';
SET lock_timeout = '15s';
SET idle_in_transaction_session_timeout = '15s';
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;
SET search_path = public;
SET default_tablespace = '';
SET default_with_oids = false;

-----------------------------------------------------------------
--                            CHANGE                           --
-----------------------------------------------------------------

DO
$do$
    BEGIN
        IF NOT EXISTS(SELECT FROM pg_catalog.pg_roles WHERE rolname = 'disputeworkflow_app') THEN
            CREATE ROLE disputeworkflow_app LOGIN NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION VALID UNTIL 'infinity' ;
            GRANT disputeworkflow_default_rw TO disputeworkflow_app;
        END IF;
    END
$do$;

-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

COMMIT;
