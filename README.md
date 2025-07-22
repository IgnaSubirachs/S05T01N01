# 🃏 Blackjack Reactive API

Welcome to the **Blackjack Reactive API**, a project built using **Spring Boot WebFlux**, **MongoDB**, and **MySQL R2DBC**.  
This is part of a final bootcamp sprint focused on reactive programming, clean architecture, testing, and API documentation.

---

## 🚀 Tech Stack

- ⚙️ **Spring Boot 3.5.3**
- 🔁 **Spring WebFlux**
- ☁️ **Reactive MongoDB** for `Game` entity
- 🗃️ **Reactive MySQL (R2DBC)** for `Player`, `Reward`, and `Ticket`
- 🧪 **JUnit 5**, **Mockito**, **StepVerifier**
- 📄 **OpenAPI/Swagger UI** for live documentation
- 🧰 **Lombok** and **Validation API**
- 🐳 **Docker** for containerization

---

## 🎮 Features

### Player Management
- Create, retrieve, update, and delete players.
- Search players by name.
- Calculate **win ratio** based on game history.
- Retrieve global **player ranking** with position, win count, and ratio.

### Game Management
- Create a new Blackjack game.
- Start game: draw initial cards.
- Hit: draw a new card for the player.
- Stand: let dealer play and evaluate winner.
- Fetch game history.
- Full game logic with **custom card objects**, **status evaluation**, and **deck simulation**.

---

## 📦 Endpoints Overview

### 🎲 `/games`
| Method | Path                | Description                  |
|--------|---------------------|------------------------------|
| `POST` | `/games`            | Create new game              |
| `GET`  | `/games/{id}`       | Get game by ID               |
| `POST` | `/games/{id}/start` | Deal cards (start round)     |
| `PUT`  | `/games/{id}/hit`   | Player draws a card          |
| `PUT`  | `/games/{id}/stand` | End player turn              |
| `GET`  | `/games/ranking`    | Get player ranking (win %)   |

### 🧍 `/players`
| Method | Path               | Description                |
|--------|--------------------|----------------------------|
| `POST` | `/players`         | Create new player          |
| `GET`  | `/players`         | Get all or search by name  |
| `GET`  | `/players/{id}`    | Get player by ID           |
| `PUT`  | `/players/{id}`    | Update player name         |
| `DELETE`| `/players/{id}`   | Delete player              |

> Full Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🛠️ Project Structure

src/
└── main/
├── controller/
├── dto/
├── mapper/
├── model/
├── repository/
├── service/
├── logic/ # Blackjack game engine logic
└── exception/ # Global error handling

yaml
Copiar
Editar

---

## ✅ Testing Coverage

All core components are covered with unit and integration tests:

- `GameServiceImplTest` — game logic and operations
- `GameEngineTest` — full logic evaluation (hit, stand, draw)
- `GameControllerTest` — integration with WebTestClient
- `PlayerServiceImplTest` — win ratio & name updates
- `PlayerControllerTest` — full REST endpoint testing
- `MapperTest` — player and game DTO/entity conversion
- `GlobalExceptionHandlerTest` — error responses and validation

---

## 🐳 Dockerization (Level 2)

The application is fully dockerized for local execution and testing.

### 🔧 Build Docker Image
```bash
docker build -t blackjack-api .
▶️ Run the Container
bash
Copiar
Editar
docker run -p 8080:8080 blackjack-api
📦 Push to Docker Hub (Optional)
bash
Copiar
Editar
docker tag blackjack-api ignasisubirachs/blackjack-api:latest
docker login
docker push ignasisubirachs/blackjack-api:latest
👉 Docker Hub Repo:
https://hub.docker.com/repository/docker/ignasisubirachs/blackjack-api