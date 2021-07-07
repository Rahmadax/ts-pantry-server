-- Verify disputeworkflow:create_user_disputeworkflow_app on pg

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
FROM pg_roles
WHERE rolname = 'disputeworkflow_app';

-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

ROLLBACK;
