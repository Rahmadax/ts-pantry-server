-- Revert disputeworkflow:upgrade_engine_7.17_to_7.18.sql to pg

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



ALTER TABLE ACT_HI_BATCH DROP COLUMN EXEC_START_TIME_;

ALTER TABLE ACT_RU_BATCH DROP COLUMN EXEC_START_TIME_;

ALTER TABLE ACT_RU_BATCH DROP COLUMN START_TIME_;

ALTER TABLE ACT_RU_TASK DROP COLUMN LAST_UPDATED_;

delete from public.ACT_GE_SCHEMA_LOG where id_='700' and version_='7.18.0';

-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

COMMIT;