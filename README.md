# Pet API

This API is used to store pets for the BattlePets coding challenge.

## Endpoints:

### List all pets

### Create pet

### Get pet

[![CircleCI](https://circleci.com/gh/wunderteam/xxxx/tree/master.svg?style=svg&circle-token=xxxxx)](https://circleci.com/gh/wunderteam/xxxx/tree/master)

## Environments

### Staging


## Deploying

Deployment to staging is automatically done after a successful build on CircleCI.

### Production


## Deploying

Deployment to production is done via CircleCI by creating a tag on the master branch. One easy way to do this is to use GitHub releases:
https://help.github.com/articles/creating-releases/

The tag format is `release-YYYYMMDDHHMM`.

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
