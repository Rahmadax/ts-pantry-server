-- Deploy disputeworkflow:create_camunda_identity to pg
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

create table if not exists ACT_ID_GROUP
(
    ID_   varchar(64),
    REV_  integer,
    NAME_ varchar(255),
    TYPE_ varchar(255),
    primary key (ID_)
);

create table if not exists ACT_ID_MEMBERSHIP
(
    USER_ID_  varchar(64),
    GROUP_ID_ varchar(64),
    primary key (USER_ID_, GROUP_ID_)
);

create table if not exists ACT_ID_USER
(
    ID_            varchar(64),
    REV_           integer,
    FIRST_         varchar(255),
    LAST_          varchar(255),
    EMAIL_         varchar(255),
    PWD_           varchar(255),
    SALT_          varchar(255),
    LOCK_EXP_TIME_ timestamp,
    ATTEMPTS_      integer,
    PICTURE_ID_    varchar(64),
    primary key (ID_)
);

create table if not exists ACT_ID_INFO
(
    ID_        varchar(64),
    REV_       integer,
    USER_ID_   varchar(64),
    TYPE_      varchar(64),
    KEY_       varchar(255),
    VALUE_     varchar(255),
    PASSWORD_  bytea,
    PARENT_ID_ varchar(255),
    primary key (ID_)
);

create table if not exists ACT_ID_TENANT
(
    ID_   varchar(64),
    REV_  integer,
    NAME_ varchar(255),
    primary key (ID_)
);

create table if not exists ACT_ID_TENANT_MEMBER
(
    ID_        varchar(64) not null,
    TENANT_ID_ varchar(64) not null,
    USER_ID_   varchar(64),
    GROUP_ID_  varchar(64),
    primary key (ID_)
);

create index if not exists ACT_IDX_MEMB_GROUP on ACT_ID_MEMBERSHIP (GROUP_ID_);
alter table if exists ACT_ID_MEMBERSHIP
    add constraint ACT_FK_MEMB_GROUP
        foreign key (GROUP_ID_)
            references ACT_ID_GROUP (ID_);

create index if not exists ACT_IDX_MEMB_USER on ACT_ID_MEMBERSHIP (USER_ID_);
alter table if exists ACT_ID_MEMBERSHIP
    add constraint ACT_FK_MEMB_USER
        foreign key (USER_ID_)
            references ACT_ID_USER (ID_);

alter table if exists ACT_ID_TENANT_MEMBER
    add constraint ACT_UNIQ_TENANT_MEMB_USER
        unique (TENANT_ID_, USER_ID_);

alter table if exists ACT_ID_TENANT_MEMBER
    add constraint ACT_UNIQ_TENANT_MEMB_GROUP
        unique (TENANT_ID_, GROUP_ID_);

create index if not exists ACT_IDX_TENANT_MEMB on ACT_ID_TENANT_MEMBER (TENANT_ID_);
alter table if exists ACT_ID_TENANT_MEMBER
    add constraint ACT_FK_TENANT_MEMB
        foreign key (TENANT_ID_)
            references ACT_ID_TENANT (ID_);

create index if not exists ACT_IDX_TENANT_MEMB_USER on ACT_ID_TENANT_MEMBER (USER_ID_);
alter table if exists ACT_ID_TENANT_MEMBER
    add constraint ACT_FK_TENANT_MEMB_USER
        foreign key (USER_ID_)
            references ACT_ID_USER (ID_);

create index if not exists ACT_IDX_TENANT_MEMB_GROUP on ACT_ID_TENANT_MEMBER (GROUP_ID_);
alter table if exists ACT_ID_TENANT_MEMBER
    add constraint ACT_FK_TENANT_MEMB_GROUP
        foreign key (GROUP_ID_)
            references ACT_ID_GROUP (ID_);


-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

COMMIT;
