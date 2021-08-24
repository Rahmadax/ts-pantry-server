-- Deploy disputeworkflow:add_initial_identity_data to pg
-- requires: create_camunda_identity

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

INSERT INTO public.act_id_group(id_, rev_, name_, type_) VALUES ('camunda-admin', 1, 'camunda BPM Administrators', 'SYSTEM')
ON CONFLICT DO NOTHING;
INSERT INTO public.act_id_user (id_, rev_, first_, last_, email_, pwd_, salt_, lock_exp_time_, attempts_, picture_id_) VALUES ('demo', 1, 'demo-first-name', 'demo-last-name', 'demo@demo.com', '{SHA-512}fb4xt0wwvCIEoiEVDz2w0kl5veGhFILkj3Uw68WykkdRNi+Cdp87B7Gj+kXtC3KbzfDQ+mvrwEMEq+wvLashXA==', 'FEu8QBOAoPfkiDsk0zpKCw==', NULL, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_id_membership(user_id_, group_id_) VALUES ('demo', 'camunda-admin')
ON CONFLICT DO NOTHING;

INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a44d06d6-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 0, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a450d767-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 1, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a451e8d8-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 2, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a452fa49-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 3, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a45459da-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 4, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a455e07b-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 5, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a457671c-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 6, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a458edbd-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 7, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a45a9b6e-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 8, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a45d819f-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 9, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a45f0840-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 10, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a46040c1-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 11, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a461a052-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 12, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a462d8d3-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 13, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a4645f74-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 14, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a465bf05-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 15, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a4671e96-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 16, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a4683007-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 17, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a4696888-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 18, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a46a79f9-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 19, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a46b645a-00b3-11ec-8747-5ef01066c26b', 1, 1, 'camunda-admin', NULL, 20, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;

--------

INSERT INTO public.act_id_group(id_, rev_, name_, type_) VALUES ('RBAC_DRC_CAMUNDA_ADMIN', 1, 'RBAC_DRC_CAMUNDA_ADMIN', 'SYSTEM')
ON CONFLICT DO NOTHING;

INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a44d06d6-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 0, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a450d767-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 1, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a451e8d8-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 2, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a452fa49-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 3, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a45459da-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 4, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a455e07b-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 5, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a457671c-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 6, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a458edbd-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 7, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a45a9b6e-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 8, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a45d819f-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 9, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a45f0840-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 10, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a46040c1-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 11, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a461a052-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 12, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a462d8d3-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 13, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a4645f74-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 14, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a465bf05-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 15, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a4671e96-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 16, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a4683007-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 17, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a4696888-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 18, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a46a79f9-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 19, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;
INSERT INTO public.act_ru_authorization (id_, rev_, type_, group_id_, user_id_, resource_type_, resource_id_, perms_, removal_time_, root_proc_inst_id_) VALUES ('a46b645a-00b3-11ec-8747-5ef01066c26c', 1, 1, 'RBAC_DRC_CAMUNDA_ADMIN', NULL, 20, '*', 2147483647, NULL, NULL)
ON CONFLICT DO NOTHING;

-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

COMMIT;
