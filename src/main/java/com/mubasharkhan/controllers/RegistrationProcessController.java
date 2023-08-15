package com.mubasharkhan.controllers;

import com.mubasharkhan.config.MysqlJDBCConfig;
import com.mubasharkhan.config.ThymeleafConfig;
import com.mubasharkhan.dao.UserDao;
import com.mubasharkhan.models.UserModel;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet(name = "RegistrationProcessController", value = "/registrationprocess")
public class RegistrationProcessController extends HttpServlet {
    private TemplateEngine templateEngine;
    private JakartaServletWebApplication application;
    private UserDao userDao;

    public void init() {
        application = JakartaServletWebApplication.buildApplication(getServletContext());
        templateEngine = ThymeleafConfig.getTemplateEngine(application);
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        final IWebExchange webExchange = application.buildExchange(request, response);
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        UserModel user = new UserModel();
        user.setUsername(request.getParameter("username"));
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        user.setAgree(request.getParameter("terms") != null ? true : false);

        if (!user.getAgree()) {
            response.sendRedirect("register?error=Please agree to the terms and conditions");
            return;
        }

        try (Connection connection = MysqlJDBCConfig.getConnection()) {

            Boolean userExists = userDao.checkIfEmailExist(connection, user.getEmail());

            if (userExists) {
                response.sendRedirect("register?error=Email already exists");
                return;
            }

            boolean success = userDao.insert(connection, user);

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO user");
            sql.append("(username, email, password, first_name, last_name, agree)");
            sql.append("VALUES");
            sql.append("(?, ?, ?, ?, ?, ?)");

            if (success) {
                response.sendRedirect("register?success=Registration successful!");
            } else {
                response.sendRedirect("register?error=Registration failed");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed " + e.getMessage());
        }
    }
}
