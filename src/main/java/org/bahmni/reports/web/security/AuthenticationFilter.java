package org.bahmni.reports.web.security;

import org.bahmni.reports.BahmniReportsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component(value = "authenticationFilter")
public class AuthenticationFilter implements HandlerInterceptor {

    public static final String REPORTING_COOKIE_NAME = "reporting_session";
    private OpenMRSAuthenticator authenticator;
    private BahmniReportsProperties properties;

    @Autowired
    public AuthenticationFilter(OpenMRSAuthenticator authenticator,
                                BahmniReportsProperties properties) {
        this.authenticator = authenticator;
        this.properties = properties;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Reports application cannot handle url " + request.getRequestURI());
            return false;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return redirectToLogin(request, response);
        }
        AuthenticationResponse authenticationResponse = AuthenticationResponse.NOT_AUTHENTICATED;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REPORTING_COOKIE_NAME)) {
                authenticationResponse = authenticator.authenticate(cookie.getValue());
            }
        }

        switch (authenticationResponse) {
            case AUTHORIZED:
                return true;
            case UNAUTHORIZED:
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Privileges is required to access reports");
                return false;
            default:
                return redirectToLogin(request, response);
        }
    }

    private boolean redirectToLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        httpServletResponse.getWriter().write("Please login to continue");

        StringBuffer redirectUrl = new StringBuffer();
        redirectUrl.append(properties.getBahmniLoginUrl());
        char paramChar = '?';
        if(redirectUrl.toString().contains("?")) {
            paramChar = '&';
        }
        redirectUrl.append(paramChar)
                .append("from=")
                .append(URLEncoder.encode(httpServletRequest.getRequestURL() + "?" + httpServletRequest.getQueryString(), "UTF-8"));
        httpServletResponse.sendRedirect(redirectUrl.toString());
        return false;
    }
}
