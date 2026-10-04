package com.cafe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cafe.DBConnection;

@WebServlet("/cafe")
public class CafeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>NESLURA Cafe</title></head>");
        out.println("<body>");

        try {
            Connection connection = DBConnection.getConnection();

            out.println("<h1>NESLURA Cafe</h1>");
            out.println("<h2>MySQL Connected Successfully!</h2>");

            connection.close();

        } catch (Exception e) {

            out.println("<h1>MySQL Connection Failed</h1>");
            out.println("<p>" + e.getMessage() + "</p>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}