# 🦆 Pokémon Ranking API 🔝

![CI/CD status](https://github.com/erickgualpa/pokemon-ranking-api/actions/workflows/main.yml/badge.svg)

### Approach details

![alt text](etc/pokemon-ranking-api.png)

#### Decisions made

#### Improvements

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