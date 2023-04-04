# Multithreaded service research
Test Task Application

# Getting Started
### Start with docker

```bash
docker compose build
docker compose up
```

Docker will build and start Redis, DB, server and client applications \
It's also possible to run server application without load testing:
```bash
docker compose up server
```

### Settings

 - [Server properties](server/src/main/resources/application.properties)
 - [Client properties](client/src/main/resources/application.properties)
 - [Postgres setup](postgres/scripts/initialization.sql)

### Results

110k requests per minute on i7-10700 with ratio of 50

### Additional Links

- [Github](https://github.com/techtheist)
- [⬒⬓◲⬕⬒⬕⬓◳⬔⬒](https://techtheist.ru)