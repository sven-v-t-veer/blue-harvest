# green-grass

Run the application direcly in a docker container:
docker run -p 8080:8080 svenveer/green-grass:latest

When running the swagger ui can be found at:
http://localhost:8080/gg/swagger-ui/index.htm

The project is linked to SonarCloud (public)
https://sonarcloud.io/project/overview?id=sven-v-t-veer_green-grass

api-docs can be found in the docs directory.

to run locally:
> gradle assemble
> docker-compose build
> docker-compose up