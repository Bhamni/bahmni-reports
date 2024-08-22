package org.bahmni.reports.web.security;

import org.bahmni.reports.BahmniReportsProperties;
import org.bahmni.reports.model.Report;
import org.bahmni.reports.model.Reports;
import org.bahmni.webclients.HttpClient;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static org.bahmni.reports.web.security.AuthenticationFilter.REPORTING_COOKIE_NAME;

public class ReportAuthorization {

    private List<String> userPrivileges;
    private BahmniReportsProperties bahmniReportsProperties;
    private HttpClient httpClient;

    public ReportAuthorization(HttpServletRequest request,
                               OpenMRSAuthenticator openMRSAuthenticator,
                               BahmniReportsProperties bahmniReportsProperties,
                               HttpClient httpClient) {
        this.bahmniReportsProperties = bahmniReportsProperties;
        this.httpClient = httpClient;
        String sessionId = getSessionId(request);
        Privileges privileges = openMRSAuthenticator.callOpenMRS(sessionId).getBody();
        userPrivileges = new ArrayList();
        privileges.forEach(a -> userPrivileges.add(a.getName()));
    }

    private String getSessionId(HttpServletRequest request) {
        String sessionId = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REPORTING_COOKIE_NAME)) {
                sessionId = cookie.getValue();
            }
        }
        return sessionId;
    }


    public boolean hasPrivilege(String reportName) throws IOException, URISyntaxException {
        Report report = Reports.find(reportName, bahmniReportsProperties.getConfigFileUrl(), httpClient);
        return isNull(report.getRequiredPrivilege()) || userPrivileges.contains(report.getRequiredPrivilege());
    }
}
