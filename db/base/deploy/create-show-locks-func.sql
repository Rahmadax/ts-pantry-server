-- Usage:
-- ---------------------------
-- BEGIN;
--   <statements to analyze: ALTER TABLE ...whatever...;>
-- SELECT * FROM show_locks();
-- ROLLBACK;
-- ---------------------------
-- from https://depopmarket.atlassian.net/wiki/spaces/PLAT/pages/573767684/PostgreSQL+Safe+Online+Operations?focusedCommentId=579993637#comment-579993637

CREATE
    OR REPLACE FUNCTION show_locks()
    RETURNS TABLE
            (
                "lock_type" VARCHAR,
                "table"     NAME
            )
AS
$$
    -- DO NOT USE ON PRODUCTION DB!
    -- (this will actually acquire the locks)
    -- use a local copy of the db, preferably empty
SELECT pl.mode,
       pc.relname
FROM pg_locks pl
         LEFT JOIN pg_class pc
                   ON pc.oid = pl.relation
WHERE virtualtransaction = (
    SELECT virtualtransaction
    FROM pg_locks
    WHERE transactionid
              ::text = (txid_current() % (2 ^ 32)::bigint)::text
-- compare int to xid, see https://dba.stackexchange.com/a/123183/10371
      AND locktype = 'transactionid'
    LIMIT 1
)
  AND pc.relnamespace >= 2200
-- 2200 is a magic number to exclude pg_* internal locks
-- see https://stackoverflow.com/a/37110529/202168
ORDER BY (
             -- hack to order results by mode, in the order below
             -- (i.e. 'strongest' lock to weakest)
             -- https://www.postgresql.org/docs/9.3/static/explicit-locking.html#LOCKING-TABLES
             -- see https://stackoverflow.com/a/4088794/202168
          pl."mode" != 'AccessExclusiveLock',
          pl."mode" != 'ExclusiveLock',
          pl."mode" != 'ShareRowExclusiveLock',
          pl."mode" != 'ShareLock',
          pl."mode" != 'ShareUpdateExclusiveLock',
          pl."mode" != 'RowExclusiveLock',
          pl."mode" != 'RowShareLock',
          pl."mode" != 'AccessShareLock',
             --
          relname
             );
$$
    LANGUAGE sql;
