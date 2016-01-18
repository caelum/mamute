Docker-Compose Production Version
=================================

## How to edit to your needs ##

You'll start by editing the file `docker-compose.yml` in which you'll find all relevant config files. Start by setting your own database username and password. Remember to read all the comments in this section!.

To edit Mamute relevant settings, look for the directory `Mamute`.

If you want to add HTTPS or set your own domainname, go to the file `./Nginx/conf.d/test.conf` and look for comments.

## How to bring everything up ##

Install [Docker](https://docs.docker.com/engine/installation/) and [Docker-Compose](https://docs.docker.com/compose/install/). Once your finished, go to the directory where the docker-compose.yml is located and execute

`docker-compose up -d`

to bring the whole system up. On the first start it takes a while to intialize the database and bring everything up.

To debug the changes you have made, use [Docker-Compose cli](https://docs.docker.com/compose/reference/docker-compose/). For example, let's show the console log of a container:

`docker-compose logs mamute`
