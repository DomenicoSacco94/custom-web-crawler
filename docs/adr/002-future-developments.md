# Future developments

## Design and architecture enhancements

### IBAN
The IBAN is a central entity for this project. Due to time constraints I simplistically defined it as a `String`, but it is actually composed
by other elements (such as country code, bank account number, ...).
The IBAN should ideally be its own object and have a `validate()` method to ensure that its format is correct.
The IBAN object can also have its `format()` method, for a leaner design and to be able to solve problems like finding a certain iban with or without spacing in a more elegant way.
The business could also introduce other requirements such as blacklisting a range of IBANs (e.G. by country or by bank code) and a dedicated
IBAN entity would help implementing it with less effort.

The `BlacklistedIban` entity contains only the IBAN so far, but in the future it might contain other important metadata, such as
the reason for why an IBAN got blacklisted. This is also why I decided to use a Projection when querying the IBAN strings, so that
only the information we actually need to perform the validation are queried.

### Deployment
Deploying all the components of this microservice (Postgres DB, Spring boot application and the Kafka server) could be a challenging endeavor.
Ideally, the IaaC paradigm should be used and the deployment instruction should be contained in dedicated files written with languages such as 
Terraform or Ansible.
Another important aspect is the secure storage of the credentials, like those used to access to the DB, which ideally happens by using secrets.
On the local machine Kafka is communicating through `PLAINTEXT`, making no use of secure communication and message encryption.
On a production environment this should be definitively avoided.
In order to implement all the requirements written above, the `application.yml` can be extended with a `production` profile.

### Miscellaneous
There are other elements that I think are important in a full-blown production application, such as:
- **Observability:** I introduced `slf4j` as a logging library, but in a production environment, it would
  be better to leverage additional tools, such as Xray for AWS and OpenSearch/ElasticSearch to store the logs and significantly enhance troubleshooting.
- **Built-in Spring diagnostic endpoints and actuators:** The diagnostic health check endpoints
  such as `/health` and `/actuator/metrics` can be used to monitor the production instance(s).
- **Dependency updates:** CVEs are discovered all the time, so it is important to update the many project dependencies. An approach
  is using the Dependabot offered by GitHub, which automatically issues PRs updating the dependencies, which can be then periodically reviewed.
- **Caching** using tools like Caffeine or even a dedicated cache like Redis, we can avoid to access to the db to fetch the IBANs every time we want to validate a document.
  Caution is needed with this approach, as the cache needs to be write-through, as more and more blacklisted IBANs can be added and changed.
    