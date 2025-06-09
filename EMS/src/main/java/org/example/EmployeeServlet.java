package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/employee")
@MultipartConfig
public class EmployeeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();

        String ename = request.getParameter("ename");
        String enumber = request.getParameter("enumber");
        String eaddress = request.getParameter("eaddress");
        String edepartment = request.getParameter("edepartment");
        String estatus = request.getParameter("estatus");

        Part filePart = request.getPart("eimage");
        String originalFileName = filePart.getSubmittedFileName();
        String fileName = UUID.randomUUID() + "_" + originalFileName;

        String uploadPath = "C:\\Lectures\\Batch\\GDSE72\\AAD\\JavaEE\\Work\\EMS-FN\\assets";
        java.io.File uploadDir = new java.io.File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileAbsolutePath = uploadPath + java.io.File.separator + fileName;
        filePart.write(fileAbsolutePath);

        ServletContext sc = request.getServletContext();
        BasicDataSource ds = (BasicDataSource) sc.getAttribute("ds");

        try (Connection connection = ds.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(
                    "INSERT INTO employee (eid, ename, enumber, eaddress, edepartment, estatus, eimage) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            String eid = UUID.randomUUID().toString();

            pstm.setString(1, eid);
            pstm.setString(2, ename);
            pstm.setString(3, enumber);
            pstm.setString(4, eaddress);
            pstm.setString(5, edepartment);
            pstm.setString(6, estatus);
            pstm.setString(7, fileName); // relative path for web access

            int executed = pstm.executeUpdate();

            if (executed > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(out, Map.of(
                        "code", "200",
                        "status", "success",
                        "message", "Employee successfully created!"
                ));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(out, Map.of(
                        "code", "400",
                        "status", "error",
                        "message", "Failed to create employee record."
                ));
            }

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(out, Map.of(
                    "code", "500",
                    "status", "error",
                    "message", "Internal server error."
            ));
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json");

        ServletContext sc = req.getServletContext();
        BasicDataSource ds = (BasicDataSource) sc.getAttribute("ds");

        try (Connection connection = ds.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(
                    "SELECT eid, ename, enumber, eaddress, edepartment, estatus, eimage FROM employee"
            );
            ResultSet rs = pstm.executeQuery();

            List<Map<String, String>> employees = new ArrayList<>();

            while (rs.next()) {
                Map<String, String> emp = new HashMap<>();
                emp.put("eid", rs.getString("eid"));
                emp.put("ename", rs.getString("ename"));
                emp.put("enumber", rs.getString("enumber"));
                emp.put("eaddress", rs.getString("eaddress"));
                emp.put("edepartment", rs.getString("edepartment"));
                emp.put("estatus", rs.getString("estatus"));
                emp.put("eimage", rs.getString("eimage")); // include image path
                employees.add(emp);
            }

            PrintWriter out = resp.getWriter();
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(out, Map.of(
                    "code", "200",
                    "status", "success",
                    "data", employees
            ));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), Map.of(
                    "code", "500",
                    "status", "error",
                    "message", "Internal server error!"
            ));
        }
    }
}
