# Dispute Resolution Centre - Process Engine

This is a Java (jdk 11, kotlin 1.5.31) and Spring Boot (2.5.5) project built using gradle (v7.2).

The following build profiles are available:

| Profile        | Boot Profiles                       | Description |
| -----------    | -----------                         | ----------- |
| default        | -                                   | Defaults to local-h2
| local-h2       | metrics, tracing, logging, h2, local         | Runs with a local, embedded, in-memory database
| local-postgres | metrics, tracing, logging, postgres, local   | Runs with against an external, postgres database
| ci             | metrics, tracing, logging          | use infra config files to set appropriate environment boot profile (staging_deployemnt/production_deployment)

The spring boot profiles are automatically set when building and running using gradle.

# Getting Started

## Running Locally

### 0. Request Okta Playground acccess

Ask in [#it-support](https://etsy.enterprise.slack.com/archives/CHGUTD9PV) to be granted access to DRC Camunda in the Okta Preview environment. You will need to be a member of the `rbac_drc_camunda_admin` group.

### 1. Create a local profile

In `dispute-workflow/src/main/resource/application-local.yaml`:

```yaml
depop:
  drc:
    client:
      shipping-api-secret: xxx

api:
  security:
    username: drc_admin
    password: drc_admin

spring:
  h2:
    console:
      enabled: true
  security:
    oauth2:
      client:
        registration:
          okta:
            client-id: xxx
            client-secret: xxx
          depop:
            client-id: xxx
            client-secret: xxx

management:
  datadog:
    metrics:
      export:
        enabled: false
        api-key: <api-key>
        application-key: <application-key>
  metrics:
    enable:
      all: true

  endpoints:
    web:
      exposure:
        include: "*"
    enabled-by-default: true

```

### 2. Replace secret values

The local profile above has a few placeholders for secrets. Replace them with the corresponding staging environment values. Most of these are available at 
`staging/services/user/default/dispute/workflow/_default` in Vault. Ask a team mate if you're missing any.

🛑 It's wise to run `git status` at this point, to check your local profile with staging values has been safely restricted by `.gitignore`

### 3. Provision local Postgres

```bash
dpdb pgpass -t local
dpdb start
dpdb initdb
dpdb deploy
```

### 4. Run the service

```bash
./gradlew bootRun -PbuildProfile=local-postgres
```

### 5. Access Camunda

Camunda is available at [http://localhost:9000/camunda](http://localhost:9000/camunda).

## Building Locally

### Build

```shell
> ./gradlew build -PbuildProfile=<profile>
```


### JFrog Artifactory Credentials

This project uses the depop JFrog artifactory for dependency resolution.

### Local Postgres Database

The default local development profile will use an in memory h2 instance by default. To create a local postgres instance please use DPDB.
