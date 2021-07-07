-- Deploy template:create-default-roles to pg

REVOKE ALL ON SCHEMA public FROM public;
CREATE ROLE disputeworkflow_default_rw NOLOGIN NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION VALID UNTIL 'infinity';
CREATE ROLE disputeworkflow_default_ro NOLOGIN NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION VALID UNTIL 'infinity';
GRANT disputeworkflow_default_ro TO disputeworkflow_default_rw;
GRANT disputeworkflow_default_rw TO depopdba;

ALTER ROLE disputeworkflow_default_rw SET statement_timeout = '15s';
ALTER ROLE disputeworkflow_default_rw SET lock_timeout = '15s';

ALTER ROLE disputeworkflow_default_ro SET statement_timeout = '15s';
ALTER ROLE disputeworkflow_default_ro SET lock_timeout = '15s';
