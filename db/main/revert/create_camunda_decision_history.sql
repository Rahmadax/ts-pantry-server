-- Revert disputeworkflow:create_camunda_decision_history from pg

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

--
-- Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
-- under one or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information regarding copyright
-- ownership. Camunda licenses this file to you under the Apache License,
-- Version 2.0; you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

drop index if exists ACT_IDX_HI_DEC_INST_ID;
drop index if exists ACT_IDX_HI_DEC_INST_KEY;
drop index if exists ACT_IDX_HI_DEC_INST_PI;
drop index if exists ACT_IDX_HI_DEC_INST_CI;
drop index if exists ACT_IDX_HI_DEC_INST_ACT;
drop index if exists ACT_IDX_HI_DEC_INST_ACT_INST;
drop index if exists ACT_IDX_HI_DEC_INST_TIME;
drop index if exists ACT_IDX_HI_DEC_INST_TENANT_ID;
drop index if exists ACT_IDX_HI_DEC_INST_ROOT_ID;
drop index if exists ACT_IDX_HI_DEC_INST_REQ_ID;
drop index if exists ACT_IDX_HI_DEC_INST_REQ_KEY;
drop index if exists ACT_IDX_HI_DEC_INST_ROOT_PI;
drop index if exists ACT_IDX_HI_DEC_INST_RM_TIME;

drop index if exists ACT_IDX_HI_DEC_IN_INST;
drop index if exists ACT_IDX_HI_DEC_IN_CLAUSE;
drop index if exists ACT_IDX_HI_DEC_IN_ROOT_PI;
drop index if exists ACT_IDX_HI_DEC_IN_RM_TIME;

drop index if exists ACT_IDX_HI_DEC_OUT_INST;
drop index if exists ACT_IDX_HI_DEC_OUT_RULE;
drop index if exists ACT_IDX_HI_DEC_OUT_ROOT_PI;
drop index if exists ACT_IDX_HI_DEC_OUT_RM_TIME;

drop table if exists ACT_HI_DECINST;

drop table if exists ACT_HI_DEC_IN;

drop table if exists ACT_HI_DEC_OUT;


-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

COMMIT;
