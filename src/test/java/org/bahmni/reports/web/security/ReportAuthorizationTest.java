package org.bahmni.reports.web.security;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.bahmni.reports.BahmniReportsProperties;
import org.bahmni.reports.model.Report;
import org.bahmni.reports.model.Reports;
import org.bahmni.webclients.HttpClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@Ignore
@PowerMockIgnore("jakarta.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(Reports.class)
public class ReportAuthorizationTest {

    private static final String SESSION_ID = "sessionId";
    private ReportAuthorization reportAuthorization;

    @Mock
    private OpenMRSAuthenticator openMRSAuthenticator;

    @Mock
    private HttpServletRequest request;

    @Mock
    private BahmniReportsProperties bahmniReportsProperties;

    @Mock
    private HttpClient httpClient;

    @Before
    public void setUp() {
        String cookieName = "reporting_session";
        Cookie cookie = new Cookie(cookieName, SESSION_ID);
        Cookie[] cookies = new Cookie[]{cookie};
        when(request.getCookies()).thenReturn(cookies);
    }

    @Test
    public void shouldInvokeCallOpenMRSWithGivenSessionId() {
        Privileges privileges = mock(Privileges.class);
        when(openMRSAuthenticator.callOpenMRS(SESSION_ID)).thenReturn(ResponseEntity.ok(privileges));

        new ReportAuthorization(request, openMRSAuthenticator, bahmniReportsProperties, httpClient);

        verify(openMRSAuthenticator).callOpenMRS(SESSION_ID);
    }

    @Test
    public void shouldSetGivenPrivilegesAfterObjectCreation() throws IllegalAccessException {
        Privileges privileges = new Privileges();
        Privilege privilege = new Privilege();
        String privilegeName = "privilege";
        FieldUtils.writeField(privilege, "name", privilegeName, true);
        privileges.add(privilege);
        when(openMRSAuthenticator.callOpenMRS(SESSION_ID)).thenReturn(ResponseEntity.ok(privileges));

        reportAuthorization = new ReportAuthorization(request, openMRSAuthenticator, bahmniReportsProperties, httpClient);
        List userPrivileges = (List) FieldUtils.getDeclaredField(reportAuthorization.getClass(),
                "userPrivileges", true)
                .get(reportAuthorization);

        assertEquals(Collections.singletonList(privilegeName), userPrivileges);
    }

    @Test
    public void shouldReturnTrueIfUserHaveTheGivenReportPrivilege() throws Exception {
        Privileges privileges = mock(Privileges.class);
        when(openMRSAuthenticator.callOpenMRS(SESSION_ID)).thenReturn(ResponseEntity.ok(privileges));
        reportAuthorization = new ReportAuthorization(request, openMRSAuthenticator, bahmniReportsProperties, httpClient);
        String privilegeName = "privilege";
        FieldUtils.writeField(reportAuthorization, "userPrivileges",
                Collections.singletonList(privilegeName), true);
        Report report = mock(Report.class);
        when(report.getRequiredPrivilege()).thenReturn("privilege");
        mockStatic(Reports.class);
        when(Reports.find(any(), any(), any())).thenReturn(report);

        boolean hasPrivilege = reportAuthorization.hasPrivilege("reportName");

        assertTrue(hasPrivilege);
    }

    @Test
    public void shouldReturnFalseIfUserHaveTheGivenReportPrivilege() throws Exception {
        Privileges privileges = mock(Privileges.class);
        when(openMRSAuthenticator.callOpenMRS(SESSION_ID)).thenReturn(ResponseEntity.ok(privileges));
        reportAuthorization = new ReportAuthorization(request, openMRSAuthenticator, bahmniReportsProperties, httpClient);
        FieldUtils.writeField(reportAuthorization, "userPrivileges",
                Collections.singletonList("userPrivilege"), true);
        Report report = mock(Report.class);
        when(report.getRequiredPrivilege()).thenReturn("otherPrivilege");
        mockStatic(Reports.class);
        when(Reports.find(any(), any(), any())).thenReturn(report);

        boolean hasPrivilege = reportAuthorization.hasPrivilege("reportName");

        assertFalse(hasPrivilege);
    }

    @Test
    public void shouldReturnTrueIfReportHasNoPrivilege() throws Exception {
        Privileges privileges = mock(Privileges.class);
        when(openMRSAuthenticator.callOpenMRS(SESSION_ID)).thenReturn(ResponseEntity.ok(privileges));
        reportAuthorization = new ReportAuthorization(request, openMRSAuthenticator, bahmniReportsProperties, httpClient);
        FieldUtils.writeField(reportAuthorization, "userPrivileges",
                Collections.singletonList("userPrivilege"), true);
        Report report = mock(Report.class);
        when(report.getRequiredPrivilege()).thenReturn(null);
        mockStatic(Reports.class);
        when(Reports.find(any(), any(), any())).thenReturn(report);

        boolean hasPrivilege = reportAuthorization.hasPrivilege("reportName");

        assertTrue(hasPrivilege);
    }
}