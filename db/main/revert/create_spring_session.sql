-- Revert disputeworkflow:create_spring_session from pg

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

drop index if exists SPRING_SESSION_IX3;
drop index if exists SPRING_SESSION_IX2;
drop index if exists SPRING_SESSION_IX1;

alter table if exists SPRING_SESSION_ATTRIBUTES
    drop constraint SPRING_SESSION_ATTRIBUTES_PK;

alter table if exists SPRING_SESSION_ATTRIBUTES
    drop constraint SPRING_SESSION_ATTRIBUTES_FK;

alter table if exists SPRING_SESSION
    drop constraint SPRING_SESSION_PK;

drop table if exists SPRING_SESSION_ATTRIBUTES;
drop table if exists SPRING_SESSION;

-----------------------------------------------------------------
--                             END                             --
-----------------------------------------------------------------

COMMIT;
