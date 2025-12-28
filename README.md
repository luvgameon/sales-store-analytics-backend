# sales-store-analytics-backend
📊 Sales & Store Analytics Backend Service

A production-ready analytics backend built using Spring Boot + MySQL, designed to power Power BI–style dashboards for sales and store performance insights.

This service exposes read-only, aggregation-focused REST APIs consumed by frontend dashboards (React / Angular) for KPIs, trends, drill-downs, and comparisons.

🚀 Tech Stack
Layer	Technology
Language	Java 17
Backend Framework	Spring Boot
ORM	Spring Data JPA / Hibernate
Database	MySQL (MySQL Workbench – localhost)
Build Tool	Maven
API Type	REST (JSON)
Security	Spring Security (CORS enabled)
🏗️ Architecture

The project follows a clean layered architecture:

controller  →  service  →  repository  →  database
               |
               → dto / mapper

Layers Explained

Controller → API endpoints, request handling

Service → Business logic, aggregation logic, YoY calculations

Repository → Native SQL queries (analytics optimized)

DTOs → Clean API response models

Config → Security, CORS, application setup

🗄️ Database Setup (MySQL Workbench – Localhost)
Prerequisites

MySQL Server running locally

MySQL Workbench installed

Database

Create a database:

CREATE DATABASE sales_report;

Tables (Simplified)
product (
  id BIGINT PRIMARY KEY,
  name VARCHAR(100),
  brand VARCHAR(100),
  category VARCHAR(100)
);

region (
  id BIGINT PRIMARY KEY,
  name VARCHAR(100)
);

store (
  id BIGINT PRIMARY KEY,
  region_id BIGINT,
  FOREIGN KEY (region_id) REFERENCES region(id)
);

sales (
  id BIGINT PRIMARY KEY,
  product_id BIGINT,
  store_id BIGINT,
  sale_date DATE,
  quantity INT,
  sales_value DECIMAL(12,2),
  FOREIGN KEY (product_id) REFERENCES product(id),
  FOREIGN KEY (store_id) REFERENCES store(id)
);

Recommended Indexes
CREATE INDEX idx_sale_date ON sales(sale_date);
CREATE INDEX idx_product_id ON sales(product_id);
CREATE INDEX idx_store_id ON sales(store_id);

⚙️ Application Configuration
application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sales_report
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

server:
  port: 8080

🔐 Security & CORS

All APIs are read-only

CORS enabled for frontend (React / Vite)

Allowed Origin:

http://localhost:5173

📦 API RESPONSE STANDARD

All APIs follow a consistent response format:

{
  "status": "SUCCESS",
  "data": {...},
  "timestamp": "2025-01-01T10:00:00"
}

📊 Available APIs
🟦 Sales KPIs

POST /api/dashboard/sales/kpis

Returns:

Total Sales

Total Orders

Average Order Value

Top Brand

YoY Growth %

📈 Sales Trend (Monthly – Year Based)

GET /api/dashboard/sales/trend?year=2025

{ "month": "Jan", "sales": 680243, "lastYear": 452400, "orders": 2682 }

🌍 Sales by Region

POST /api/dashboard/sales/by-region

{ "region": "Europe", "sales": 699901, "growth": 11.2, "stores": 140 }

🏷️ Sales by Brand

POST /api/dashboard/sales/by-brand

{ "brand": "Nike", "sales": 882399, "growth": 2.0, "orders": 10147 }

🔍 Brand → Product Drill-Down

POST /api/dashboard/sales/by-brand/products?brand=Nike

{
  "product": "Running Shoes",
  "brand": "Nike",
  "sales": 197881,
  "quantity": 5281
}

🏬 Store KPIs

POST /api/dashboard/stores/kpis

Returns:

Total Active Stores

YoY Change %

New Stores

Top Region

📉 Active Stores Trend (Year Based)

GET /api/dashboard/stores/trend?year=2025

{ "month": "Jan", "activeStores": 444, "newStores": 14, "closedStores": 1 }

🌍 Active Stores by Region

POST /api/dashboard/stores/by-region

{ "region": "Asia Pacific", "activeStores": 191, "growth": 11.0 }

🧠 Business Rules

Active Store → Store with ≥1 sale in selected period

New Store → Active in current period but not in previous period

Closed Store → Active in previous period but not current

YoY Growth → Compared with same period last year

Filters support:

Month / Quarter / Year

Brand = all / specific

Category = all / specific

Region = all / specific

▶️ How to Run the Project
1️⃣ Start MySQL

Ensure MySQL is running on:

localhost:3306

2️⃣ Update DB Credentials

Update application.yml

3️⃣ Run Backend
mvn spring-boot:run

4️⃣ Backend Runs At
http://localhost:8080

📈 Performance Considerations

Native SQL used for analytics (faster aggregations)

Indexed date & FK columns

Minimal joins per query

Optimized for dashboard workloads

Stateless APIs (scalable)

🎯 Ideal Use Cases

Power BI / Tableau-style dashboards

React / Angular analytics frontend

Interview take-home assignments

Portfolio projects

Real-world analytics backend

🚀 Future Enhancements

Redis caching for heavy queries

Pagination for drill-down APIs

Swagger / OpenAPI documentation

Role-based access (Admin / Analyst)

Materialized views for large datasets
