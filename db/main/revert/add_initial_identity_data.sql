-- Revert disputeworkflow:add_initial_identity_data from pg

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
DELETE FROM public.act_id_membership WHERE user_id_='demo' and group_id_='camunda-admin';
DELETE FROM public.act_id_group WHERE id_='camunda-admin';
DELETE FROM public.act_id_group WHERE id_='RBAC_DRC_CAMUNDA_ADMIN';
DELETE FROM public.act_id_user WHERE id_='demo';
DELETE FROM public.act_ru_authorization WHERE group_id_='camunda-admin';
DELETE FROM public.act_ru_authorization WHERE group_id_='RBAC_DRC_CAMUNDA_ADMIN';

-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

COMMIT;
