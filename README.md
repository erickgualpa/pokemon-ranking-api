# 🦆 Pokémon Ranking API 🔝

![CI/CD status](https://github.com/erickgualpa/pokemon-ranking-api/actions/workflows/main.yml/badge.svg)

### Approach details

![alt text](etc/pokemon-ranking-api.png)

#### Decisions made

I think that most relevant thing is having used hexagonal architecture to shape the project. I know there are many other
ways to apply this kind of architecture so this is only the way I see it. It helped not only to define an architecture
over a service but to also define testing boundaries that more than to be restrictive are orientative.

Furthermore, when I have to implement a solution I try to first consider the problem and for me that means building a
test that apart from helping to validate my solution also makes me think about other factors that in the first instance
I could have missed.

Clarified that, I first decided what would be domain and what would be infrastructure on this problem, which was to
retrieve Pokémon rankings based on different criteria. I put the action of retrieve and limit the ranking in domain as
it is something that could make sense to vary based on functional requirements. Construction of the ranking though has
been considered infrastructure as it could come directly from the PokéAPI (not really) or have some preprocessing or
even caching, without affecting the domain. I planned my steps in this way, and it could be seen
in [how I integrated my solutions into the main branch](https://github.com/erickgualpa/pokemon-ranking-api/pulls?q=),
first trying to resolve the functional requirements and then giving a workaround to the performance issue on the PokéAPI
requests.

#### Solution Improvements

Infrastructure could be refactored and optimized.
Domain is sufficiently well-defined to allow the addition of new rankings also giving freedom on the limit factor but
infrastructure is
tightly coupled to the PokéAPI. Considering that, the solution provided is just a first first approach to make it "
production ready"
but to be actually ready alternatives like caching should be considered. Even if caching could be a nightmare most of
the time, the nature
of the problem faced seems to be requesting it as the dependency with the PokéAPI is requiring HTTP requests each time a
ranking is built.

In any case, this problem seems to be addressed already by the PokéAPI as the data offered by it are details from beings
(fantastic beings in this case, but it could be compared to animals) that are not updated frequently. Caching a
preprocessed view
from the details required to calculate the rankings could help to reduce possible dependency issues such as time to get
the data or
connection failure. The caching could be performed on application startup or on the first request made (depending on
what affects less
to the service availability). Once something like that is configured, rankings would be built from data now owned by the
service, so even
a two-level-cache could be implemented as well (if needed), holding the most popular rankings built from the main cache.

On this scenario `pokemon-ranking-api` would be just a supplier of rankings based on in-memory preprocessed data.

#### Code Improvements

- Clean up e2e tests.
- Clean up `HttpRankingRepository`.
- Configure PokéAPI client bean from `PokemonRankingApiConfiguration` instead of building it in `HttpRankingRepository`.
- Define and throw specific exceptions on sad paths.
- Add integration tests covering sad paths from `HttpRankingRepository`.

---
🚀 Build and deploy service as container! 🐳
<br>

```shell script
./build_and_deploy.sh
```

💤 Clear service containers

```shell script
docker compose down --rmi local
``` 

🔹 Use of this service is specified through [SpringDoc OpenAPI Swagger UI](http://localhost:8080/swagger-ui/index.html).

📣 This project has been structured following a Hexagonal Architecture

[//]: # (Directory tree below was generated using 'tree -d -I target' command)

```
.
└── src
    ├── main
    │   ├── java
    │   │   └── org
    │   │       └── egualpam
    │   │           └── services
    │   │               └── pokemon
    │   │                   └── pokemonrankingapi
    │   │                       ├── application
    │   │                       ├── domain
    │   │                       └── infrastructure
    │   │                           ├── configuration
    │   │                           ├── controllers
    │   │                           └── repositories
    │   └── resources
    └── test
        ├── java
        │   └── org
        │       └── egualpam
        │           └── services
        │               └── pokemon
        │                   └── pokemonrankingapi
        │                       ├── application
        │                       ├── e2e
        │                       └── infrastructure
        └── resources

```