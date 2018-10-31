# Purchase Orders Example (WIP)

[![Build Status](https://travis-ci.org/pacphi/purchase-orders-example.svg)](https://travis-ci.org/pacphi/purchase-orders-example) [![Known Vulnerabilities](https://snyk.io/test/github/pacphi/purchase-orders-example/badge.svg)](https://snyk.io/test/github/pacphi/purchase-orders-example)

This project explores design of a modern web application which includes: model, API and UI modules.  Also a study in how one might interact with  legacy and modern Oracle databases.


## Prerequisites

* An account with [Space Developer role](https://docs.cloudfoundry.org/concepts/roles.html#roles) access on a Cloud Foundry foundation, e.g., [Pivotal Web Services](https://run.pivotal.io)
* An account on [AWS](https://portal.aws.amazon.com/billing/signup#/start)
* [aws-cli](https://aws.amazon.com/cli/) 1.16.40 or better
* [CF CLI](https://github.com/cloudfoundry/cli#downloads) 6.40.0 or better if you want to push the application to a Cloud Foundry (CF) instance
* [httpie](https://httpie.org/#installation) 0.9.9 or better to simplify interaction with API endpoints
* [jq](https://stedolan.github.io/jq/) 1.5 or better
* Java [JDK](https://openjdk.java.net/install/) 1.8u181 or better to compile and run the code
* [Gradle](https://gradle.org/releases/) 4.10.2 or better to build and package source code
* Docker for [Mac](https://store.docker.com/editions/community/docker-ce-desktop-mac) or [Windows](https://store.docker.com/editions/community/docker-ce-desktop-windows) 
* [DbVisualizer](https://www.dbvis.com/download/) 10.0.15 or better

## Clone

```
git clone https://github.com/pacphi/purchase-orders-example.git
```


## Build

Build this project with Gradle

(Composed of multiple modules so we need to script this a bit)

```
cd orders-model
gradle clean build
cd ../orders-api
gradle clean build
cd ../orders-ui
gradle clean build
```

## Prepare

#### Docker hosted

Start a database instance

```
docker run --rm -d -p 8080:8080 -p 1521:1521 sath89/oracle-xe-11g
```

> Remove the `--rm` argument above if you do not want to reap the database intance on stop

Connect via DbVisualizer

> Consult the [documentation](http://confluence.dbvis.com/display/UG100/Setting+Up+a+Connection+Manually)

![Screenshot of a successfull configuration and connection attempt to an Oracle 11g XE instance](docs/dbvis-oracle-connx.png)

Stop the database instance

```
docker ps
docker stop {pid}
```

> where `{pid}` is the currently running database container id

#### AWS hosted Oracle

Fire up a browser and [login](https://console.aws.amazon.com/console/home) to the AWS console

Also launch a [Terminal](https://macpaw.com/how-to/use-terminal-on-mac) session, configure your accesskey, secretkey, and region 

```
aws configure

AWS Access Key ID [None]: AKIAIOSFODNN7EXAMPLE
AWS Secret Access Key [None]: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Default region name [None]: us-west-2
Default output format [None]: json
```

> Replace values above with your own.

Visit the AWS console in your browser, navigate to `RDS`, select `Oracle` as the engine, and use the wizard to create a new instance

![Screenshot of how to start creating an RDS Oracle instance](docs/aws-rds-instance-creation.png)

Choose `Oracle Standard Edition Two` and click `Next`

Choose `Dev/Test` for the use case and click `Next`

When spcifying `DB details` choose the `Free tier` and complete the `Settings` dialog then click `Next`

Tune or accept the defaults in `Advanced settings` and click `Create database`

### Database preparation

// TODO Articulate the steps to connect to the AWS hosted database with DbVisualizer. Perhaps share a screenshot and script for prep?


## Run 

To startup the application, execute

### Simple case 

We'll fire up a local Docker instance of Oracle, then start the `orders-api` 

```bash
docker run --rm -d -p 8080:8080 -p 1521:1521 sath89/oracle-xe-11g
cd orders-api
gradle bootRun -Dspring.profiles.active=docker
```

### with Docker Compose

// TODO

### with Kubernetes

// TODO


## How to deploy to Pivotal Application Service

These instructions assume that a Oracle instance is available and was pre-provisioned "off-platform" (e.g., on PKS or AWS RDS Oracle).

### Authenticating

Authenticate to a foundation using the API endpoint. 
> E.g., login to [Pivotal Web Services](https://run.pivotal.io)

```
cf login -a https:// api.run.pivotal.io
```

### Managing secrets

Place secrets in `config/secrets.json`, e.g.,

// TODO Just what is required here?

```
{
	
}
```

We'll use this file later as input configuration for the creation of either a [credhub](https://docs.pivotal.io/credhub-service-broker/using.html) or [user-provided](https://docs.cloudfoundry.org/devguide/services/user-provided.html#credentials) service instance.

> Replace occurrences of `xxxxx` above with appropriate values

### Deploy and shutdown

Deploy the app (w/ a user-provided service instance vending secrets)

```
./deploy.sh
```

Deploy the app (w/ a Credhub service instance vending secrets)

```
./deploy.sh --with-credhub
```

Shutdown and destroy the app and service instances

```
./destroy.sh
```


## Endpoints

Current implementation supports

// TODO

## Credits and further reading

A curated list of links to material from which you may draw insight, inspiration and wisdom:

* [Gradle Dependencies for Java, use compile or implmentation?](https://www.javacodegeeks.com/2018/03/gradle-dependencies-java-use-compile-implementation.html)
* [Gradle's Java Library Plugin Documentation](https://docs.gradle.org/current/userguide/java_library_plugin.html)
* [Gradle's Test API Documentation](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html)
* [Gradle's Logging Documentation](https://docs.gradle.org/current/userguide/logging.html)
* [Devops Java Workshop](https://github.com/Pivotal-Field-Engineering/devops-workshop)
* [Migrating from JUnit 4 to JUnit 5](https://www.baeldung.com/junit-5-migration)
* [Junit 5 Assertions Javadoc](https://junit.org/junit5/docs/5.0.1/api/org/junit/jupiter/api/Assertions.html)
* [JUnit 5 Jupiter Starter Gradle Sample](https://github.com/junit-team/junit5-samples/tree/master/junit5-jupiter-starter-gradle)
* [Validating Form Input](https://spring.io/guides/gs/validating-form-input/)
* [Spring Data JDBC - Reference Documentation](https://docs.spring.io/spring-data/jdbc/docs/1.0.0.RELEASE/reference/html/)
* [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
* [Spring JdbcTemplate Querying Exsmples](https://www.mkyong.com/spring/spring-jdbctemplate-querying-examples/)
* [Spring JDBC Overview](https://www.baeldung.com/spring-jdbc-jdbctemplate)
* [Spring JDBC and Composite Primary Keys](https://stackoverflow.com/questions/3133699/spring-jdbc-and-composite-primary-keys)
* [Calling Stored Procedures from Spring Data JPA](https://dzone.com/articles/calling-stored-procedures-from-spring-data-jpa)
* [Testcontainers](https://www.testcontainers.org)
* [Testcontainers and Spring Boot](https://dzone.com/articles/testcontainers-and-spring-boot)
* [JUnit 5 and Testcontainers](https://speakerdeck.com/timriemer/junit-5-and-testcontainers)
* [How to customize the Environment or ApplicationContext before it starts](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-spring-boot-application.html#howto-customize-the-environment-or-application-context)
* [Spring Boot 2 With JUnit 5 and Mockito 2 for Unit, Integration Testing](https://dzone.com/articles/spring-boot-2-with-junit-5-and-mockito-2-for-unit)
* [Base Starter for Vaadin Flow and Spring](https://vaadin.com/start/latest/project-base-spring)
* [Gradle Vaadin Flow plugin](https://github.com/devsoap/gradle-vaadin-flow/)
* [Oracle Database XE Downloads](https://www.oracle.com/technetwork/database/database-technologies/express-edition/downloads/index.html)
* [Oracle 12c XE Docker Image](https://hub.docker.com/r/sath89/oracle-12c/)
* [Oradle 11g XE Docker Image](https://hub.docker.com/r/sath89/oracle-xe-11g/)
* [Creating an Oracle DB Instance and Connecting to a Database on an Oracle DB Instance](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_GettingStarted.CreatingConnecting.Oracle.html)
* [Is Oracle's SYS_GUID() UUID RFC 4122 compliant?](https://stackoverflow.com/questions/6505837/is-oracles-sys-guid-uuid-rfc-4122-compliant)
* [Options for having Oracle Database vend UUID](https://oracle-base.com/articles/9i/uuid-9i)
* [Java UUID Generator](https://www.journaldev.com/17182/java-uuid-generator-java-guid)
* [Liquibase Documentation](http://www.liquibase.org/documentation/index.html)
* [Use Liquibase to Safely Evolve Your Database Schema](https://www.baeldung.com/liquibase-refactor-schema-of-java-app)