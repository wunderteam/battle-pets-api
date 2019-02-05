# Pets API

[![CircleCI](https://circleci.com/gh/wunderteam/battle-pets-api.svg?style=svg&circle-token=a3873feb3d77f83373634ede1de3cf521432f5d5)](https://circleci.com/gh/wunderteam/battle-pets-api)

JSON API for provisioning and managing pets as part of the Wunder BattlePets coding challenge.

## Usage

The API is hosted at https://wunder-pet-api-staging.herokuapp.com/

### Authentication

All endpoints require a valid API token. Ask your contact at Wunder for a token.

Include the token in the `X-Pets-Token` HTTP header.

### Responses

All responses will be encoded as JSON. As such, ensure you include an `Accept: application/json` HTTP header.

HTTP status codes are used as follows:

* `200`: ok
* `201`: pet created
* `400`: malformed request JSON
* `404`: pet not found
* `422`: pet not valid

### Errors

On failure, the response will include the appropriate HTTP status code and any errors encoded as JSON.

```
{"errors":["Pet name cannot be empty.","Pet integrity must be greater than 0."]}
```

### Endpoints

#### `POST /pets`

Create a pet.

The POST body should be encoded as JSON.

```
$ curl \
    -H 'X-Pets-Token: <your-token>' \
    -H 'Accept: application/json' \
    -H 'Content-Type: application/json' \
    -d '{ "name": "Fluffy", "strength": 12, "intelligence": 22, "speed": 21, "integrity": 66 }' \
    https://wunder-pet-api-staging.herokuapp.com/pets

{"id":"d44c512d-65f2-4173-a7aa-a1ebc8cc52d6","name":"Fluffy","strength":12,"intelligence":22,"speed":21,"integrity":66}
```

#### `GET /pets`

Get all pets.

```
$ curl \
    -H 'X-Pets-Token: <your-token>' \
    -H 'Accept: application/json' \
    -H 'Content-Type: application/json' \
    https://wunder-pet-api-staging.herokuapp.com/pets

[{"id":"d44c512d-65f2-4173-a7aa-a1ebc8cc52d6","name":"Fluffy","strength":12,"intelligence":22,"speed":21,"integrity":66}, ...]
```

#### `GET /pets/:id`

Get a pet by ID.

```
$ curl \
    -H 'X-Pets-Token: <your-token>' \
    -H 'Accept: application/json' \
    https://wunder-pet-api-staging.herokuapp.com/pets/d44c512d-65f2-4173-a7aa-a1ebc8cc52d6

{"id":"d44c512d-65f2-4173-a7aa-a1ebc8cc52d6","name":"Fluffy","strength":12,"intelligence":22,"speed":21,"integrity":66}
```

## Running Locally

You are welcome and encouraged to run the API locally on your machine. To do so, clone this repository and perform the following steps.

### Dependencies

* SBT 0.13.x (Scala Build Tool) - available via Homebrew
* OpenJDK 8 (newer versions will not work)
* Postgres - available via Homebrew

### Setup

Install Java and SBT

```
brew tap AdoptOpenJDK/openjdk
brew cask install adoptopenjdk8
export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home
brew install sbt@0.13
```

Create test and development databases.

```
$ createdb pets_development
$ createdb pets_test
```

(More on the Postgres `createdb` command [here](https://www.postgresql.org/docs/9.1/static/manage-ag-createdb.html).)

Migrate the databases using the included `bin/migrateDB` script. The script requires a valid JDBC database connection string to run. You can specify one as the environment variable `JDBC_DATABASE_URL`:

```
$ env JDBC_DATABASE_URL="jdbc:postgresql://localhost:5432/pets_development?user=[username]&password=[password]" \
    ./bin/migrateDB
```

Or pass it directly to the script:

```
$ ./bin/migrateDB "jdbc:postgresql://localhost:5432/pets_test?user=[username]&password=[password]"
```

(More on the Postgres JDBC connection string [here](https://jdbc.postgresql.org/documentation/94/connect.html).)

### Running

The app will expect the `JDBC_DATABASE_URL` environment variable in order to connect to the database, so ensure this variable is loaded into your environment before running the app.

To run the app, use the SBT `run` command. Using `~run` (note the tilde) causes the app to recompile if files change.

```
$ export JDBC_DATABASE_URL="jdbc:postgresql://localhost:5432/pets_development?user=[username]&password=[password]"
$ sbt "~run"
```

### Testing

As when running the app, ensure the appropriate `JDBC_DATABASE_URL` is in scope before running tests.

To run the tests, use the SBT `test` (or its tilde-clad cousin, `~test`) command.

```
$ export JDBC_DATABASE_URL="jdbc:postgresql://localhost:5432/pets_test?user=[username]&password=[password]"
$ sbt test
```

---

Happy battling!
