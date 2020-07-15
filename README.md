# Weather service assignment

Service enabling to lookup weather information and have the result persisted in a database.
The service will use an API from the Open Weather API (https://openweathermap.org/).
Before you can use the service, you'll first have to request an API key (https://openweathermap.org/appid).  

# Starting the service

When bootstrapping the service, following properties are expected to be passed as VM arguments:

* spring.datasource.username
* spring.datasource.password
* spring.datasource.url
* openweather-api.token (this represents the API key from openweathermap)

Example:

mvn spring-boot:run 
    -Dspring.datasource.username=myassignment 
    -Dspring.datasource.password=myassignment 
    -Dspring.datasource.url=jdbc:postgresql://localhost:5432/myassignment 
    -Dopenweather-api.token=12345

This setup is expecting a running Postgres server.

For convenience, you'll find a docker-compose.yml file in the project root which will bootstrap a Dockerized Postgres database.
It can be started running following command. Make sure you run this before starting the application ;-)

Note, an Open Weather API key can be found here: https://openweathermap.org/appid

docker-compose up

# Local development

For local development, you can use the "dev" profile in order to skip passing the sensitive settings to the service.
Meaning, you can start the service using:

mvn spring-boot:run -Dspring.profiles.active=dev

Note that this profile is not the default one. Also, provide your own API key into the application-dev.properties file.

# Example endpoint consumption:

http://localhost:8080/v1/weather?city=Utrecht

A successful request will result in a structure like:

{
    "id": 1,
    "city": "Nieuwegein",
    "country": "NL",
    "temperature": 289.09
}

Any other response results in:

{
    "status": <<Some response code>>,
    "error": "<<Some reasone message>>"
}




