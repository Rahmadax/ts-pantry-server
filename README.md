# Dispute Resolution Centre - Process Engine

This is a Java (jdk 11, kotlin 1.4.31) and Spring Boot (2.5.0) project built using gradle (v7.1).

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

Please add your api key to your global gradle properties file `~/.gradle/gradle.properties`

```properties
jfrog_api_key=<api-key>
```

The key can also be set via an environment variable.

```shell
> export JFROG_API_KEY=<api-key>
```

For more information on getting an API key please see here:

https://depopmarket.atlassian.net/wiki/spaces/BD/pages/1298530340/Setup+artifactory+access+locally

### Local Postgres Database

To create a local postgres instance please use DPDB or run the following:

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
