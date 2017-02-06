# Pet API

This API is used to store pets for the BattlePets coding challenge.

## Endpoints:

### List all pets

### Create pet

### Get pet

[![CircleCI](https://circleci.com/gh/wunderteam/battle-pets-api.svg?style=svg&circle-token=a3873feb3d77f83373634ede1de3cf521432f5d5)](https://circleci.com/gh/wunderteam/battle-pets-api)

## Environments

### Staging

https://wunder-pet-api-staging.herokuapp.com/

## Deploying

Deployment to staging is automatically done after a successful build on CircleCI.

### Production


## Deploying

### API keys

All API endpoints are protected by a single API key/environment. They are passed via the `X-Pets-Token` HTTP header.

## Running app

`sbt ~run` - this will recompile if files change

## Running tests

`sbt test` - one off test run
`sbt ~test` - watch for file changes and re-run 

## Building artifact

`sbt dist`

## Databases

### Setup

You need to manually create the databases. For Postgres, you can use the [Postico app](https://eggerapps.at/postico/) or the [command line](https://www.postgresql.org/docs/9.1/static/manage-ag-createdb.html).

### Running

`./bin/migrateDB jdbc:postgresql://[host]:[port]/[db-name]?user=[username]&password=[password]`

**See https://jdbc.postgresql.org/documentation/94/connect.html for details on the URL.**

This command will need to be run once for each database, both `_development` and `_test`.
