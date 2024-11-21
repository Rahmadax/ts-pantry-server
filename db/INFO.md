### Camunda Schema Management

Automatic Camunda schema management is disabled for this project.

Camunda's own DB maintenance scripts can be found in the main engine jar (camunda-engine-x.x.x.jar) at the following
path:

```org/camunda/bpm/engine/db/```

The DPDB main version is based on camunda-engine 7.22.0.

When upgrading the engine version, please analyse the migration scripts in the new jar and translate them into
appropriate DPDB scripts.

__NOTE__: while running DB schema migrations, you may see the following errors:
```shell
org.postgresql.util.PSQLException: ERROR: cached plan must not change result type
```
to prevent this error, change `autosave` parameter to `conservative` value in jdbc connection string, i.e. add `autosave=conservative` 
to the end of the connection string defined in `ENV_DB_URL` variable in infra yaml files.
NOTE: please keep INSERT statements into ACT_GE_SCHEMA_LOG table as they would help to keep track which schema version is currently applied. 

### DPDB scripts validation

To validate new scripts locally we can compare dpdb diff output between local and staging environment files for migration run via dpdb and
liquibase cli (used by Camunda project). 

#### Running schema migration using liquibase cli and Camunda's scripts 

Camunda uses liquibase for schema management. There is a way to migrate the schema using liquibase cli documented [here](https://docs.camunda.org/manual/7.22/installation/database-schema/#migrate-to-liquibase).
The files required for migration can be find in [camunda-bpm-platform](https://github.com/camunda/camunda-bpm-platform) repo under `engine/src/main/resources/org/camunda/bpm/engine/db` path.

##### Steps to det diff running migration using Camunda's Liquibase scripts

**1. Setup Liquibase** 

Download [Liquibase CLI](https://www.liquibase.com/download)

**2. Set environment variables**

Set the following environment variables:

```shell
export LIQUIBASE_COMMAND_USERNAME=<your_db_username>
export LIQUIBASE_COMMAND_PASSWORD=<your_db_password>
export LIQUIBASE_COMMAND_URL=jdbc:postgresql://<your_db_url>:5432/disputeworkflow
export LIQUIBASE_COMMAND_DRIVER=org.postgresql.Driver
export LIQUIBASE_SEARCH_PATH=<camunda_bpm_platform_repo_directory>/engine/src/main/resources/org/camunda/bpm/engine/db/
```

**3. Identify your current database schema version**

After running `dbdb deploy` (without new scripts!), connect to local db and run the following query:

```sql
SELECT MAX(CAST(id_ as int)) FROM act_ge_schema_log;
```

**4. Set current schema version using Liquibase**

Run the following command to set the current schema version:

```shell
cp -R <camunda_bpm_platform_repo_directory>/engine/src/main/resources/org/camunda/bpm/engine/db/upgrade <camunda_bpm_platform_repo_directory>/engine/src/main/resources/org/camunda/bpm/engine/db/liquibase 
liquibase tag --tag=7.16.0
liquibase changelog-sync-to-tag --tag=7.16.0 --changelog-file=liquibase/camunda-changelog.xml
```

**4. Run schema migration**

 Delete change sets after required version (check for not released versions) from `amunda-changelog.xml` file before running the following command

```shell
liquibase update --changelog-file=liquibase/camunda-changelog.xml
```

**5. Drop Liquibase tables**

Drop the following command to keep diff file clean

```sql
DROP TABLE IF EXISTS databasechangeloglock;
DROP TABLE IF EXISTS databasechangelog;
```

**5. Get the diff**

```shell
dpdb diff --source local --target staging --file diff_comunda.sql
```
##### Steps to det diff running migration using DPDB

Checkout the branch with new scripts and run the following commands

```shell
dpdb implode -f && dpdb start && dpdb initdb && dpdb deploy
dpdb diff --source local --target staging --file diff_dpdb.sql
```

##### Validate new scripts

make sure the content of `diff_dpdb.sql` and `diff_comunda.sql` files are equal
