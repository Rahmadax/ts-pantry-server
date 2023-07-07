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

## Getting Started - Developing Locally

### Build

```shell
> ./gradlew build -PbuildProfile=<profile>
```

### Run

```shell
> ./gradlew bootRun -PbuildProfile=<profile>
```

### JFrog Artifactory Credentials

This project uses the depop JFrog artifactory for dependency resolution.

### Local Postgres Database

The default local development profile will use an in memory h2 instance by default. To create a local postgres instance please use DPDB.

