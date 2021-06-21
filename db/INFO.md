### Camunda Schema Management

Automatic Camunda schema management is disabled for this project.

Camunda's own DB maintenance scripts can be found in the main engine jar (camunda-engine-x.x.x.jar) at the following
path:

```org/camunda/bpm/engine/db/```

The DPDB main version is based on camunda-engine 7.15.0.

When upgrading the engine version, please analyse the migration scripts in the new jar and translate them into
appropriate DPDB scripts.

