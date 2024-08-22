package org.bahmni.reports.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration.Dynamic;

import org.bahmni.reports.web.security.SecurityConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
public class Initializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(SecurityConfig.class);
        ctx.setServletContext(servletContext);
        Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        dynamic.addMapping("/");
        dynamic.setLoadOnStartup(1);
    }
}