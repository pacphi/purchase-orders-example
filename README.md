# Purchase Orders Example (WIP)

[![Build Status](https://travis-ci.org/pacphi/purchase-orders-example.svg)](https://travis-ci.org/pacphi/purchase-orders-example) [![Known Vulnerabilities](https://snyk.io/test/github/pacphi/purchase-orders-example/badge.svg)](https://snyk.io/test/github/pacphi/purchase-orders-example)

This project explores desing of a modern web application which includes: model, API and UI modules.


## Prerequisites

* An account with [Space Developer role](https://docs.cloudfoundry.org/concepts/roles.html#roles) access on a Cloud Foundry foundation, e.g., [Pivotal Web Services](https://run.pivotal.io)
* An [account](https://azure.microsoft.com/free/) on Microsoft [Azure](https://azure.microsoft.com/)
* [CF CLI](https://github.com/cloudfoundry/cli#downloads) 6.40.0 or better if you want to push the application to a Cloud Foundry (CF) instance
* [httpie](https://httpie.org/#installation) 0.9.9 or better to simplify interaction with API endpoints
* [jq](https://stedolan.github.io/jq/) 1.5 or better
* Java [JDK](https://openjdk.java.net/install/) 1.8u181 or better to compile and run the code
* [Gradle](https://gradle.org/releases/) 4.10.2 or better to build and package source code
* Docker for [Mac](https://store.docker.com/editions/community/docker-ce-desktop-mac) or [Windows](https://store.docker.com/editions/community/docker-ce-desktop-windows) 


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

Start the server

```

```

Connect via ---

```

```

If you want to access the server from the host machine where you have --- already installed, you could start it with 

```

```

then

```

```

#### AWS hosted Oracle

Fire up a browser and [login]() to the AWS console

Also launch a [Terminal](https://macpaw.com/how-to/use-terminal-on-mac) session, set your subscription account and create a new resource group, e.g.,

```

```

> Replace values for {params} above with your own.

Visit the AWS console in your browser, navigate to `RDS > Oracle`, and create a new instance

{Insert screenshot here}

{Add details for how to connect here}


### Database Creation

Once connected we need to create a database

```bash

```


## Run 

To startup the application, execute

### Simple case 

We'll fire up a local Docker instance of Oracle, then start the application 

```bash

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



## Credits and further reading

To those who've blazed trails...

