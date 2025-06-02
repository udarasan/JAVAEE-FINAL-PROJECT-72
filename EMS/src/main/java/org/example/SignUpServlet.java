package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> user =
                mapper.readValue(request.getInputStream(), Map.class);

        ServletContext sc = request.getServletContext();
        BasicDataSource ds= (BasicDataSource) sc.getAttribute("ds");
        try {
            Connection connection=ds.getConnection();
            PreparedStatement pstm=connection.prepareStatement
                    ("INSERT INTO systemusers (uid,uname,upassword,uemail) VALUES (?,?,?,?)");
            pstm.setString(1, UUID.randomUUID().toString());
            pstm.setString(2,user.get("uname"));
            pstm.setString(3,user.get("upassword"));
            pstm.setString(4,user.get("uemail"));
            int executed = pstm.executeUpdate();
            if (executed > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            }
            connection.close();
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }
}