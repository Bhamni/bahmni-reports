package org.bahmni.reports.report.integrationtests;

import net.sf.dynamicreports.jasper.builder.JasperConcatenatedReportBuilder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bahmni.reports.BahmniReportsProperties;
import org.bahmni.reports.filter.JasperResponseConverter;
import org.bahmni.reports.model.AllDatasources;
import org.bahmni.reports.template.BaseReportTemplate;
import org.bahmni.reports.web.MainReportController;
import org.bahmni.reports.web.ReportParams;
import org.bahmni.reports.web.security.OpenMRSAuthenticator;
import org.bahmni.reports.web.security.Privileges;
import org.bahmni.reports.web.security.Privilege;
import org.bahmni.reports.wrapper.CsvReport;
import org.bahmni.webclients.HttpClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.sql.*;
import java.util.List;
import java.util.Properties;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@SkipBaseSetup
public class BaseIntegrationTest extends BaseContextSensitiveTest {

    protected MockMvc mockMvc;

    @Mock
    private OpenMRSAuthenticator openMRSAuthenticator;

    @Mock
    protected HttpClient httpClient;

    @Mock
    private JasperResponseConverter jasperResponseConverter;

    @Mock
    protected BahmniReportsProperties bahmniReportsProperties;

    @Mock
    private AllDatasources allDatasources;

    private BahmniReportsProperties dbProperties;

    @InjectMocks
    private MainReportController controller;

    private String configFileUrl = "src/test/resources/config/reports.json";

    public BaseIntegrationTest(String configFilePath) {
        this.configFileUrl = configFilePath;
    }

    public BaseIntegrationTest() {
    }

    @Before
    public void beforeBaseIntegrationTest() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        when(bahmniReportsProperties.getConfigFileUrl()).thenReturn(configFileUrl);
        when(bahmniReportsProperties.getOpenmrsRootUrl()).thenReturn(dbProperties.getOpenmrsRootUrl());
        when(bahmniReportsProperties.getOpenmrsServiceUser()).thenReturn(dbProperties.getOpenmrsServiceUser());
        when(bahmniReportsProperties.getOpenmrsServicePassword()).thenReturn(dbProperties.getOpenmrsServicePassword());
        when(bahmniReportsProperties.getOpenmrsConnectionTimeout()).thenReturn(dbProperties.getOpenmrsConnectionTimeout());
        when(bahmniReportsProperties.getOpenmrsReplyTimeout()).thenReturn(dbProperties.getOpenmrsReplyTimeout());
        when(bahmniReportsProperties.getMacroTemplatesTempDirectory()).thenReturn("/tmp");
        when(allDatasources.getConnectionFromDatasource(any(BaseReportTemplate.class))).thenReturn(getDatabaseConnection());

        String fileData=FileUtils.readFileToString(new File(configFileUrl));
        when(httpClient.get(any(URI.class))).thenReturn(fileData);
        setUpTestData();
        Context.authenticate("admin", "test");

        Privileges privileges = new Privileges();
        Privilege privilege = new Privilege();
        String privilegeName = "privilege";
        FieldUtils.writeField(privilege, "name", privilegeName, true);
        privileges.add(privilege);
        when(openMRSAuthenticator.callOpenMRS("")).thenReturn(ResponseEntity.ok(privileges));
    }

    private void setUpTestData() throws Exception {
        deleteAllData();
        if (!Context.isSessionOpen()) {
            Context.openSession();
        }
        executeDataSet("datasets/initialTestDataSet.xml");
        executeDataSet("datasets/testDataSet.xml");
        getConnection().commit();
        Context.clearSession();
    }

    @Override
    public Boolean useInMemoryDatabase() {
        return false;
    }

    @Override
    public Properties getRuntimeProperties() {
        dbProperties = new BahmniReportsProperties("bahmni-reports-test.properties");
        Properties properties = new Properties();
        properties.put("connection.url", dbProperties.getOpenmrsUrl());
        properties.put("connection.username", dbProperties.getOpenmrsUser());
        properties.put("connection.password", dbProperties.getOpenmrsPassword());
        return properties;
    }

    protected Connection getDatabaseConnection() {
        try {
            Connection connection = DriverManager.getConnection(dbProperties.getOpenmrsUrl(),
                    dbProperties.getOpenmrsUser(), dbProperties.getOpenmrsPassword());
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected CsvReport fetchCsvReport(String reportName, String startDate, String endDate) throws Exception {
        return fetchCsvReport(reportName, startDate, endDate, false);
    }

    protected CsvReport fetchCsvReport(String reportName, String startDate, String endDate, boolean ignoreStatusCheck) throws Exception {
        MvcResult mvcResult = fetchMvcResult(reportName, startDate, endDate, "text/csv", ignoreStatusCheck);
        String enc = "utf-8";
        MockHttpServletResponse response = mvcResult.getResponse();
        response.setCharacterEncoding(enc);
        String result = response.getContentAsString();
        return CsvReport.getReport(result, response.getErrorMessage());
    }

    protected XSSFWorkbook fetchXlsReport(String reportName, String startDate, String endDate) throws Exception {
        MvcResult mvcResult = fetchMvcResult(reportName, startDate, endDate, "application/vnd.ms-excel", false);
        XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray()));
        return workbook;
    }

    protected MvcResult fetchMvcResult(String reportName, String startDate, String endDate, String responseType, boolean ignoreStatusCheck) throws Exception {
        getConnection().commit();
        doCallRealMethod().when(jasperResponseConverter).applyReportTemplates(any(List.class),
                any(String.class));
        doCallRealMethod().when(jasperResponseConverter).applyHttpHeaders(
                any(String.class), any(HttpServletResponse.class), anyString());
        doCallRealMethod().when(jasperResponseConverter).convertToResponseType(any(ReportParams.class), anyString(), any(OutputStream.class), any(JasperConcatenatedReportBuilder.class));
        String url = "/report?name=" + reportName + "&startDate=" + startDate + "&endDate=" + endDate + "&responseType=" + responseType + "&paperSize=A3";
        ResultActions perform = mockMvc.perform(get(url));
        final MvcResult mvcResult = perform.andReturn();
        if (!ignoreStatusCheck)
            perform.andExpect(status().isOk());
        return mvcResult;
    }

    protected JasperReportBuilder fetchReportBuilder(String reportName, String startDate, String endDate) throws Exception {
        getConnection().commit();
        ArgumentCaptor<List> reportBuilderArgumentCaptor = ArgumentCaptor.forClass(List.class);
        doCallRealMethod().when(jasperResponseConverter).applyReportTemplates(reportBuilderArgumentCaptor.capture(),
                any(String.class));
        doCallRealMethod().when(jasperResponseConverter).applyHttpHeaders(
                any(String.class), any(HttpServletResponse.class), anyString());
        doCallRealMethod().when(jasperResponseConverter).convertToResponseType(any(ReportParams.class), anyString(), any(OutputStream.class), any(JasperConcatenatedReportBuilder.class));
        String url = "/report?name=" + reportName + "&startDate=" + startDate + "&endDate=" + endDate + "&responseType=text/csv&paperSize=A3";
        ResultActions perform = mockMvc.perform(get(url));
        perform.andReturn();
        perform.andExpect(status().isOk());
        List<JasperReportBuilder> value = reportBuilderArgumentCaptor.getValue();
        return value.get(0);
    }
}
