-- Verify template:create-default-roles on pg

BEGIN;

-- Verify role inheritance for depopdba
do
$$
    BEGIN
        ASSERT
            3 = COUNT(*) FROM
    pg_catalog.pg_roles
    WHERE pg_has_role('depopdba', oid, 'member')
      AND rolname IN (
        'depopdba',
        'disputeworkflow_default_rw',
        'disputeworkflow_default_ro'),
    'Did not find all 4 inherited roles';
    END;
$$;

-- Verify role inheritance for disputeworkflow_default_rw
do
$$
    BEGIN
        ASSERT
            2 = COUNT(*) FROM
    pg_catalog.pg_roles
    WHERE pg_has_role('disputeworkflow_default_rw', oid, 'member')
      AND rolname IN (
        'disputeworkflow_default_rw',
        'disputeworkflow_default_ro'),
    'Did not find all 2 inherited roles';
    END;
$$;

-- Verify role inheritance for disputeworkflow_default_ro
do
$$
    BEGIN
        ASSERT
            1 = COUNT(*) FROM
    pg_catalog.pg_roles
    WHERE pg_has_role('disputeworkflow_default_ro', oid, 'member')
      AND rolname IN (
        'disputeworkflow_default_ro'),
    'Did not find all 1 inherited roles';
    END;
$$;

ROLLBACK;
