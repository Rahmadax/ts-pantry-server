### Camunda Schema Management

Automatic Camunda schema management is disabled for this project.

Camunda's own DB maintenance scripts can be found in the main engine jar (camunda-engine-x.x.x.jar) at the following
path:

```org/camunda/bpm/engine/db/```

The DPDB main version is based on camunda-engine 7.15.0.

When upgrading the engine version, please analyse the migration scripts in the new jar and translate them into
appropriate DPDB scripts.

__NOTE__: while running DB schema migrations, you may see the following errors:
```shell
org.postgresql.util.PSQLException: ERROR: cached plan must not change result type
```
to prevent this error, change `autosave` parameter to `conservative` value in jdbc connection string, i.e. add `autosave=conservative` 
to the end of the connection string defined in `ENV_DB_URL` variable in infra yaml files.
