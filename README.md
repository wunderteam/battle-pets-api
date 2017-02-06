# Pet API

[![CircleCI](https://circleci.com/gh/wunderteam/battle-pets-api.svg?style=svg&circle-token=a3873feb3d77f83373634ede1de3cf521432f5d5)](https://circleci.com/gh/wunderteam/battle-pets-api)

This API is used to store pets for the BattlePets coding challenge.

## Endpoints:

All endpoints require an API to access. Please email you contact at Wunder for the current API key.

This key is passed via the `X-Pets-Token` HTTP header.

### List all pets

`GET /pets`

### Create pet

`POST /pets`

JSON payload:

```
{
	"name": "Fluffy",
	"strength": 12,
	"intelligence": 22,
	"speed": 21,
	"integrity": 66
}

```

### Get pet

`GET /pets/:id`

## Environments

### Staging

https://wunder-pet-api-staging.herokuapp.com/

#### Deploying

Deployment to staging is automatically done after a successful build on CircleCI.

### Production


#### Deploying

## Running locally app

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
