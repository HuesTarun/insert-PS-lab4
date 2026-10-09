package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/add")
public class insertdata extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Read input values from your HTML form
        String name = req.getParameter("name");
        String marksStr = req.getParameter("marks"); // Reading marks as text from form

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            // Convert the marks string to an integer to match the SQL database column type
            int marks = Integer.parseInt(marksStr);

            // 2. Load the MySQL Database Connection Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 3. Establish the database handshake
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "2210231@iO9");

            // 4. SQL Statement targeting your actual table: csef
            String sql = "INSERT INTO csef (name, marks) VALUES (?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, marks); // Setting the int value for marks column

            int rows = ps.executeUpdate();

            out.println("<html><body>");
            if (rows > 0) {
                out.println("<h2>Student Added Successfully</h2>");
            } else {
                out.println("<h2>Student Not Added</h2>");
            }
            out.println("<br><a href='index.html'>Go Back</a>");
            out.println("</body></html>");

            // Clean up connections
            ps.close();
            con.close();

        } catch (NumberFormatException e) {
            out.println("<h2 style='color:red;'>Error: Please enter a valid number for marks.</h2>");
        } catch (Exception e) {
            out.println("<h2 style='color:red;'>Database Error: " + e.getMessage() + "</h2>");
            e.printStackTrace();
        }
    }
}
