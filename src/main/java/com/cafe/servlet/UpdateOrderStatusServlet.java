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

@WebServlet("/update-order-status")
public class UpdateOrderStatusServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String status = request.getParameter("status");

        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, status);
            ps.setInt(2, Integer.parseInt(id));

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {

                response.getWriter().print(
                    "{\"success\":true}"
                );

            } else {

                response.getWriter().print(
                    "{\"success\":false}"
                );
            }

        } catch (Exception e) {

            response.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            String errorMessage = e.getMessage();

            if (errorMessage == null) {
                errorMessage = "Unknown error";
            }

            errorMessage = errorMessage
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\r", "")
                    .replace("\n", "");

            response.getWriter().print(
                "{\"success\":false,\"error\":\""
                + errorMessage
                + "\"}"
            );
        }
    }
}

