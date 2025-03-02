# Instant Payment API

---

## Overview

This is an Instant Payment API service where transactions can be made from one account to another in a cloud environment

---

## How to install

### Prerequisites

- Docker
- Kubernetes Cluster (e.g. Minikube)
- Helm

### Docker Image

Build the docker image with the *image_build.sh* script found within the scripts folder

> \$ cd scripts<br>
> \$ ./image_build.sh

### Deploy via helm

Once the docker image is built you can deploy the helm chart
with the *helm_deploy* script found within the scripts folder<br>
A prompt will ask for the namespace

> $ ./helm_deploy.sh<br>
> Namespace:<br>
> ENTER NAMESPACE HERE

Once the chart is deployed you can access the api through port-forwarding

> $ kubectl port-forward svc/finance-api 8080:8080 -n NAMESPACE

---

## Features

### Persistence

Transactions and accounts are persisted in a PostgresSQL database,
with accounts keeping track of the balance

### Asynchronous Notifications

Through Kafka asynchronous notifications are sent of every successful transaction to the recipient<br>
The messages can be filtered on the consumers by the 'recipient_id' field in the Header

### Error Handling

Every error that can happen inside the application
will return a nice formatted error message as the response body
with clear status codes

### High Availability

Multiple replicas are present of the pods in the cloud,
so if one goes down, another can server the incoming requests in the meantime<br>
Kafka messages have a retry strategy which retries sending messages in case of an error

### Data Integrity

Every database interaction is handled within transactions, to avoid persisting incomplete data<br>
Updating transactions are managed with pessimistic locks to avoid concurrency issues like double spending<br>
Kafka messages are sent with an idempotent producer, ensuring EOS semantics, meaning every notification will only be sent once

### Containerization

A dockerfile is provided to be able to run the service in a docker container<br>
A helm chart is also provided for easy installation and management in a Kubernetes cluster

---

## Open API Documentation

You can find the Open API documentation in the *src/main/resources* folder *api-docs.yaml*<br>
Every endpoint is manually documented with the schemas and possible responses

## Technologies Used

- **Java:** Chosen language of the service. JDK version: 21
- **Spring Boot:** Main framework of the service. Version: 3.4.3
- **PostgresSQL:** Database for handling persistence of accounts and transactions.
- **Kafka:** Asynchronous notification handling
- **Docker:** Engine for running the application in a container
- **Kubernetes:** Orchestration platform for cloud deployment
- **Helm:** Tool for managing charts holding Kubernetes objects and configurations