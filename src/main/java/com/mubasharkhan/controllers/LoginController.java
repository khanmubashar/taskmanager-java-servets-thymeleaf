package com.mubasharkhan.controllers;

import com.mubasharkhan.config.ThymeleafConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.Calendar;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {
    private TemplateEngine templateEngine;
    private JakartaServletWebApplication application;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        application = JakartaServletWebApplication.buildApplication(config.getServletContext());
        templateEngine = ThymeleafConfig.getTemplateEngine(application);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        final IWebExchange webExchange = application.buildExchange(request, response);
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        ctx.setVariable("today", Calendar.getInstance());
        ctx.setVariable("message", "Hello hh, Thymeleaf!");
        templateEngine.process("login", ctx, response.getWriter());
    }
}
