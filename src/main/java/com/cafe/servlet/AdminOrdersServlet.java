package com.cafe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cafe.DBConnection;

@WebServlet("/admin-orders")
public class AdminOrdersServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String sql = "SELECT id, customer_name, customer_phone, item_name, quantity, total_price, order_date, status "
           + "FROM orders ORDER BY id DESC";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            out.print("[");

            boolean first = true;

            while (rs.next()) {

                if (!first) {
                    out.print(",");
                }

                out.print("{");
                out.print("\"id\":" + rs.getInt("id") + ",");
                out.print("\"customer\":\"" + escape(rs.getString("customer_name")) + "\",");
                out.print("\"phone\":\"" + escape(rs.getString("customer_phone")) + "\",");
                out.print("\"item\":\"" + escape(rs.getString("item_name")) + "\",");
                out.print("\"quantity\":" + rs.getInt("quantity") + ",");
                out.print("\"total\":" + rs.getBigDecimal("total_price") + ",");
                out.print("\"date\":\"" + escape(String.valueOf(rs.getTimestamp("order_date"))) + "\",");
                out.print("\"status\":\"" + escape(rs.getString("status")) + "\"");
                out.print("}");
            

                first = false;
            }

            out.print("]");

        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            out.print("{\"error\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}