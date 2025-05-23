package com.vcarrin87.jdbi_example.constants;

public final class SqlConstants {

    public static final String SELECT_ALL_PRODUCTS = "SELECT * FROM products";
    public static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM products WHERE product_id = :product_id";
    public static final String INSERT_PRODUCT = 
        "INSERT INTO products (name, description, price) VALUES (:name, :description, :price)";
    public static final String UPDATE_PRODUCT = 
        "UPDATE products SET name = :name, description = :description, price = :price WHERE product_id = :product_id";
    public static final String DELETE_PRODUCT = "DELETE FROM products WHERE product_id = :product_id";
    public static final String SELECT_PRODUCTS_IN_STOCK = 
        "SELECT p.product_id p_product_id, " +
        "p.name p_name, " +
        "p.description p_description, " +
        "p.price p_price, " +
        "i.product_id i_product_id, " +
        "i.stock_level i_stock_level " +
        "FROM products p INNER JOIN inventory i ON p.product_id = i.product_id WHERE i.stock_level > 0";

    public static final String SELECT_ALL_ORDERS = "SELECT * FROM orders";
    public static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE order_id = :order_id";
    public static final String INSERT_ORDER = 
        "INSERT INTO orders (customer_id, order_status, delivery_date) VALUES (:customer_id, :order_status, :delivery_date)";
    public static final String UPDATE_ORDER = 
        "UPDATE orders SET customer_id = :customer_id, order_date = :order_date WHERE order_id = :order_id";
    public static final String DELETE_ORDER = "DELETE FROM orders WHERE order_id = :order_id";
    public static final String SELECT_ALL_ORDER_ITEMS = "SELECT * FROM order_items";

    public static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM customers";
    public static final String SELECT_CUSTOMER_BY_ID = "SELECT * FROM customers WHERE customer_id = :customer_id";
    public static final String INSERT_CUSTOMER = 
        "INSERT INTO customers (name, email, address) VALUES (:name, :email, :address)";
    public static final String UPDATE_CUSTOMER = 
        "UPDATE customers SET name = :name, email = :email, address = :address WHERE customer_id = :customer_id";
    public static final String DELETE_CUSTOMER_BY_ID = "DELETE FROM customers WHERE customer_id = :customer_id";    

    public static final String SELECT_CUSTOMER_WITH_ORDERS_AND_PAYMENTS =
        "SELECT c.customer_id c_customer_id, " + 
        "c.name c_name, " + 
        "c.email c_email, " + 
        "c.address c_address, " + 
        "o.order_id o_order_id, " + 
        "o.customer_id o_customer_id, " + 
        "o.order_status o_order_status, " + 
        "o.delivery_date o_delivery_date, " + 
        "p.payment_id p_payment_id, " + 
        "p.order_id p_order_id, " + 
        "p.amount p_amount, " + 
        "p.payment_date p_payment_date, " + 
        "p.payment_method p_payment_method " + 
        "FROM customers c " + 
        "LEFT JOIN orders o ON c.customer_id = o.customer_id " + 
        "LEFT JOIN payments p ON o.order_id = p.order_id " + 
        "WHERE c.customer_id = :customerId ";

    private SqlConstants() {
        // Prevent instantiation
    }
}
