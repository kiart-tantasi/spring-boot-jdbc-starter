# Setup

Before starting the application, please run

`docker-compose up -d`

or

`make up` if you have `make` installed

<br>

It will create database `mydb` with root user that has password as `password`.

It will also table `test` and insert some dummy data and stored procedures.

<br>

**NOTE:** It will take around 1.30 minutes to finish setting up.

**You can use your own MySQL server by configuring connection string, username and password at application.properties file**

<br>
<br>

# Configuration

You can configure connection string, username and password at `src/main/resources/application.properties`.

<br>
<br>

# Clean up

When finish, please run

`docker-compose down --volumes`

or

`make down`

to remove the container and the volume

<br>
<br>
