# Runbook for user-default-dispute-workflow

## Service Overview

Workflow engine service for providing dispute workflow to users of dispute resolution center.

## Service Details

| Metadata            | Value                                                                                |
|---------------------|--------------------------------------------------------------------------------------|
| Service Level       | 4                                                                                    |
| Hostname Production | https://disputes.depop.systems                                                        |
| Hostname Staging    | https://disputes-stage.depop.systems                                                  |
| Cosmos              | [Dashboard](https://cosmos.depop.com/service/user-default-dispute-workflow)          |
| API Documentation   | [Camunda REST API](https://docs.camunda.org/manual/7.5/reference/rest/)              |
| Owning Team         | CX                                                                                   |
| User Agent          | Depop-Service user-default-dispute-workflow                                          |
| RabbitMQ            | No                                                                                   |
| Kafka               | No                                                                                   |
| Camunda Cockpit     | [Camunda Cockpit](https://user-default-dispute-cms.eks-default.stage-svcs.dpop.co.uk)|

### Observability

| Output    | Location                                                                                                                                                                                                                                                                                        |
|-----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Dashboard | [Production Default Metrics](https://app.datadoghq.com/dashboard/suq-hpc-4ec/production-production-user-default-dispute-workflow-default-metrics-stdv1) & [Staging Default Metrics](https://app.datadoghq.com/dashboard/nks-cr5-cx8/staging-staging-user-default-dispute-workflow-default-metrics-stdv1)  |
| Logs      | [Production](https://app.datadoghq.com/logs?query=service%3Auser-default-dispute-workflow%20env_type%3Aproduction) & [Staging](https://app.datadoghq.com/logs?query=service%3Auser-default-dispute-workflow%20env_type%3Astaging)                                                                         |
| Traces    | [Production APM](https://app.datadoghq.com/apm/traces?live=true&query=env%3Aproduction-production%20service%3Auser-default-dispute-workflow) & [Staging APM](https://app.datadoghq.com/apm/traces?live=true&query=env%3Astaging-staging%20service%3Auser-default-dispute-workflow)                        |
| PagerDuty | TODO                                                                                                                                                                                                                                                                                            |
| SLOs      | TODO                                                                                                                                                                                                                                                                                            |

### Dependencies

| Incoming Dependencies                     | Service Level | Description                              |
|-------------------------------------------|---------------|------------------------------------------|
| Dispute Service (Internal)                | 4             | Disputes Service API                     |
| Disputes Processing                       | 4             | Background task processing               |


---

| Outgoing Dependencies                     | Service Level | Description                              |
|-------------------------------------------|---------------|------------------------------------------|
| Dispute Service (Internal)                | 4             | Disputes Service API                     |
| Checkout API                              | 1             | For fetching receipt info                |
| Payments API                              | 1             | For fetching payment info                |
| Shipping API                              | 1             | For fetching shipping info               |
| Postgres                                  | 0             | Storage layer                            |


## Deployment
Recent deployments: https://cosmos.depop.com/service/user-default-dispute-workflow

Deploy through Jenkins: [Classic](https://jenkins-master.dflt-ops.dpop.co.uk/job/depop/job/dispute-workflow/) & [Blue](https://jenkins-master.dflt-ops.dpop.co.uk/blue/organizations/jenkins/depop%2Fdispute-workflow/activity/)

## Technical Debt
Follow Jira [filter](https://depopmarket.atlassian.net/browse/CXPROD-728?jql=project%20%3D%20CXPROD%20AND%20labels%20%3D%20tech_debt%20AND%20text%20~%20%22workflow%22%20order%20by%20created%20DESC)

## Support Documentation
[Go-Live Support Document](https://depopmarket.atlassian.net/wiki/spaces/CPT/pages/3435036787/DRC+Go-Live+Support)
