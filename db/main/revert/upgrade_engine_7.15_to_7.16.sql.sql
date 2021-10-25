-- Revert disputeworkflow:upgrade_engine_7.15_to_7.16.sql from pg

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

delete from public.ACT_GE_SCHEMA_LOG where id_='500' and version_='7.16.0';
drop table if exists ACT_RE_CAMFORMDEF;

-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

COMMIT;
