package com.vcarrin87.jdbi_example.constants;

public final class SqlConstants {

    // CUSTOMERS
    // Note: The customers table is assumed to have a foreign key relationship with the orders and the payments tables
    public static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM customers";
    public static final String SELECT_CUSTOMER_BY_ID = "SELECT * FROM customers WHERE customer_id = :customer_id";
    public static final String INSERT_CUSTOMER = 
        "INSERT INTO customers (name, email, address) VALUES (:name, :email, :address)";
    public static final String UPDATE_CUSTOMER = 
        "UPDATE customers SET name = :name, email = :email, address = :address WHERE customer_id = :customer_id";
    public static final String DELETE_CUSTOMER_BY_ID = "DELETE FROM customers WHERE customer_id = :customer_id";  

    // PRODUCTS
    // Note: The products table is assumed to have a foreign key relationship with the inventory and the order_items tables
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

    public static final String SELECT_PRODUCTS_WITH_ORDER_ITEMS =
        "SELECT p.product_id p_product_id, " + 
        "p.name p_name, " +
        "p.description p_description, " +
        "p.price p_price, " +
        "oi.product_id oi_product_id, " +
        "oi.quantity oi_quantity, " +   
        "oi.price oi_price, " +
        "oi.order_id oi_order_id, " +
        "oi.orderitem_id oi_order_item_id " +
        "FROM products p " +
        "LEFT JOIN order_items oi ON p.product_id = oi.product_id";

    // ORDERS
    // Note: The orders table is assumed to have a foreign key relationship with the customers, payments and the order_items tables
    public static final String SELECT_ALL_ORDERS = "SELECT * FROM orders";
    public static final String SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE order_id = :order_id";
    public static final String INSERT_ORDER = 
        "INSERT INTO orders (customer_id, order_status, delivery_date) VALUES (:customer_id, :order_status, :delivery_date)";
    public static final String UPDATE_ORDER = 
        "UPDATE orders SET customer_id = :customer_id, order_date = :order_date WHERE order_id = :order_id";
    public static final String DELETE_ORDER = "DELETE FROM orders WHERE order_id = :order_id";
    public static final String SELECT_ALL_ORDER_ITEMS = "SELECT * FROM order_items"; 

    // ORDER ITEMS
    // Note: The order_items table is assumed to have a foreign key relationship with the orders and the products tables
    public static final String INSERT_ORDER_ITEM = 
        "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (:order_id, :product_id, :quantity, :price)";
    public static final String DELETE_ORDER_ITEMS_BY_ORDER_ID = "DELETE FROM order_items WHERE order_id = :order_id";
    public static final String DELETE_ORDER_ITEMS_BY_PRODUCT_ID = "DELETE FROM order_items WHERE product_id = :product_id";
    
    // PAYMENTS
    // Note: The payments table is assumed to have a foreign key relationship with the orders table
    public static final String SELECT_ALL_PAYMENTS = "SELECT * FROM payments";
    public static final String SELECT_PAYMENT_BY_ID = "SELECT * FROM payments WHERE payment_id = :payment_id";
    public static final String INSERT_PAYMENT = 
        "INSERT INTO payments (order_id, amount, payment_date, payment_method) VALUES (:order_id, :amount, :payment_date, :payment_method)";
    public static final String UPDATE_PAYMENT = 
        "UPDATE payments SET order_id = :order_id, amount = :amount, payment_date = :payment_date, payment_method = :payment_method WHERE payment_id = :payment_id";
    public static final String DELETE_PAYMENT = "DELETE FROM payments WHERE payment_id = :payment_id";
    public static final String DELETE_PAYMENTS_BY_ORDER_ID = "DELETE FROM payments WHERE order_id = :order_id";

    // INVENTORY
    public static final String SELECT_ALL_INVENTORY = "SELECT * FROM inventory";
    public static final String UPDATE_INVENTORY = "UPDATE inventory SET stock_level = stock_level + :delta WHERE product_id = :product_id";
    public static final String SELECT_INVENTORY_BY_PRODUCT_ID = "SELECT * FROM inventory WHERE product_id = :product_id";
    public static final String INSERT_INVENTORY = 
        "INSERT INTO inventory (product_id, stock_level) VALUES (:product_id, :stock_level)";
    public static final String DELETE_INVENTORY_BY_PRODUCT_ID = "DELETE FROM inventory WHERE product_id = :product_id";

    private SqlConstants() {
        // Prevent instantiation
    }
}
