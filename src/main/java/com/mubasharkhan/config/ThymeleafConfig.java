package com.mubasharkhan.config;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

public class ThymeleafConfig {
    private static TemplateEngine templateEngine;
    private ThymeleafConfig() {
    }

    public static synchronized TemplateEngine getTemplateEngine(JakartaServletWebApplication webApplication) {
        if(templateEngine == null) {
           templateEngine = intializeTemplateEngine(webApplication);
        }

        return templateEngine;
    }

    private static TemplateEngine intializeTemplateEngine(JakartaServletWebApplication webApplication) {

        final WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(webApplication);

        // HTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // This will convert "home" to "/WEB-INF/templates/home.html"
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by LRU
        templateResolver.setCacheTTLMs(Long.valueOf(3600000L));

        // Cache is set to true by default. Set to false if you want templates to
        // be automatically updated when modified.
        templateResolver.setCacheable(true);

        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }

}
