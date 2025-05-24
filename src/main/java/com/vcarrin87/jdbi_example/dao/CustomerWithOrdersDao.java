package com.vcarrin87.jdbi_example.dao;

import java.util.stream.Stream;

import org.jdbi.v3.core.mapper.JoinRow;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterJoinRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

import com.vcarrin87.jdbi_example.models.Customer;
import com.vcarrin87.jdbi_example.models.Orders;
import com.vcarrin87.jdbi_example.models.Payments;
import com.vcarrin87.jdbi_example.reducers.CustomerOrderPaymentReducer;

public interface CustomerWithOrdersDao {

    /**
     * This method retrieves one customer with their orders.
     * It uses a join query to fetch the data from the customers and orders tables.
     *
     * @return a stream of JoinRow objects containing customer and order data
     */
    @RegisterJoinRowMapper({Customer.class, Orders.class})
     @SqlQuery("""      
        SELECT 
        c.customer_id c_customer_id, 
        c.name c_name, 
        c.email c_email, 
        c.address c_address, 
        o.order_id o_order_id, 
        o.customer_id o_customer_id, 
        o.order_status o_order_status,
        o.delivery_date o_delivery_date 
        FROM customers c 
        JOIN orders o ON c.customer_id = o.customer_id
        WHERE c.customer_id = :customerId
    """)
    @RegisterBeanMapper(value = Customer.class, prefix = "c")
    @RegisterBeanMapper(value = Orders.class, prefix = "o")
    Stream<JoinRow> getCustomerWithOrders(@Bind("customerId") int customerId);

    /**
     * This method retrieves one customer with their orders and payments.
     * It uses a join query to fetch the data from the customers, orders, and payments tables.          
     *  
     * @param customerId the ID of the customer to retrieve             
     * @return a Customer object containing the customer, orders, and payments data
     */
    @SqlQuery("""
        SELECT 
        c.customer_id c_customer_id,
        c.name c_name,
        c.email c_email,
        c.address c_address,
        o.order_id o_order_id,
        o.customer_id o_customer_id,
        o.order_status o_order_status,
        o.delivery_date o_delivery_date,
        p.payment_id p_payment_id,
        p.order_id p_order_id,
        p.amount p_amount,
        p.payment_date p_payment_date,
        p.payment_method p_payment_method
        FROM customers c
        LEFT JOIN orders o ON c.customer_id = o.customer_id
        LEFT JOIN payments p ON o.order_id = p.order_id
        WHERE c.customer_id = :customerId      
    """)
    @RegisterBeanMapper(value = Customer.class, prefix = "c")
    @RegisterBeanMapper(value = Orders.class, prefix = "o")
    @RegisterBeanMapper(value = Payments.class, prefix = "p")
    @UseRowReducer(CustomerOrderPaymentReducer.class)
    Customer getCustomerWithOrdersAndPayments(@Bind("customerId") int customerId);     
}
