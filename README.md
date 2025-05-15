# CUSTOM WEB CRAWLER
This repo contains a custom web crawler to scan PDFs searching for all the recurrences of a Regexp

## How to run the application locally
To spin up the Spring Boot App and the Postgres DB, please run the following command:

```sh
docker-compose up --build
```

Once the Docker containers are running, the Swagger can be consulted at this link:

http://localhost:8080/swagger-ui/index.html

## Running tests locally
The tests can be run using the Gradle wrapper:

```sh
./gradlew test
```

This command can be used to generate the testing reports:

```sh
./gradlew test jacocoTestReport
```

An HTML summary of the test coverage is available at the path `build/reports/jacoco/test/html/index.html`
At the conclusion of this task, the test coverage was higher than 80%.

## Test with exposed documents

In order to easily test the app, this custom script can be used:

```sh
./local-test.sh --serve-port <port-number>
```

The script will serve all the files in the `src/test/resources/testfiles` path and produce an url object like this one:

```
{
    "urls": [
        "http://192.168.2.105:34000/test_png.png",
        "http://192.168.2.105:34000/Testdata_Invoices.pdf",
    ]
}
```

Which can then be fed to the `/v1/document/scan/bulk` endpoint.

## Further information
If you are interested in checking the decision process and design choices that I made during the implementation, 
please refer to the Architectural Decision Records located at `/docs/adr`.


