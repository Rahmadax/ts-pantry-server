-- Verify disputeworkflow:upgrade_engine_7.15_to_7.16.sql on pg

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

SELECT 1 / COUNT(*)
FROM ACT_GE_SCHEMA_LOG
WHERE id_ = '500'
  and version_ = '7.16.0';

SELECT *
FROM ACT_RE_CAMFORMDEF
WHERE false;

INSERT INTO ACT_RE_CAMFORMDEF (id_,
                               rev_,
                               key_,
                               version_,
                               deployment_id_,
                               resource_name_,
                               tenant_id_)
VALUES ('6a1af85f-038c-4204-a6b8-8f687b51867f',
        123,
        '6a1af85f-038c-4204-a6b8-8f687b51867f',
        123,
        '6a1af85f-038c-4204-a6b8-8f687b51867f',
        '6a1af85f-038c-4204-a6b8-8f687b51867f',
        '6a1af85f-038c-4204-a6b8-8f687b51867f');

SELECT 1 / COUNT(*)
FROM ACT_RE_CAMFORMDEF
WHERE id_ = '6a1af85f-038c-4204-a6b8-8f687b51867f';

-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

ROLLBACK;
