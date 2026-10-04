package com.cafe.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cafe.DBConnection;

@WebServlet("/place-order")
public class OrderServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        String itemName = request.getParameter("itemName");
        String quantity = request.getParameter("quantity");
        String totalPrice = request.getParameter("totalPrice");

        String sql = "INSERT INTO orders " +
                "(customer_name, customer_phone, item_name, quantity, total_price) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(
                            sql,
                            java.sql.Statement.RETURN_GENERATED_KEYS
                    )
        ) {

            statement.setString(1, customerName);
            statement.setString(2, customerPhone);
            statement.setString(3, itemName);
            statement.setInt(4, Integer.parseInt(quantity));
            statement.setBigDecimal(
                    5,
                    new java.math.BigDecimal(totalPrice)
            );

            int rowsInserted = statement.executeUpdate();

            int orderId = 0;

            if (rowsInserted > 0) {

                try (
                    var generatedKeys =
                            statement.getGeneratedKeys()
                ) {

                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                    }
                }
            }

            response.getWriter().println(
                    "Order placed successfully! Order ID: " + orderId
            );

        } catch (Exception e) {

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().println(
                    "Error placing order: " + e.getMessage()
            );
        }
    }
}