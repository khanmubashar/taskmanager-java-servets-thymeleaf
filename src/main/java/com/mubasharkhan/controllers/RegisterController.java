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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

@WebServlet(name = "RegisterController", value = "/register")
public class RegisterController extends HttpServlet {
    private TemplateEngine templateEngine;
    private JakartaServletWebApplication application;

    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        application = JakartaServletWebApplication.buildApplication(config.getServletContext());
        templateEngine = ThymeleafConfig.getTemplateEngine(application);
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);


        final IWebExchange webExchange = application.buildExchange(request, response);
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());

        String success = request.getParameter("success");
        if(success != null) {
            ctx.setVariable("success", success);
        }

        String error = request.getParameter("error");
        if(error != null) {
            ctx.setVariable("error",error);
        }
        templateEngine.process("register", ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }


}
