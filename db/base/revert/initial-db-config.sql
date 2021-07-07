\connect disputeworkflow

BEGIN;
-- Don't rollback everything as somethings are just explicit postgres defaults

SET
    statement_timeout = 0;
SET
    lock_timeout = 0;
SET
    idle_in_transaction_session_timeout = 0;

COMMIT;
