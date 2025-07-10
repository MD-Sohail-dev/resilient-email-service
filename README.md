# 📧 Resilient Email Sending Service (Spring Boot)

This project is a **mock Resilient Email Sender Service** built using **Java** and **Spring Boot**.  
It simulates a production-ready email system with features such as:

- ✅ Retry mechanism with exponential backoff
- ✅ Fallback between multiple email providers
- ✅ Rate limiting per recipient
- ✅ Idempotency using request identifiers
- ✅ Status tracking for each email request

The goal is to demonstrate how to design a **robust and fault-tolerant system** that gracefully handles failures and avoids duplicate processing.
---

## 🚀 Features

✅ Retry mechanism with exponential backoff  
✅ Fallback between two mock email providers  
✅ Idempotency to prevent duplicate sends  
✅ Rate limiting (max 5 emails per recipient per minute)  
✅ Status tracking for email attempts  
✅ RESTful API using Spring Boot  
✅ Clean code using SOLID principles

---

## 🧩 Tech Stack

- Java 1.8
- Spring Boot 2.7.x
- Maven
- Lombok
- JUnit (for testing)

---

## 🏗️ Project Structure

```
src/main/java/com/mdsohail/emailsender/
│
├── controller/
│   └── EmailController.java
├── model/
│   ├── EmailRequest.java
│   ├── EmailStatus.java
├── service/
│   ├── EmailService.java
│   ├── EmailProvider.java
│   ├── MockEmailProviderA.java
│   ├── MockEmailProviderB.java
│   └── RateLimiter.java
```

---

## 🔌 API Endpoints

### 📤 Send Email

**POST** `/api/email/send`

**Request body:**
```json
{
  "to": "user@example.com",
  "subject": "Test Email",
  "body": "Hello world",
  "requestId": "unique-id-123"
}
```

**Response:**
```
SUCCESS
FAILED
DUPLICATE
RATE_LIMITED
```

---

### 📬 Check Email Status

**GET** `/api/email/status/{requestId}`

**Response:**
```json
{
  "status": "SUCCESS"
}
```

---

## 🧪 Running the Project

### ✅ Prerequisites:
- Java 1.8
- Maven
- IntelliJ IDEA or VS Code

### ▶️ Steps to Run:
```bash
# In project root:
mvn clean install
mvn spring-boot:run
```

Server starts on:
```
http://localhost:8081
```

---

## ✅ Future Enhancements (Optional)
- Add Circuit Breaker Pattern
- Add Background Queue and Scheduled Retries
- Store email statuses in a database (MySQL, MongoDB)
- Add real email provider integration (SendGrid, SMTP, etc.)

---

## 👨‍💻 Author

**MD Sohail Ansari**

📧 [sohail9749037725@gmail.com]  


---

## 📄 License

This project is for educational/demo purposes only.
