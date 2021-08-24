-- Verify disputeworkflow:add_initial_identity_data on pg

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

SELECT 1/COUNT(*) FROM public.act_id_membership WHERE user_id_='demo' and group_id_='camunda-admin';
SELECT 1/COUNT(*) FROM public.act_id_group WHERE id_='camunda-admin';
SELECT 1/COUNT(*) FROM public.act_id_group WHERE id_='RBAC_DRC_CAMUNDA_ADMIN';
SELECT 1/COUNT(*) FROM public.act_id_user WHERE id_='demo';
SELECT 1/COUNT(*) FROM public.act_ru_authorization WHERE group_id_='camunda-admin';
SELECT 1/COUNT(*) FROM public.act_ru_authorization WHERE group_id_='RBAC_DRC_CAMUNDA_ADMIN';

-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

ROLLBACK;
