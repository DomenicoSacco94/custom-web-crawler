# CUSTOM WEB CRAWLER

This repo contains a Web Crawler which scans the web pages starting from some seed URLs, looking for occurrences about a given Topic (expressed in a collection of regexps).

Once the corresponding occurrences are found, fact(s) for each occurrence are extrapolated using Spring AI.

![Block Diagram](./docs/images/Block%20diagram.jpg)

## Purpose of the Repo
I wanted to create a POC to check if LLMs could be leveraged to quickly browse through high-volume information sources (Social medias, news feeds, ...)
and extrapolate only the relevant facts about a given topic. 

In a time when LLMs will produce a humungous amount of noise on the web, 
I wondered how effectively they could be used as their own antidote: extrapolate only the relevant facts from huge amounts of text.

## Implementation choices

Through this POC I wanted to get acquainted with the following technologies:
- [Spring AI](https://spring.io/projects/spring-ai), particularly its integration with chat-like LLMs.
- [Kafka](https://kafka.apache.org/), used here to handle the asynchronous communication.
- [TestContainers](https://testcontainers.com/), used to create comprehensive (and complicated) integration tests.

If you are interested in checking in more detail the decision process and design choices that I made during the implementation,
check out the Architectural Decision Records located at `/docs/adr`.

## How to run the application locally
To spin up the necessary Docker containers, run the following command:

```sh
docker-compose up --build
```

Once the Docker containers are running, the Swagger can be consulted at this link:

http://localhost:8080/swagger-ui/index.html

## Example of usage

Say you would like to scan a news feed website for relevant facts about a given industry sector.

First, we need to create a **Topic** with some **Regexps** representing it:

**POST /v1/topics**

```
{
    "name": "Sector name",
    "description": "An important industry sector",
    "regexps": [
        {
            "pattern": "(?i)\\bCompanyA\\b",
            "description": "Company A"
        },
        {
            "pattern": "(?i)\\bCompanyB\\b",
            "description": "Company A"
        }
    ]
}
```

Then we can trigger the scan of the corresponding news Feed endpoint:

**POST /v1/scan**

```
{
    "topicId": 1,
    "urls": [
        "http://news.website.com/sectorname"
    ]
}
```

The DB will then start getting filled with facts about the chosen industry sector, which are accessible through the endpoint **GET /v1/facts**.

**DISCLAIMER**: Depending on the quality of the Ollama model used, the results might be false, irrelevant and/or hallucinated.
There is also the strong tendency to the extrapolated facts being duplicated. 

An improvement of this crawler could be to re-use the LLMs on the fact collection itself to compile a holistic report, without duplications.
Different LLMs models can be also combined for different applications, for instance using a quicker (but less effective) LLM to extrapolate the single facts and then a "smarter" (but slower) LLM to compile a report.

## Running tests locally
The tests can be run using the Gradle wrapper:

```sh
./gradlew test
```

This command can be used to generate the testing reports:

```sh
./gradlew test jacocoTestReport
```

An HTML summary of the test coverage is available at the path `build/reports/jacoco/test/html/index.html`.

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

Which can then be fed to the `/v1/scan` endpoint.



