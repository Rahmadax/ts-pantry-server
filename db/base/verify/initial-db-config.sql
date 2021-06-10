BEGIN;

SELECT 1 / COUNT(*)
FROM pg_extension
WHERE extname = 'plpgsql';

ROLLBACK;
