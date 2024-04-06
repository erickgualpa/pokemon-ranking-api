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