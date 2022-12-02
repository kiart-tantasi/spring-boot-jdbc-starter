# Setup

Before starting the application, please run

```
docker-compose up -d
```

or `make up` if you have `make` installed to create MySQL server on Docker

<br>

It will also create database `mydb` with root user that has password as `password`.

Moreover, it will create table `test` and insert some dummy data and stored procedures.

<br>

**NOTE:** It will take around 1.30 minutes to finish setting up.

<br>
<br>

# How to Run App

Please follow step above to set up MySQL first.

If you use VS Code, you can right-click at `JdbcMysqlApplication.java` and click `Run Java`.

If you do not have VS Code, please run this command to start the app

```
./gradlew bootRun
```

<br>
<br>

# Configuration

You can configure connection string, username and password at

```
src/main/resources/application.properties
```

<br>
<br>

# Clean up

When finish, please run

```
docker-compose down --volumes
```

or `make down` to remove the container and the volume

<br>
<br>

# Testing

Testing is only 50% code coverage so the app is now error-prone.

**For now, you can test with 2 steps**

1. run `./gradlew test` (50% coverage)

2. start MySQL docker and run `./gradlew bootRun`

<br>
<br>
