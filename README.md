# 🛒 MonkCommerce Coupon Service

A Spring Boot based backend system for managing and applying different types of coupons in an e-commerce platform.

This project implements Cart, Product, and Buy-X-Get-Y (BXGY) coupon types with proper validation, exception handling, and unit testing.

---

# 🚀 Features

## ✅ Coupon Types Supported

### 1️⃣ Cart Coupon

* Minimum cart value validation
* Flat or percentage discount
* Maximum discount cap support
* Proportional discount distribution across cart items

### 2️⃣ Product Coupon

* Applied only to specific products
* Minimum quantity validation
* Flat or percentage discount

### 3️⃣ BXGY (Buy X Get Y)

* Buy specific products in required quantity
* Get specific products for free
* Repetition limit support

---

# 🏗 Architecture

Layered Architecture:

Controller → DTO → Service → Entity → Repository → Database

* Controllers handle HTTP requests
* DTOs handle request/response models
* Services contain business logic
* Entities map to database tables
* Repository handles data access (Spring Data JPA)
* Global Exception Handler manages structured error responses

---

# 🧠 Business Logic Highlights

* Expired coupon validation
* Inactive coupon validation
* Coupon not found handling
* Max usage validation
* Repetition limit enforcement for BXGY
* Clean enum-based type handling
* Proper mapping between DTO and Entity

---

# 🛡 Exception Handling

Custom Exceptions:

* CouponNotFoundException
* InvalidCouponException

Handled globally using @ControllerAdvice.

No raw RuntimeException usage.

---

# ✅ Validation

* Bean validation annotations used in DTOs
* @Valid used in controller layer
* Field-level validation for required inputs

---

# 🧪 Unit Testing

Test Coverage Includes:

* Create coupon
* Get coupon by ID
* Get all coupons
* Update coupon
* Delete coupon
* Cart coupon logic
* Product coupon logic
* BXGY logic
* Expired coupon
* Inactive coupon
* Coupon not found
* Proportional discount distribution
* Repetition limit handling

Mockito used for mocking repository layer.

---

# 🗄 Database

* JPA/Hibernate
* Proper entity relationships
* Enum stored as STRING
* Cascade rules configured correctly

---

# 📦 API Endpoints

| Method | Endpoint       | Description          |
| ------ | -------------- | -------------------- |
| POST   | /coupons       | Create coupon        |
| GET    | /coupons       | Get all coupons      |
| GET    | /coupons/{id}  | Get coupon by ID     |
| PUT    | /coupons/{id}  | Update coupon        |
| DELETE | /coupons/{id}  | Delete coupon        |
| POST   | /coupons/apply | Apply coupon to cart |

---

# 🧩 Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate
* Lombok
* JUnit 5
* Mockito

---

# ▶ How to Run

1. Clone repository

2. Configure database in application.properties

3. Run application using:

   mvn spring-boot:run

4. Test APIs using Postman

---

# 📊 Project Quality Summary

✔ Clean layered architecture
✔ Enum-based type safety
✔ Strong business rule implementation
✔ Custom exception handling
✔ Unit tested service layer
✔ HR-ready backend assignment

---

# 👨‍💻 Author

Tasneem
Backend Developer Candidate

---

# 📌 Notes

This project was built as an assignment to demonstrate backend architecture, clean coding practices, and coupon rule implementation for an e-commerce system.

The system is production-structure ready and can be extended into a microservice environment.
