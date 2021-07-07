-- Verify template:create-database on pg

BEGIN;

-- validate database exists and has the correct owner
SELECT 1 / COUNT(*)
FROM (
         SELECT d.datname, u.usename
         FROM pg_catalog.pg_database d,
              pg_catalog.pg_user u
         WHERE d.datname = 'disputeworkflow'
           AND u.usesysid = d.datdba
           AND u.usename = 'depopdba'
     ) verify_db_owner;

ROLLBACK;
