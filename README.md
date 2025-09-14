# Spring Boot Caching with Redis 🚀 | @Cacheable, @CachePut, @CacheEvict & TTL 🌿

Welcome to **[Backend Verse](https://www.youtube.com/@BackendVerse)**! 🎥  
In this tutorial, we explore **Spring Boot Caching with Redis** – using annotations like `@Cacheable`, `@CachePut`, `@CacheEvict`, and TTL (Time To Live). 🚀  

👉 Watch the full tutorial series here:  
[Spring Boot Tutorial Series in Hindi 🌿](https://www.youtube.com/playlist?list=PLdUn31k8Q721HBdMQzyl403o-bUtd31Wb)

---

## ▶ Watch the Tutorial

📺 **[Spring Boot Caching with Redis 🚀 | Explained in Hindi](https://youtu.be/0OyzhSs4BcQ)**  

---

## 📝 What You’ll Learn
📌 What is caching & why it’s important  
⚡ How to integrate Redis with Spring Boot  
🔑 How `@Cacheable`, `@CachePut`, and `@CacheEvict` work  
🏷️ How to configure TTL (Time To Live)  
💡 Real-world REST API caching examples  

---

## 🎯 Why Watch This Video?
With caching in Spring Boot, you can:  
✔️ Improve performance & reduce DB load  
✔️ Build scalable & optimized apps  
✔️ Use Redis effectively with Spring Boot  
✔️ Apply clean caching strategies in production  

---

## 🔧 Tools & Technologies Used
- Java 17  
- Spring Boot  
- Redis  
- Gradle  
- IntelliJ IDEA  
- Postman  
- Docker  
- Docker Compose  

## ⚙️ Project Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/learnwithdeveloper/spring-boot-redis
   cd spring-boot-redis
   ```

2. ## 🐳 Run with Docker Compose (Recommended)

If you don’t want to install PostgreSQL & Redis manually, you can use **Docker Compose**.

1. Start containers:

   ```bash
   docker compose up -d
   ```

2. Check running containers:

   ```bash
   docker ps
   ```

   Look for container named **docker-spring-postgres**.

3. Connect to PostgreSQL inside container:

   ```bash
   docker exec -it <container_id> bash
   psql -U postgres
   ```

   ✅ Welcome! Now you are inside the PostgreSQL database.

---

4. Run the Spring Boot application.

---

## 📬 API Endpoints with cURL

### 1️⃣ Create User

```bash
curl -X POST http://localhost:8080/users \
   -H "Content-Type: application/json" \
   -d '{
     "name": "John Doe",
     "email": "johndoe@gmail.com"
   }'
```

### 2️⃣ Get All Users

```bash
curl -X GET http://localhost:8080/users
```

### 3️⃣ Get All Users (Searching, Sorting, Pagination) - API

```bash
curl --location 'http://localhost:8080/users?page=0&size=10&sortKey=name&sortValue=asc&name=it&email=das'
```

### 4️⃣ Update User

```bash
curl -X PATCH http://localhost:8080/users/{userId} \
   -H "Content-Type: application/json" \
   -d '{
     "name": "John Doe Updated Name",
     "email": ""
   }'
```

### 5️⃣ Delete User

```bash
curl -X DELETE http://localhost:8080/users/{userId}
```

## 🧪 Import Postman Collection

We have already included the Postman Collection file in this repo:
**`Spring Boot Crud Using PostgreSql.postman_collection.json`**

Follow these steps:

1. Open **Postman**.
2. Click on **Import** (top-left corner).
3. Select the file → `Spring Boot Crud Using PostgreSql.postman_collection.json`.
4. Done ✅ Now you can directly test APIs without writing cURL.

## 📢 Stay Connected

👍 Like the video if you found it helpful  
💬 Drop your questions in the comments  
🔔 Subscribe to **[Backend Verse](https://www.youtube.com/@BackendVerse)** for more Spring Boot tutorials in Hindi

---