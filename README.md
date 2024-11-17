
# Stick Figure Combat - Backend Services

This project comprises multiple microservices to support the **Stick Figure Combat** game. Each service is designed to handle a specific functionality, ensuring scalability and maintainability. The services communicate via REST APIs and are containerized for deployment on Kubernetes.

---

## Services Overview

### 1. **User Service**
- **Purpose**: Manages user accounts and information.
- **Responsibilities**:
  - Register and retrieve user details.
  - Validate user actions (e.g., for purchasing power-ups).
- **API Endpoints**:
  - `GET /users/{userId}`: Fetch user details.
  - `POST /users`: Create a new user.
  - `PUT /users/{userId}`: Update user information.

---

### 2. **Game Service**
- **Purpose**: Manages game logic, including combat mechanics, weapon assignments, and power-ups.
- **Responsibilities**:
  - Create and start games.
  - Manage gameplay mechanics (random weapon assignments, status effects).
  - Notify users of game outcomes.
- **API Endpoints**:
  - `POST /games`: Create a new game.
  - `POST /games/start/{gameId}`: Start a specific game.
  - `POST /games/purchase-power-up/{gameId}/{fighterName}`: Purchase and apply a power-up.

---

### 3. **Payment Service**
- **Purpose**: Handles payment processing for purchasing power-ups or other monetized features.
- **Responsibilities**:
  - Process payments for in-game items.
  - Maintain transaction records.
- **API Endpoints**:
  - `POST /payments/process`: Process a payment.
  - `GET /payments/transaction/{transactionId}`: Retrieve transaction details.

---

## Prerequisites

- **Java 17** (or higher)
- **Maven 3.6+**
- **Docker** and **Docker Compose**
- **Kubernetes** (optional for deployment)
- **Postman** or any API testing tool (for testing endpoints)

---

## Running the Services Locally

Each service is structured as a standalone Spring Boot application.

### 1. **Clone the Repository**

```bash
git clone https://github.com/your-repo/stick-figure-combat.git
cd stick-figure-combat
```

### 2. **Running Services Individually**

Navigate to each service directory (`user-service`, `game-service`, `payment-service`) and follow these steps:

#### **Step 1: Build the Application**

```bash
mvn clean package
```

#### **Step 2: Run the Application**

```bash
java -jar target/<service-name>-0.0.1-SNAPSHOT.jar
```

Each service will run on a specific port (configured in `application.yml`):
- **User Service**: `http://localhost:8081`
- **Game Service**: `http://localhost:8082`
- **Payment Service**: `http://localhost:8083`

---

### 3. **Running with Docker**

Each service includes a `Dockerfile`. To build and run them as containers:

#### **Step 1: Build Docker Images**

```bash
cd user-service
docker build -t user-service .

cd ../game-service
docker build -t game-service .

cd ../payment-service
docker build -t payment-service .
```

#### **Step 2: Run Containers**

```bash
docker run -p 8081:8081 user-service
docker run -p 8082:8082 game-service
docker run -p 8083:8083 payment-service
```

---

### 4. **Running with Docker Compose**

A `docker-compose.yml` is provided to run all services together.

#### **Step 1: Start Services**

```bash
docker-compose up --build
```

#### **Step 2: Access Services**

- **User Service**: `http://localhost:8081`
- **Game Service**: `http://localhost:8082`
- **Payment Service**: `http://localhost:8083`

---

### 5. **Deploying on Kubernetes**

Each service includes Kubernetes deployment and service YAML files.

#### **Step 1: Apply Deployment Files**

```bash
kubectl apply -f kubernetes/user-service-deployment.yml
kubectl apply -f kubernetes/game-service-deployment.yml
kubectl apply -f kubernetes/payment-service-deployment.yml
```

#### **Step 2: Access Services**

Use `kubectl get services` to find the external IP or port for each service.

---

## API Documentation

### Example Game Workflow

1. **Create a User**:
   - Endpoint: `POST /users`
   - Payload:
     ```json
     {
       "username": "player1",
       "email": "player1@example.com"
     }
     ```

2. **Create a Game**:
   - Endpoint: `POST /games`
   - Response:
     ```json
     {
       "id": 123,
       "status": "CREATED"
     }
     ```

3. **Start the Game**:
   - Endpoint: `POST /games/start/123`

4. **Purchase Power-Up**:
   - Endpoint: `POST /games/purchase-power-up/123/RedFighter`
   - Parameters: `userId=1`
   - Payload:
     ```json
     "BOOST_STRENGTH"
     ```

5. **Process Payment**:
   - Endpoint: `POST /payments/process`
   - Payload:
     ```json
     {
       "userId": 1,
       "amount": 100
     }
     ```

---

## Testing

- Use **Postman** or **curl** to test endpoints.
- Run integration tests in each service:
  ```bash
  mvn test
  ```

---

## Future Enhancements

- **Authentication**: Add OAuth2 for secure communication between services.
- **Real Payment Gateway**: Integrate Stripe or PayPal for payment processing.
- **Metrics and Logging**: Use Prometheus and Grafana for monitoring.

---

## Contributors

- **Your Name** - Developer
- **Your Team** - Contributors

---

## License

This project is licensed under the MIT License. See `LICENSE` for details.
