# 📚 BookStore - Full Stack E-Commerce Application

A complete full-stack e-commerce application for buying and selling books online with JWT authentication, MongoDB database, and a responsive frontend.

## 🏗️ Project Architecture

```
Frontend (UI) → API Calls → Backend (Spring Boot) → Database (MongoDB)
```

## 📋 Features

### 🔐 Authentication

- User registration and login
- JWT token-based authentication
- Role-based access control (ADMIN / USER)
- Secure password encryption

### 📚 Book Management

- View all books with search and filtering
- Browse by category
- Add books (Admin only)
- Update book details (Admin only)
- Delete books (Admin only)

### 🛒 Shopping Cart

- Add books to cart
- Remove items from cart
- Update quantity
- View cart total price
- Clear cart

### 📦 Order Management

- Place orders from cart
- View order history
- Track order status
- Admin can update order status
- Admin can view all orders

## 🛠️ Technology Stack

### Backend

- **Language:** Java 11
- **Framework:** Spring Boot 2.7.14
- **Database:** MongoDB
- **Security:** Spring Security + JWT
- **Build Tool:** Maven

### Frontend

- **HTML5**
- **CSS3**
- **Vanilla JavaScript**
- **Responsive Design**

## 📦 Project Structure

```
book_store/
│
├── src/main/java/com/bookstore/
│   ├── controller/           # API Controllers
│   │   ├── AuthController.java
│   │   ├── BookController.java
│   │   ├── CartController.java
│   │   └── OrderController.java
│   │
│   ├── service/              # Business Logic
│   │   ├── AuthService.java
│   │   ├── BookService.java
│   │   ├── CartService.java
│   │   └── OrderService.java
│   │
│   ├── repository/           # Data Access Layer
│   │   ├── UserRepository.java
│   │   ├── BookRepository.java
│   │   ├── CartRepository.java
│   │   └── OrderRepository.java
│   │
│   ├── model/                # Entity Classes
│   │   ├── User.java
│   │   ├── Book.java
│   │   ├── Cart.java
│   │   ├── CartItem.java
│   │   ├── Order.java
│   │   └── OrderItem.java
│   │
│   ├── security/             # JWT & Security
│   │   ├── JwtUtil.java
│   │   └── JwtFilter.java
│   │
│   ├── config/               # Configuration
│   │   └── SecurityConfig.java
│   │
│   └── BookstoreApplication.java
│
├── src/main/resources/
│   └── application.properties
│
├── frontend/
│   ├── index.html            # Home Page
│   ├── login.html            # Login Page
│   ├── signup.html           # Registration Page
│   ├── books.html            # Books Collection
│   ├── cart.html             # Shopping Cart
│   ├── orders.html           # Order History
│   ├── admin.html            # Admin Panel
│   ├── css/
│   │   └── style.css         # Styling
│   └── js/
│       ├── api.js            # API Calls
│       ├── auth.js           # Authentication
│       ├── book.js           # Book Operations
│       ├── cart.js           # Cart Operations
│       └── order.js          # Order Operations
│
└── pom.xml                   # Maven Dependencies
```

## 🗄️ Database Design

### User Collection

```json
{
  "_id": ObjectId,
  "name": "John Doe",
  "email": "john@example.com",
  "password": "encrypted_password",
  "role": "USER"
}
```

### Book Collection

```json
{
  "_id": ObjectId,
  "title": "Book Title",
  "author": "Author Name",
  "price": 299.99,
  "category": "Fiction",
  "stock": 50,
  "description": "Book description"
}
```

### Cart Collection

```json
{
  "_id": ObjectId,
  "userId": "user_id",
  "items": [
    {
      "bookId": "book_id",
      "title": "Book Title",
      "price": 299.99,
      "quantity": 2
    }
  ]
}
```

### Order Collection

```json
{
  "_id": ObjectId,
  "userId": "user_id",
  "items": [...],
  "totalPrice": 599.98,
  "status": "PENDING",
  "orderDate": "2024-04-28T10:30:00"
}
```

## ⚙️ Setup Instructions

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- MongoDB 4.0+
- Node.js (optional, for frontend testing)

### Backend Setup

