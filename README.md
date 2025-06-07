# JDBI Example Project

This project is a demonstration how to use Java with PostgreSQL with JDBI and Liquibase

* JDBI is a high-level API built on top of JDBC. It simplifies database interactions by reducing boilerplate code and providing a more user-friendly and declarative approach to database operations. 
* The project connects to PostgreSQL with Liquibase. 
* During testing, it connects to H2 in-memory database with test data to run unit tests.

# Getting Started

## Reducers

Jdbi reducers are a feature that helps you efficiently map SQL query results into complex object graphs, such as a Customer with their Orders and each Order's Payments.

Customer: Has many Orders.
Order: Has many Payments.

You want to fetch a Customer with all their Orders, and for each Order, all its Payments.

#### How Jdbi Reducers Work
* Single SQL Query: Write a SQL query that joins customers, orders, and payments.
* Row Mapping: Each row in the result set contains data for a customer, an order, and a payment.
* Reducer: Jdbi's reducer collects rows and assembles them into the nested object structure.

## Transactions/Rollbacks

When deleting a Customer that has related orders with payments or an Order that has related payments and order items or Products that has related order items, we use Jdbi useTransaction, which is used to execute a block of code within a database transaction. If any exception is thrown inside the block, the transaction is rolled back automatically; otherwise, it is committed. 
* See example: src/main/java/com/vcarrin87/jdbi_example/services/CustomerService.java #deleteCustomer()
* See example: src/main/java/com/vcarrin87/jdbi_example/services/OrdersService.java #deleteOrders()

### Swagger-UI

Swagger UI provides a web-based interface to visualize and interact with the API's endpoints.
To access Swagger UI:
1. Start the application server.
2. Open a web browser.
3. Navigate to the Swagger UI URL, typically in the format: `http://<server_address>:8090/swagger-ui/index.html`.
4. Use the interface to explore available API endpoints, view request/response schemas, and test API calls.

![swagger-ui](src/main/resources/static/swagger-ui.png)

### Database ER Diagram
![er-diagram](src/main/resources/static/er-diagram.png)

## Testing
### Run all tests
```bash
    ./mvnw test
```

### Run single test suite
```bash
    ./mvnw -Dtest=<Test_Name> test
```

### Run single test
```bash
    ./mvnw -Dtest=<Test_Name>#<method_name> test
```

