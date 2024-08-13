# 🦆 Pokémon Ranking API 🔝

![CI/CD status](https://github.com/erickgualpa/pokemon-ranking-api/actions/workflows/main.yml/badge.svg)
[![](https://img.shields.io/badge/Spring%20Boot%20Version-3.3.2-blue)](/pom.xml)
[![](https://img.shields.io/badge/Java%20Version-21-blue)](/pom.xml)

🧪 Run tests
<br>

```shell script
./mvnw clean verify
```

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
    │   │           └── contexts
    │   │               └── pokemon
    │   │                   └── pokemonrankingapi
    │   │                       ├── application
    │   │                       │   └── ports
    │   │                       │       └── out
    │   │                       ├── domain
    │   │                       │   └── exceptions
    │   │                       └── infrastructure
    │   │                           └── adapters
    │   │                               ├── configuration
    │   │                               │   └── properties
    │   │                               │       └── clients
    │   │                               ├── in
    │   │                               │   └── controllers
    │   │                               └── out
    │   │                                   └── pokemonsearchrepository
    │   │                                       ├── concurrent
    │   │                                       ├── shared
    │   │                                       └── webflux
    │   └── resources
    └── test
        ├── java
        │   └── org
        │       └── egualpam
        │           └── contexts
        │               └── pokemon
        │                   └── pokemonrankingapi
        │                       ├── application
        │                       ├── e2e
        │                       └── infrastructure
        │                           └── adapters
        │                               └── out
        │                                   └── pokemonsearchrepository
        │                                       └── concurrent
        └── resources
```
