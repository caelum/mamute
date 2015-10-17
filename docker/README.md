# mamute
Dockerfile for Mamute
-----------

It takes the basic mamute-1.3-0 distribution, adds a script to parameterize the hibernate config file so 
you can start the container with following options

* DB_HOST : the host of your MySQL installation (or container)
* DB_USER : the user 
* DB_PWD : its password
* DB_NAME : the name of the database
* DB_PORT : the port running your database.
* MAMUTE_HOST : the external hostname (defaults to localhost, but change it when you need to access it via another URL)
* MAMUTE_PORT : the external port (defaults to 8080, but change it when you need to access it via another URL)

This container has been tested together with a default mysql container


Example :
**docker run -ti -e DB_HOST=192.168.1.4 -e DB_USER=mamute -e DB_PWD=mamute_pwd -e DB_NAME=mamute -e DB_PORT=7000 -p 8080:8080 kullervo16/mamute**