1. **Install MongoDB**

   ```bash
   # Windows
   # Download from: https://www.mongodb.com/try/download/community
   # Run the installer and follow the setup wizard

   # Or use Docker
   docker run -d -p 27017:27017 --name mongodb mongo
   ```

2. **Start MongoDB**

   ```bash
   # Windows
   net start MongoDB

   # Or if using Docker
   docker start mongodb
   ```

3. **Clone the project** (or extract the files)

   ```bash
   cd c:\Users\HP\Desktop\book_store
   ```

4. **Configure MongoDB connection**

   Edit `src/main/resources/application.properties`:

   ```properties
   spring.data.mongodb.uri=mongodb://localhost:27017/bookstore
   spring.data.mongodb.database=bookstore
   ```

5. **Build the project**

   ```bash
   mvn clean install
   ```

6. **Run the backend**

   ```bash
   mvn spring-boot:run
   ```

   The backend will be running at `http://localhost:8080`

### Frontend Setup

1. **Open frontend in browser**

   ```
   Open c:\Users\HP\Desktop\book_store\frontend\index.html
   ```

   Or use a local server:

   ```bash
   # Using Python
   python -m http.server 8000 --directory c:\Users\HP\Desktop\book_store\frontend

   # Then visit: http://localhost:8000
   ```

## 🚀 API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user

### Books

- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get book by ID
- `GET /api/books/search/title/{title}` - Search books by title
- `GET /api/books/search/category/{category}` - Filter by category
- `POST /api/books` - Add book (Admin only)
- `PUT /api/books/{id}` - Update book (Admin only)
- `DELETE /api/books/{id}` - Delete book (Admin only)

### Cart

- `GET /api/cart` - Get user cart
- `POST /api/cart/add` - Add to cart
- `DELETE /api/cart/{bookId}` - Remove from cart
- `PUT /api/cart/{bookId}/quantity` - Update quantity
- `DELETE /api/cart/clear` - Clear cart

### Orders

- `POST /api/orders` - Place order
- `GET /api/orders` - Get user orders
- `GET /api/orders/all` - Get all orders (Admin only)
- `GET /api/orders/{orderId}` - Get order details
- `PUT /api/orders/{orderId}/status` - Update order status (Admin only)
- `DELETE /api/orders/{orderId}` - Delete order (Admin only)

## 📝 Sample Data

To test the application, you can add sample books through the admin panel or using the API:

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "price": 299.99,
    "category": "Fiction",
    "stock": 50,
    "description": "A classic American novel"
  }'
```

## 🧪 Testing the Application

1. **Register a user**: Go to signup.html and create an account
2. **Login**: Use your credentials on login.html
3. **Browse books**: View all books on books.html
4. **Add to cart**: Click "Add to Cart" on any book
5. **Checkout**: Go to cart.html and place order
6. **Track order**: View your orders on orders.html
7. **Admin features**: Create an admin account to manage books and orders

## 🔐 Security Features

- **Password Encryption**: Passwords are encrypted using BCrypt
- **JWT Authentication**: Secure token-based authentication
- **CORS Enabled**: Cross-Origin Resource Sharing configured
- **Role-Based Access Control**: Admin and User roles
- **Input Validation**: All inputs are validated

## 🐛 Troubleshooting

### MongoDB Connection Issues

```
Error: connect ECONNREFUSED
Solution: Ensure MongoDB is running on localhost:27017
```

### CORS Errors

```
Error: Access to XMLHttpRequest blocked by CORS policy
Solution: CORS is enabled in SecurityConfig.java for all origins
```

### Port Already in Use

```
Error: Address already in use: bind
Solution: Change the port in application.properties or kill the process using port 8080
```

## 📚 Learning Concepts Covered

- **Object-Oriented Programming (OOP)**: Classes, Inheritance, Encapsulation
- **Database Management**: MongoDB, Collections, Relationships
- **Web Development**: REST APIs, Client-Server Architecture
- **Cybersecurity**: Authentication, Authorization, Encryption
- **Software Engineering**: MVC Pattern, Layered Architecture
- **Data Structures**: Lists, Collections

## 📄 License

This project is open source and available under the MIT License.

## 👨‍💻 Author

Created as a full-stack e-commerce learning project.

## 📞 Support

For issues or questions, please refer to the Spring Boot and MongoDB documentation.

---

Happy Coding! 🎉
