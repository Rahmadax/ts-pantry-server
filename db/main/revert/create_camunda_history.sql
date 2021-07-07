-- Revert disputeworkflow:create_camunda_history from pg

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

drop index if exists ACT_IDX_HI_PRO_INST_END;
drop index if exists ACT_IDX_HI_PRO_I_BUSKEY;
drop index if exists ACT_IDX_HI_PRO_INST_TENANT_ID;
drop index if exists ACT_IDX_HI_PRO_INST_PROC_DEF_KEY;
drop index if exists ACT_IDX_HI_PRO_INST_PROC_TIME;
drop index if exists ACT_IDX_HI_PI_PDEFID_END_TIME;
drop index if exists ACT_IDX_HI_PRO_INST_ROOT_PI;
drop index if exists ACT_IDX_HI_PRO_INST_RM_TIME;

drop index if exists ACT_IDX_HI_ACTINST_ROOT_PI;
drop index if exists ACT_IDX_HI_ACT_INST_START_END;
drop index if exists ACT_IDX_HI_ACT_INST_END;
drop index if exists ACT_IDX_HI_ACT_INST_PROCINST;
drop index if exists ACT_IDX_HI_ACT_INST_COMP;
drop index if exists ACT_IDX_HI_ACT_INST_STATS;
drop index if exists ACT_IDX_HI_ACT_INST_TENANT_ID;
drop index if exists ACT_IDX_HI_ACT_INST_PROC_DEF_KEY;
drop index if exists ACT_IDX_HI_AI_PDEFID_END_TIME;
drop index if exists ACT_IDX_HI_ACT_INST_RM_TIME;

drop index if exists ACT_IDX_HI_TASKINST_ROOT_PI;
drop index if exists ACT_IDX_HI_TASK_INST_TENANT_ID;
drop index if exists ACT_IDX_HI_TASK_INST_PROC_DEF_KEY;
drop index if exists ACT_IDX_HI_TASKINST_PROCINST;
drop index if exists ACT_IDX_HI_TASKINSTID_PROCINST;
drop index if exists ACT_IDX_HI_TASK_INST_RM_TIME;
drop index if exists ACT_IDX_HI_TASK_INST_START;
drop index if exists ACT_IDX_HI_TASK_INST_END;

drop index if exists ACT_IDX_HI_DETAIL_ROOT_PI;
drop index if exists ACT_IDX_HI_DETAIL_PROC_INST;
drop index if exists ACT_IDX_HI_DETAIL_ACT_INST;
drop index if exists ACT_IDX_HI_DETAIL_CASE_INST;
drop index if exists ACT_IDX_HI_DETAIL_CASE_EXEC;
drop index if exists ACT_IDX_HI_DETAIL_TIME;
drop index if exists ACT_IDX_HI_DETAIL_NAME;
drop index if exists ACT_IDX_HI_DETAIL_TASK_ID;
drop index if exists ACT_IDX_HI_DETAIL_TENANT_ID;
drop index if exists ACT_IDX_HI_DETAIL_PROC_DEF_KEY;
drop index if exists ACT_IDX_HI_DETAIL_BYTEAR;
drop index if exists ACT_IDX_HI_DETAIL_RM_TIME;
drop index if exists ACT_IDX_HI_DETAIL_TASK_BYTEAR;
drop index if exists ACT_IDX_HI_DETAIL_VAR_INST_ID;

drop index if exists ACT_IDX_HI_IDENT_LNK_ROOT_PI;
drop index if exists ACT_IDX_HI_IDENT_LNK_USER;
drop index if exists ACT_IDX_HI_IDENT_LNK_GROUP;
drop index if exists ACT_IDX_HI_IDENT_LNK_TENANT_ID;
drop index if exists ACT_IDX_HI_IDENT_LNK_PROC_DEF_KEY;
drop index if exists ACT_IDX_HI_IDENT_LINK_TASK;
drop index if exists ACT_IDX_HI_IDENT_LINK_RM_TIME;
drop index if exists ACT_IDX_HI_IDENT_LNK_TIMESTAMP;

drop index if exists ACT_IDX_HI_VARINST_ROOT_PI;
drop index if exists ACT_IDX_HI_PROCVAR_PROC_INST;
drop index if exists ACT_IDX_HI_PROCVAR_NAME_TYPE;
drop index if exists ACT_IDX_HI_CASEVAR_CASE_INST;
drop index if exists ACT_IDX_HI_VAR_INST_TENANT_ID;
drop index if exists ACT_IDX_HI_VAR_INST_PROC_DEF_KEY;
drop index if exists ACT_IDX_HI_VARINST_BYTEAR;
drop index if exists ACT_IDX_HI_VARINST_RM_TIME;
drop index if exists ACT_IDX_HI_VAR_PI_NAME_TYPE;

