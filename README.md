# Sample Notes Application using SpringBoot and MongoDB
This application demonstrates a simple Note application where a user can create, update, view, filter and delete notes.
The application is built using Spring Boot framework and utilizes MongoDB as a database.
The functionalities of the app are implemented as REST APIs.

## API Usage

The following APIs are available:

- [List Notes](apidocs/listnotes.md): `GET /api/v1/notes`
- [Create Note](apidocs/createnote.md): `POST /api/v1/notes`
- [Get Note](apidocs/getnote.md): `GET /api/v1/notes/{noteId}`
- [Update Note](apidocs/updatenote.md): `PUT /api/v1/notes/{noteId}`
- [Delete Note](apidocs/deletenote.md): `DELETE /api/v1/notes/{noteId}`
- [Get Note Stats](apidocs/notestats.md): `GET /api/v1/notes/{noteId}/stats`

## Application usage

The application can be run in multiple ways.

### Docker-compose

```
 docker compose build
 docker compose up
```

Using the following commands three containers will be launched, one for the spring application (port 8080), one for the mongodb database (port 27017) and one for a mongoexpress (port 8081) service to inspect the database if needed. This is the simplest way to run the application as the application and its dependencies will be automatically started.


### Build with maven

```
mvn clean spring-boot:build-image
```

Using the above command, a single container of the spring application will be created and can be launched alongside an existing mongodb container (should have the name notes-mongodb). It is important to note that both containers must share the same network to interact.

### Github container repository

It is also possible to grab the latest SpringBoot docker container from the [Github container repository](http://ghcr.io/ahmedts/notesapp:main) using the following command

```
docker pull ghcr.io/ahmedts/notesapp:main
```

In this case the application can be run alongside a mongodb container (should have the name: notes-mongodb). It is important to note that both containers must share the same network to interact.