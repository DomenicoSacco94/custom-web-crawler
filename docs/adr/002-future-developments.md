# Future developments

## Design and architecture enhancements

### Deployment
Deploying all the components of this microservice (Postgres DB, Spring boot application, Kafka server and the Ollama server) could be a challenging endeavor.
Ideally, the IaaC (Infrastructure as a Code) paradigm should be used, storing the corresponding deployment instruction in dedicated files written with languages such as 
Terraform or Ansible.
Another important aspect is the secure storage of the credentials, like those used to access to the DB, which ideally happens by using secrets.
On the local machine Kafka is communicating through `PLAINTEXT`, making no use of the available encryption protocols. On a production environment this should be definitively avoided.
In order to implement all the requirements written above, the `application.yml` can be extended with a `production` profile.

### Miscellaneous
There are other elements that I think are important in a full-blown production application, such as:
- **Observability:** I introduced `slf4j` as a logging library, but in a production environment, it would
  be better to leverage additional tools, such as Xray for AWS and OpenSearch/ElasticSearch to store the logs and significantly enhance troubleshooting.
- **Built-in Spring diagnostic endpoints and actuators:** The diagnostic health check endpoints
  such as `/health` and `/actuator/metrics` can be used to monitor the production instance(s).
- **Dependency updates:** CVEs are discovered all the time, so it is important to update the many project dependencies. An approach
  is using the Dependabot offered by GitHub, which automatically issues PRs updating the dependencies, which can be then periodically reviewed.
- **Caching** using tools like Caffeine or even a dedicated cache like Redis, we can avoid to access to the db to fetch the Regexps every time.
    