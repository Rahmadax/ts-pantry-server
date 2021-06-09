# Dispute Resolution Centre - Process Engine

This is a Java (jdk 16) and Spring Boot (2.4.5) project built using gradle (v7).

The following build profiles are available:

| Profile        | Boot Profiles                   | Description |
| -----------    | -----------                     | ----------- |
| default        | -                               | Defaults to dev-h2
| dev-h2       | metrics, tracing, h2, dev         | Runs with a local, embedded, in-memory database
| dev-postgres | metrics, tracing, postgres, dev   | Runs with against an external, postgres database

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

Please add your credentials to your global gradle properties file `~/.gradle/gradle.properties`

```properties
depopJFrogUsername=<username>
depopJFrogPassword=<api-key>
```

These can also be set via environment variables.

```shell
> export ORG_GRADLE_PROJECT_depopJFrogUsername=<username>
> export ORG_GRADLE_PROJECT_depopJFrogPassword=<api-key>
```

For more information see here:

https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties

For more information on getting an API key please see here:

https://depopmarket.atlassian.net/wiki/spaces/BD/pages/1298530340/Setup+artifactory+access+locally

### Local Postgres Database

To create a local postgres instance please run the following:

```shell
> docker-compose -f docker/dev/docker-compose.dev.yaml up
```

This requires AWS authentication support in docker.  
Please install and configure the depop and AWS CLI and ensure that you are able to assume the developer IAM role:

https://infra.docs.depop.com/internal/aws/federated-auth/

The following environment variables should be set for access to the depop ECR:

```properties
AWS_DEFAULT_PROFILE=developer@auth-default
AWS_DEFAULT_REGION=us-east-1
AWS_SDK_LOAD_CONFIG=1
AWS_ACCESS_KEY_ID=<key-id>
AWS_SECRET_ACCESS_KEY=<key>
AWS_SESSION_TOKEN=<token>
```

Please also install the Amazon ECR Docker Credential Helper:

https://github.com/awslabs/amazon-ecr-credential-helper

### Datadog Credentials

When running locally, exporting of metrics to datadog is disabled by default.

Export can be enabled by adding an api and application key to your local development application properties:

`<project>/dispute-workflow/src/main/resources/application-dev.yaml`

This file should not be committed to the vcs and is included in the git ignore file.

```yaml
management:
  metrics:
    export:
      datadog:
        enabled: true
        api-key: <api-key>
        application-key: <application-key>
```

## How To Contribute

TODO
