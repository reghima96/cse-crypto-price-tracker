# Crypto Price Tracker (CPT)

## 📌 Project Overview

The **Crypto Price Tracker (CPT)** is a cloud-native microservices-based application that provides real-time cryptocurrency price tracking. It fetches price data from an external API, stores it, and allows users to view historical trends via a dashboard. It also supports authentication, role-based access control, and an admin interface for managing available cryptocurrencies.

## 🚀 Running the Project Locally

### **1️⃣ Prerequisites**

Ensure you have the following installed:

- Docker & Docker Compose
- Java 17+
- Maven
- A `.env` file (if needed for secrets)

### **2️⃣ Start the Project**

The easiest way to start the project locally is using Docker Compose:

```sh
cd cse-crypto-price-tracker
docker-compose -f docker-compose-dev.yml up --build
```

> This starts all microservices and databases.

Alternatively, to run services manually:

```sh
cd services/user-management-service && mvn spring-boot:run
cd services/price-fetch-service && mvn spring-boot:run
cd services/api-gateway-service && mvn spring-boot:run
cd services/config-service && mvn spring-boot:run
```

## 🔌 Available APIs

### **🔐 Authentication Service (User Management)**

| Method | Endpoint                               | Description                   | Access |
| ------ | -------------------------------------- | ----------------------------- | ------ |
| `POST` | `/auth/login`                          | User login, returns JWT token | Public |
| `POST` | `/auth/register`                       | User registration             | Public |
| `POST` | `/api/admin/users/{email}/roles/admin` | Promote user to admin         | Admin  |

### **📊 Price Fetching Service**

| Method | Endpoint                      | Description                        | Access        |
| ------ | ----------------------------- | ---------------------------------- | ------------- |
| `GET`  | `/api/prices/{symbol}/recent` | Get recent price data for a symbol | Authenticated |
| `GET`  | `/api/prices/dashboard`       | Load price dashboard UI            | Authenticated |
| `GET`  | `/api/prices/export`          | Export price data as Excel file    | Authenticated |
| `POST` | `/api/admin/cryptocurrency`   | Add a new cryptocurrency to track  | Admin         |

### **🛠 API Gateway Routes**

- **User Management Service:** `/auth/**`, `/api/admin/**`
- **Price Fetch Service:** `/api/prices/**`

## 🛠 Configuration Notes

- Uses **Spring Cloud Config** to centralize microservice configurations.
- Uses **API Gateway** for authentication & request routing.
- **Environment-specific configurations** are managed via Spring profiles (`dev`, `prod`).

## 🌍 Deployment

- For production, ensure all **secrets** (DB credentials, JWT secret, etc.) are properly set.

---

💡 **Next Steps:**

- Improve error handling & monitoring.


---

🚀 **Enjoy tracking crypto prices securely and efficiently!**

