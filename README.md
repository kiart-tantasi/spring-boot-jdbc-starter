# Setup

Before start the application, please run

`docker-compose up -d`

(When finish, please run `docker-compose down --volumes` to remove the container and volume)

It will create database `mydb` with root user that has password as `password`.

It will also table `test` and insert some dummy data and stored procedures.

**NOTE:** It will take around 1.30 minutes to finish setting up.

**or you can use your own MySQL server and config connection string, username and password at application.properties file**

<br>

<hr>

# Config

You can config connectionstring, username and password at `src/main/resources/application.properties`.

<br>
