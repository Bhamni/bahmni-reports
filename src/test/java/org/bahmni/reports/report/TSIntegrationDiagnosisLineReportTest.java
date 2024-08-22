package org.bahmni.reports.report;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.constant.PageType;
import org.bahmni.reports.BahmniReportsProperties;
import org.bahmni.reports.model.Report;
import org.bahmni.reports.model.TSIntegrationDiagnosisLineReportConfig;
import org.bahmni.reports.template.TSIntegrationDiagnosisLineReportTemplate;
import org.bahmni.reports.util.FileReaderUtil;
import org.bahmni.reports.util.SqlUtil;
import org.bahmni.webclients.HttpClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@Ignore
@PowerMockIgnore({"jakarta.management.*", "jakarta.net.ssl.*", "jakarta.script.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(SqlUtil.class)
public class TSIntegrationDiagnosisLineReportTest {

    @InjectMocks
    TSIntegrationDiagnosisLineReportTemplate tsIntegrationDiagnosisLineReportTemplate;
    @Mock
    Report<TSIntegrationDiagnosisLineReportConfig> mockReport;
    @Mock
    private HttpClient mockHttpClient;
    @Mock
    private Connection mockConnection;
    @Mock
    private Statement mockStatement;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private JasperReportBuilder mockJasperReport;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private ResultSetMetaData mockResultSetMetaData;

    @Mock
    private Properties mockTsProperties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(SqlUtil.class);
        tsIntegrationDiagnosisLineReportTemplate.setDescendantsUrlTemplate("dummyUrlTemplate");
    }

    @Ignore
    @Test
    public void shouldFetchTSDiagnosisLineReportTemplateWhenTSDiagnosisLineReportTypeIsInvoked() {
        TSIntegrationDiagnosisLineReportConfig tsIntegrationDiagnosisLineReportConfig = new TSIntegrationDiagnosisLineReportConfig();
        TSIntegrationDiagnosisLineReport tsIntegrationDiagnosisLineReport = new TSIntegrationDiagnosisLineReport();
        tsIntegrationDiagnosisLineReport.setConfig(tsIntegrationDiagnosisLineReportConfig);
        assertTrue(tsIntegrationDiagnosisLineReport.getTemplate(new BahmniReportsProperties()).getClass().isAssignableFrom(TSIntegrationDiagnosisLineReportTemplate.class));
    }

    @Test
    public void shouldIncludeTerminologyCodeColumnInJasperReport() throws Exception {
        when(mockTsProperties.getProperty("ts.defaultPageSize")).thenReturn("10000");
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        when(mockReport.getName()).thenReturn("dummyReport");
        TSIntegrationDiagnosisLineReportConfig reportConfig = getMockTerminologyDiagnosisLineReportConfig(true, false, false);
        when(mockReport.getConfig()).thenReturn(addPatientAttributesToConfig(reportConfig, true));

        when(mockJasperReport.setPageFormat(any(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setReportName(anyString())).thenReturn(mockJasperReport);
        when(mockJasperReport.setTemplate(any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setShowColumnTitle(anyBoolean())).thenReturn(mockJasperReport);
        when(mockJasperReport.setWhenNoDataType(any())).thenReturn(mockJasperReport);
        when(SqlUtil.executeSqlWithStoredProc(any(), anyString())).thenReturn(mockResultSet);
        when(mockJasperReport.setDataSource(anyString(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.subtotalsAtSummary(any())).thenReturn(mockJasperReport);

        when(mockHttpClient.get(any(URI.class))).thenReturn(getMockTerminologyDescendants());

        tsIntegrationDiagnosisLineReportTemplate.build(mockConnection, mockJasperReport, mockReport, "dummyStartDate", "dummyEndDate", null, PageType.A4);

        verify(mockJasperReport, times(7)).addColumn(any());
    }

    @Test
    public void shouldExcludeTerminologyCodeColumnInJasperReport() throws Exception {
        when(mockTsProperties.getProperty("ts.defaultPageSize")).thenReturn("10000");
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        when(mockReport.getName()).thenReturn("dummyReport");
        TSIntegrationDiagnosisLineReportConfig reportConfig = getMockTerminologyDiagnosisLineReportConfig(false, false, false);
        when(mockReport.getConfig()).thenReturn(addPatientAttributesToConfig(reportConfig, true));

        when(mockJasperReport.setPageFormat(any(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setReportName(anyString())).thenReturn(mockJasperReport);
        when(mockJasperReport.setTemplate(any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setShowColumnTitle(anyBoolean())).thenReturn(mockJasperReport);
        when(mockJasperReport.setWhenNoDataType(any())).thenReturn(mockJasperReport);
        when(SqlUtil.executeSqlWithStoredProc(any(), anyString())).thenReturn(mockResultSet);
        when(mockJasperReport.setDataSource(anyString(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.subtotalsAtSummary(any())).thenReturn(mockJasperReport);

        when(mockHttpClient.get(any(URI.class))).thenReturn(getMockTerminologyDescendants());

        tsIntegrationDiagnosisLineReportTemplate.build(mockConnection, mockJasperReport, mockReport, "dummyStartDate", "dummyEndDate", null, PageType.A4);

        verify(mockJasperReport, times(6)).addColumn(any());
    }

    @Test
    public void shouldIncludePatientAttributeColumnsInJasperReport() throws Exception {
        when(mockTsProperties.getProperty("ts.defaultPageSize")).thenReturn("10000");
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        when(mockReport.getName()).thenReturn("dummyReport");
        TSIntegrationDiagnosisLineReportConfig reportConfig = getMockTerminologyDiagnosisLineReportConfig(false, false, false);
        when(mockReport.getConfig()).thenReturn(addPatientAttributesToConfig(reportConfig, false));

        when(mockJasperReport.setPageFormat(any(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setReportName(anyString())).thenReturn(mockJasperReport);
        when(mockJasperReport.setTemplate(any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setShowColumnTitle(anyBoolean())).thenReturn(mockJasperReport);
        when(mockJasperReport.setWhenNoDataType(any())).thenReturn(mockJasperReport);
        when(SqlUtil.executeSqlWithStoredProc(any(), anyString())).thenReturn(mockResultSet);
        when(mockJasperReport.setDataSource((ResultSet) any())).thenReturn(mockJasperReport);
        when(mockJasperReport.subtotalsAtSummary(any())).thenReturn(mockJasperReport);

        when(mockHttpClient.get(any(URI.class))).thenReturn(getMockTerminologyDescendants());

        tsIntegrationDiagnosisLineReportTemplate.build(mockConnection, mockJasperReport, mockReport, "dummyStartDate", "dummyEndDate", null, PageType.A4);

        verify(mockJasperReport, times(9)).addColumn(any());
    }

    @Test
    public void shouldIncludePatientAddressColumnsInJasperReport() throws Exception {
        when(mockTsProperties.getProperty("ts.defaultPageSize")).thenReturn("10000");
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        when(mockReport.getName()).thenReturn("dummyReport");
        TSIntegrationDiagnosisLineReportConfig reportConfig = getMockTerminologyDiagnosisLineReportConfig(false, false, false);
        addPatientAttributesToConfig(reportConfig, true);
        when(mockReport.getConfig()).thenReturn(addPatientAddressesToConfig(reportConfig));

        when(mockJasperReport.setPageFormat(any(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setReportName(anyString())).thenReturn(mockJasperReport);
        when(mockJasperReport.setTemplate(any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setShowColumnTitle(anyBoolean())).thenReturn(mockJasperReport);
        when(mockJasperReport.setWhenNoDataType(any())).thenReturn(mockJasperReport);
        when(SqlUtil.executeSqlWithStoredProc(any(), anyString())).thenReturn(mockResultSet);
        when(mockJasperReport.setDataSource(anyString(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.subtotalsAtSummary(any())).thenReturn(mockJasperReport);

        when(mockHttpClient.get(any(URI.class))).thenReturn(getMockTerminologyDescendants());

        tsIntegrationDiagnosisLineReportTemplate.build(mockConnection, mockJasperReport, mockReport, "dummyStartDate", "dummyEndDate", null, PageType.A4);

        verify(mockJasperReport, times(8)).addColumn(any());
    }

    @Test
    public void shouldDisplayShortWhenConceptNameDisplayFormatEqualsShortNamePreferredInJasperReport() throws Exception {
        when(mockTsProperties.getProperty("ts.defaultPageSize")).thenReturn("10000");
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        when(mockReport.getName()).thenReturn("dummyReport");
        TSIntegrationDiagnosisLineReportConfig reportConfig = getMockTerminologyDiagnosisLineReportConfig(false, true, false);
        addPatientAttributesToConfig(reportConfig, true);
        when(mockReport.getConfig()).thenReturn(addPatientAddressesToConfig(reportConfig));

        when(mockJasperReport.setPageFormat(any(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setReportName(anyString())).thenReturn(mockJasperReport);
        when(mockJasperReport.setTemplate(any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setShowColumnTitle(anyBoolean())).thenReturn(mockJasperReport);
        when(mockJasperReport.setWhenNoDataType(any())).thenReturn(mockJasperReport);
        when(SqlUtil.executeSqlWithStoredProc(any(), anyString())).thenReturn(mockResultSet);
        when(mockJasperReport.setDataSource(anyString(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.subtotalsAtSummary(any())).thenReturn(mockJasperReport);

        when(mockHttpClient.get(any(URI.class))).thenReturn(getMockTerminologyDescendants());

        tsIntegrationDiagnosisLineReportTemplate.build(mockConnection, mockJasperReport, mockReport, "dummyStartDate", "dummyEndDate", null, PageType.A4);
        verifyStatic(SqlUtil.class, times(1));
        SqlUtil.executeSqlWithStoredProc(any(), contains("AND cn.concept_name_type = \"SHORT\" AND cn.locale = \"en\" AND cn.voided = false"));
    }

    @Test
    public void shouldDisplayFullySpecifiedWhenConceptNameDisplayFormatNotEqualsShortNamePreferredInJasperReport() throws Exception {
        when(mockTsProperties.getProperty("ts.defaultPageSize")).thenReturn("10000");
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        when(mockReport.getName()).thenReturn("dummyReport");
        TSIntegrationDiagnosisLineReportConfig reportConfig = getMockTerminologyDiagnosisLineReportConfig(false, false, false);
        addPatientAttributesToConfig(reportConfig, true);
        when(mockReport.getConfig()).thenReturn(addPatientAddressesToConfig(reportConfig));

        when(mockJasperReport.setPageFormat(any(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setReportName(anyString())).thenReturn(mockJasperReport);
        when(mockJasperReport.setTemplate(any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setShowColumnTitle(anyBoolean())).thenReturn(mockJasperReport);
        when(mockJasperReport.setWhenNoDataType(any())).thenReturn(mockJasperReport);
        when(SqlUtil.executeSqlWithStoredProc(any(), anyString())).thenReturn(mockResultSet);
        when(mockJasperReport.setDataSource(anyString(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.subtotalsAtSummary(any())).thenReturn(mockJasperReport);

        when(mockHttpClient.get(any(URI.class))).thenReturn(getMockTerminologyDescendants());

        tsIntegrationDiagnosisLineReportTemplate.build(mockConnection, mockJasperReport, mockReport, "dummyStartDate", "dummyEndDate", null, PageType.A4);
        verifyStatic(SqlUtil.class, times(1));
        SqlUtil.executeSqlWithStoredProc(any(), contains("AND cn.concept_name_type = \"FULLY_SPECIFIED\" AND cn.locale = \"en\" AND cn.voided = false"));
    }

    @Test
    public void shouldIncludeIcd10TerminologyCodeColumnInJasperReport() throws Exception {
        when(mockTsProperties.getProperty("ts.defaultPageSize")).thenReturn("10000");
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        when(mockReport.getName()).thenReturn("dummyReport");
        TSIntegrationDiagnosisLineReportConfig reportConfig = getMockTerminologyDiagnosisLineReportConfig(true, false, true);
        when(mockReport.getConfig()).thenReturn(addPatientAttributesToConfig(reportConfig, true));

        when(mockJasperReport.setPageFormat(any(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setReportName(anyString())).thenReturn(mockJasperReport);
        when(mockJasperReport.setTemplate(any())).thenReturn(mockJasperReport);
        when(mockJasperReport.setShowColumnTitle(anyBoolean())).thenReturn(mockJasperReport);
        when(mockJasperReport.setWhenNoDataType(any())).thenReturn(mockJasperReport);
        when(SqlUtil.executeSqlWithStoredProc(any(), anyString())).thenReturn(mockResultSet);
        when(mockJasperReport.setDataSource(anyString(), any())).thenReturn(mockJasperReport);
        when(mockJasperReport.subtotalsAtSummary(any())).thenReturn(mockJasperReport);

        when(mockHttpClient.get(any(URI.class))).thenReturn(getMockTerminologyDescendants());

        when(mockResultSet.getMetaData()).thenReturn(mockResultSetMetaData);
        when(mockResultSetMetaData.getColumnCount()).thenReturn(-1);
        when(mockResultSet.next()).thenReturn(false);

        tsIntegrationDiagnosisLineReportTemplate.build(mockConnection, mockJasperReport, mockReport, "dummyStartDate", "dummyEndDate", null, PageType.A4);

        verify(mockJasperReport, times(8)).addColumn(any());
    }

    private TSIntegrationDiagnosisLineReportConfig getMockTerminologyDiagnosisLineReportConfig(boolean displayTerminologyCodeFlag, boolean shortNamePreferredFlag, boolean icd10ExtensionFlag) {
        TSIntegrationDiagnosisLineReportConfig tsIntegrationDiagnosisLineReportConfig = new TSIntegrationDiagnosisLineReportConfig();
        tsIntegrationDiagnosisLineReportConfig.setDisplayTerminologyCode(displayTerminologyCodeFlag);
        tsIntegrationDiagnosisLineReportConfig.setTerminologyParentCode("dummyCode");
        if (shortNamePreferredFlag)
            tsIntegrationDiagnosisLineReportConfig.setConceptNameDisplayFormat("shortNamePreferred");
        if (icd10ExtensionFlag)
            tsIntegrationDiagnosisLineReportConfig.setExtensions(Arrays.asList("org.bahmni.reports.extensions.sample.SampleResultSetExtension"));
        return tsIntegrationDiagnosisLineReportConfig;
    }

    private TSIntegrationDiagnosisLineReportConfig addPatientAttributesToConfig(TSIntegrationDiagnosisLineReportConfig tsIntegrationDiagnosisLineReportConfig, boolean isEmpty) {
        List<String> patientAttributeList = isEmpty ? new ArrayList<>() : Arrays.asList("education", "primaryContact", "secondaryContact");
        tsIntegrationDiagnosisLineReportConfig.setPatientAttributes(patientAttributeList);
        return tsIntegrationDiagnosisLineReportConfig;
    }

    private TSIntegrationDiagnosisLineReportConfig addPatientAddressesToConfig(TSIntegrationDiagnosisLineReportConfig tsIntegrationDiagnosisLineReportConfig) {
        List<String> patientAddressesList = Arrays.asList("city_village", "address3");
        tsIntegrationDiagnosisLineReportConfig.setPatientAddresses(patientAddressesList);
        return tsIntegrationDiagnosisLineReportConfig;
    }

    private String getMockTerminologyDescendants() {
        return FileReaderUtil.getFileContent("terminologyServices/descendantCodes.json");
    }

}