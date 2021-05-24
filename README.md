# Dispute Resolution Centre - Process Engine

## Getting Started - Developing Locally

This is a Java (jdk 16) and Spring Boot (2.4.5) project built using gradle (v7).

The following build profiles are available:

| Profile        | Boot Profiles            | Description |
| -----------    | -----------              | ----------- |
| default        | local, h2, datadog       | Defaults to local-h2
| local-h2       | local, h2, datadog       | Runs with a local, embedded, in-memory database and exports metrics to datadog
| local-postgres | local, postgres, datadog | Runs with against an external, postgres database and exports metrics to datadog

The spring boot profiles are automatically set when building and running using gradle.

### Build

```shell
> ./gradlew build -PbuildProfile=<profile>
```

### Run

```shell
> ./gradlew bootRun -PbuildProfile=<profile>
```

### Local Postgres Database

To create a local postgres instance please run the following:

```shell
> docker-compose -f docker/local/docker-compose.local.yaml up
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

### JFrog Artifactory Credentials

This project uses the depop JFrog artifactory for dependency resolution.

Please add your credentials to your global gradle properties file `~/.gradle/gradle.properties`

```properties
depopJFrogUsername=<username>
depopJFrogPassword=<api-key>
```

These can also be set via environment variables. For more information see here:

https://docs.gradle.org/current/userguide/build_environment.html#sec:project_properties

For more information on getting an API key please see here:

https://depopmarket.atlassian.net/wiki/spaces/BD/pages/1298530340/Setup+artifactory+access+locally

### Datadog Credentials

_**TODO: Control datadog reporting using a build profile.**_

This project logs metrics to datadog by default.

Please add an api and application key to your local application properties:

`<project>/dispute-workflow/src/main/resources/application-local.yaml`

This file should not be committed to the vcs and is included in the git ignore file.

```yaml
management:
  metrics:
    export:
      datadog:
        api-key: <api-key>
        application-key: <application-key>
```

## How To Contribute

TODO
