-- Deploy disputeworkflow:create_camunda_decision_engine to pg
-- requires: create_camunda_engine

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

-- create decision definition table --
create table if not exists ACT_RE_DECISION_DEF
(
    ID_                 varchar(64)  NOT NULL,
    REV_                integer,
    CATEGORY_           varchar(255),
    NAME_               varchar(255),
    KEY_                varchar(255) NOT NULL,
    VERSION_            integer      NOT NULL,
    DEPLOYMENT_ID_      varchar(64),
    RESOURCE_NAME_      varchar(4000),
    DGRM_RESOURCE_NAME_ varchar(4000),
    DEC_REQ_ID_         varchar(64),
    DEC_REQ_KEY_        varchar(255),
    TENANT_ID_          varchar(64),
    HISTORY_TTL_        integer,
    VERSION_TAG_        varchar(64),
    primary key (ID_)
);

-- create decision requirements definition table --
create table if not exists ACT_RE_DECISION_REQ_DEF
(
    ID_                 varchar(64)  NOT NULL,
    REV_                integer,
    CATEGORY_           varchar(255),
    NAME_               varchar(255),
    KEY_                varchar(255) NOT NULL,
    VERSION_            integer      NOT NULL,
    DEPLOYMENT_ID_      varchar(64),
    RESOURCE_NAME_      varchar(4000),
    DGRM_RESOURCE_NAME_ varchar(4000),
    TENANT_ID_          varchar(64),
    primary key (ID_)
);

alter table if exists ACT_RE_DECISION_DEF
    add constraint ACT_FK_DEC_REQ
        foreign key (DEC_REQ_ID_)
            references ACT_RE_DECISION_REQ_DEF (ID_);

create index if not exists ACT_IDX_DEC_DEF_TENANT_ID on ACT_RE_DECISION_DEF (TENANT_ID_);
create index if not exists ACT_IDX_DEC_DEF_REQ_ID on ACT_RE_DECISION_DEF (DEC_REQ_ID_);
create index if not exists ACT_IDX_DEC_REQ_DEF_TENANT_ID on ACT_RE_DECISION_REQ_DEF (TENANT_ID_);


-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

COMMIT;