drop index if exists ACT_IDX_HI_INCIDENT_TENANT_ID;
drop index if exists ACT_IDX_HI_INCIDENT_PROC_DEF_KEY;
drop index if exists ACT_IDX_HI_INCIDENT_ROOT_PI;
drop index if exists ACT_IDX_HI_INCIDENT_PROCINST;
drop index if exists ACT_IDX_HI_INCIDENT_RM_TIME;
drop index if exists ACT_IDX_HI_INCIDENT_CREATE_TIME;
drop index if exists ACT_IDX_HI_INCIDENT_END_TIME;

drop index if exists ACT_IDX_HI_JOB_LOG_ROOT_PI;
drop index if exists ACT_IDX_HI_JOB_LOG_PROCINST;
drop index if exists ACT_IDX_HI_JOB_LOG_PROCDEF;
drop index if exists ACT_IDX_HI_JOB_LOG_TENANT_ID;
drop index if exists ACT_IDX_HI_JOB_LOG_JOB_DEF_ID;
drop index if exists ACT_IDX_HI_JOB_LOG_PROC_DEF_KEY;
drop index if exists ACT_IDX_HI_JOB_LOG_EX_STACK;
drop index if exists ACT_IDX_HI_JOB_LOG_RM_TIME;
drop index if exists ACT_IDX_HI_JOB_LOG_JOB_CONF;

drop index if exists ACT_HI_EXT_TASK_LOG_ROOT_PI;
drop index if exists ACT_HI_EXT_TASK_LOG_PROCINST;
drop index if exists ACT_HI_EXT_TASK_LOG_PROCDEF;
drop index if exists ACT_HI_EXT_TASK_LOG_PROC_DEF_KEY;
drop index if exists ACT_HI_EXT_TASK_LOG_TENANT_ID;
drop index if exists ACT_IDX_HI_EXTTASKLOG_ERRORDET;
drop index if exists ACT_HI_EXT_TASK_LOG_RM_TIME;

drop index if exists ACT_HI_BAT_RM_TIME;

drop index if exists ACT_IDX_HI_OP_LOG_ROOT_PI;
drop index if exists ACT_IDX_HI_OP_LOG_PROCINST;
drop index if exists ACT_IDX_HI_OP_LOG_PROCDEF;
drop index if exists ACT_IDX_HI_OP_LOG_TASK;
drop index if exists ACT_IDX_HI_OP_LOG_RM_TIME;
drop index if exists ACT_IDX_HI_OP_LOG_TIMESTAMP;
drop index if exists ACT_IDX_HI_OP_LOG_USER_ID;
drop index if exists ACT_IDX_HI_OP_LOG_OP_TYPE;
drop index if exists ACT_IDX_HI_OP_LOG_ENTITY_TYPE;

drop index if exists ACT_IDX_HI_ATTACHMENT_CONTENT;
drop index if exists ACT_IDX_HI_ATTACHMENT_ROOT_PI;
drop index if exists ACT_IDX_HI_ATTACHMENT_PROCINST;
drop index if exists ACT_IDX_HI_ATTACHMENT_TASK;
drop index if exists ACT_IDX_HI_ATTACHMENT_RM_TIME;

drop index if exists ACT_IDX_HI_COMMENT_TASK;
drop index if exists ACT_IDX_HI_COMMENT_ROOT_PI;
drop index if exists ACT_IDX_HI_COMMENT_PROCINST;
drop index if exists ACT_IDX_HI_COMMENT_RM_TIME;

drop table if exists ACT_HI_PROCINST;
drop table if exists ACT_HI_ACTINST;
drop table if exists ACT_HI_VARINST;
drop table if exists ACT_HI_TASKINST;
drop table if exists ACT_HI_DETAIL;
drop table if exists ACT_HI_COMMENT;
drop table if exists ACT_HI_ATTACHMENT;
drop table if exists ACT_HI_OP_LOG;
drop table if exists ACT_HI_INCIDENT;
drop table if exists ACT_HI_JOB_LOG;
drop table if exists ACT_HI_BATCH;
drop table if exists ACT_HI_IDENTITYLINK;
drop table if exists ACT_HI_EXT_TASK_LOG;

-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

COMMIT;
