package org.bahmni.reports.report.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bahmni.reports.model.ConceptName;
import org.bahmni.reports.wrapper.CsvReport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Ignore
public class GenericObservationReportTest extends BaseIntegrationTest {
    public GenericObservationReportTest() {
        super("src/test/resources/config/genericObservationReportConfig.json");
    }

    @Before
    public void setUp() throws Exception {
        executeDataSet("datasets/genericObservationReportDataSet.xml");
    }

    @Test
    public void shouldFetchMandatoryColumnsIfNoConfigSpecified() throws Exception {
        String reportName = "Observation report without any config";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 170 2016-04-21 15:30:31.0 21-Apr-2016 21-Apr-2016", report.getRowAsString(1, " "));
    }


    @Test
    public void shouldFetchMandatoryColumnsIfEmptyConfigSpecified() throws Exception {
        String reportName = "Observation report with empty config";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 170 2016-04-21 15:30:31.0 21-Apr-2016 21-Apr-2016", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFetchOneConceptPerRow() throws Exception {
        String reportName = "Observation report without any config";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowProviderInfoIfConfigured() throws Exception {
        String reportName = "Observation report with provider info";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(17, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016  Clinical Provider", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals Clinical Provider", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals Clinical Provider", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowVisitInfoIfConfigured() throws Exception {
        String reportName = "Observation report with visit info";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(19, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016  Initial HIV Clinic Visit 01-Jun-2016 30-Jun-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals Initial HIV Clinic Visit 01-Jun-2016 30-Jun-2016", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals Initial HIV Clinic Visit 01-Jun-2016 30-Jun-2016", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowPatientAttributesIfConfigured() throws Exception {
        String reportName = "Observation report with patient attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(22, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016   10th pass  8763245677 General", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals  10th pass  8763245677 General", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals  10th pass  8763245677 General", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowVisitAttributesIfConfigured() throws Exception {
        String reportName = "Observation report with visit attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(18, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016  OPD", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals OPD", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals OPD", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowPatientAddressIfConfigured() throws Exception {
        String reportName = "Observation report with patient address";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(18, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016   Dindori", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals  Dindori", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals  Dindori", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowDataAnalysisColumnsIfConfigured() throws Exception {
        String reportName = "Observation report with data analysis columns";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(23, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016  1000 1100 1100 1100   1002", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals 1000 1100 1100 1101 1100  1000", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals 1000 1100 1100 1102 1100  1001", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldFilterByConceptNames() throws Exception {
        String reportName = "Observation report filtered by concept names";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Height\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Height"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByConceptClass() throws Exception {
        String reportName = "Observation report filtered by concept class";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterByConceptNamesAndClass() throws Exception {
        String reportName = "Observation report filtered by concept names and class";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Vitals\"");
        objectList.add("\"Height\"");
        objectList.add("\"Weight\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Vitals"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByLocationTags() throws Exception {
        String reportName = "Observation report filtered by location tags";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Vitals  2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldFilterByConceptNamesAndLocationTags() throws Exception {
        String reportName = "Observation report filtered by concept names and location tags";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Height\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Height"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByConceptClassesAndLocationTags() throws Exception {
        String reportName = "Observation report filtered by concept class and location tags";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterByConceptNamesAndConceptClassesAndLocationTags() throws Exception {
        String reportName = "Observation report filtered by concept names and concept class and location tags";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Height\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Height"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByPrograms() throws Exception {
        String reportName = "Observation report filtered by program name";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Vitals  2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldFilterByProgramsAndConceptNames() throws Exception {
        String reportName = "Observation report filtered by program name and concept name";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Height\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Height"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByProgramsAndLocationTags() throws Exception {
        String reportName = "Observation report filtered by program name and location tags";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Vitals  2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldFilterByProgramsAndConceptNameAndLocationTags() throws Exception {
        String reportName = "Observation report filtered by program name and concept names and location tags";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Height\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Height"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByProgramsAndConceptClassFilter() throws Exception {
        String reportName = "Observation report filtered by program name and concept class";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterByProgramsAndConceptNameAndConceptClass() throws Exception {
        String reportName = "Observation report filtered by program name and concept names and concept class";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Vitals\"");
        objectList.add("\"Height\"");
        objectList.add("\"Weight\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Vitals"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterByProgramsAndConceptNameAndConceptClassAndLocationTags() throws Exception {
        String reportName = "Observation report filtered by program name and concept names and concept class and location tags";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Vitals\"");
        objectList.add("\"Height\"");
        objectList.add("\"Weight\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Vitals"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterByVisitTypes() throws Exception {
        String reportName = "Observation report filtered by visit types";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Vitals\"");
        objectList.add("\"Height\"");
        objectList.add("\"Weight\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Vitals"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-04-21", "2016-05-23");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 170 2016-04-21 15:30:31.0 21-Apr-2016 21-Apr-2016", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldApplyDateRangeFilterForVisitStartDate() throws Exception {
        String reportName = "Observation report apply date range for visit start date";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-01");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(6, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Vitals  2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(3, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Vitals  2016-08-02 00:00:00.0 02-Aug-2016 02-Aug-2016", report.getRowAsString(4, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Height 170 2016-08-02 00:00:00.0 02-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(5, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Weight(weight) 70 2016-08-02 00:00:00.0 02-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(6, " "));
    }

    @Test
    public void shouldApplyDateRangeFilterForVisitStopDate() throws Exception {
        String reportName = "Observation report apply date range for visit stop date";

        CsvReport report = fetchCsvReport(reportName, "2016-08-30", "2016-08-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(6, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Vitals  2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(3, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Vitals  2016-08-02 00:00:00.0 02-Aug-2016 02-Aug-2016", report.getRowAsString(4, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Height 170 2016-08-02 00:00:00.0 02-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(5, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Weight(weight) 70 2016-08-02 00:00:00.0 02-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(6, " "));
    }

    @Test
    public void shouldFetchAllDataInOneRowPerEncounter() throws Exception {
        String reportName = "Observation report encounter per row";

        List<ConceptName> objectList = new ArrayList<>();
        objectList.add(new ConceptName("Height", null));
        objectList.add(new ConceptName("Weight", null));

        URI getLeafConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/leafConceptNames?conceptNames=Vitals");
        URI getChildConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Vitals");
        when(httpClient.get(getLeafConceptsUri)).thenReturn(new ObjectMapper().writeValueAsString(objectList));

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        verify(httpClient, never()).get(getChildConceptsUri);
        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 180 80", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 170 70", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldExcludeSpecifiedFromTheDefaultColumns() throws Exception {
        String reportName = "Observation report excluding default columns";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(15, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 170 2016-04-21 15:30:31.0 21-Apr-2016 21-Apr-2016", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldExcludeSpecifiedFromTheVisitInfoColumns() throws Exception {
        String reportName = "Observation report excluding visit info columns";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(18, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016  01-Jun-2016 30-Jun-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals 01-Jun-2016 30-Jun-2016", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals 01-Jun-2016 30-Jun-2016", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldExcludeSpecifiedFromPatientAttributeColumns() throws Exception {
        String reportName = "Observation report excluding patient attribute columns";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(21, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016  10th pass  8763245677 General", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals 10th pass  8763245677 General", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals 10th pass  8763245677 General", report.getRowAsString(3, " "));

    }

    @Test
    public void shouldExcludeSpecifiedFromVisitAttributeColumns() throws Exception {

        String reportName = "Observation report excluding visit attribute columns";
        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(17, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(3, " "));

    }
    @Test
    public void shouldExcludePatientAddressColumn() throws Exception {
        String reportName = "Observation report excluding patient address columns";


        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(17, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldExcludeProviderNameColumn() throws Exception {
        String reportName = "Observation report excluding provider info column";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldExcludeDataAnalysisColumns() throws Exception {
        String reportName = "Observation report excluding data analysis columns";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(22, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016  1100 1100 1100   1002", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals 1100 1100 1101 1100  1000", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals 1100 1100 1102 1100  1001", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldExcludeConceptColumns() throws Exception {
        String reportName = "Observation report excluding concept name columns ignore case";

        List<ConceptName> objectList = new ArrayList<>();
        objectList.add(new ConceptName("Height", null));
        objectList.add(new ConceptName("Weight", null));

        URI getLeafConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/leafConceptNames?conceptNames=Vitals");
        URI getChildConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Vitals");
        when(httpClient.get(getLeafConceptsUri)).thenReturn(new ObjectMapper().writeValueAsString(objectList));


        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        verify(httpClient, never()).get(getChildConceptsUri);
        assertEquals(11, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 80", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 70", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldThrowAnExceptionIfAllColumnsAreExcluded() throws Exception {

        String reportName = "Observation report excluding all columns ignore case";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30", true);

        assertEquals("Incorrect Configuration You have excluded all columns.", report.getErrorMessage());
    }

    @Test
    public void shouldShowExtraIdentifiersIfConfigured() throws Exception {
        String reportName = "Observation report having multiple identifiers";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(17, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 170 2016-04-21 15:30:31.0 21-Apr-2016 21-Apr-2016  Pan11", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowExtraIdentifiersIfConfiguredInEncounterPerRow() throws Exception {
        String reportName = "Observation report having multiple identifiers with encounter per row";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Adhar11 Pan11", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldNotShowPatientWithoutObsForTheConceptsSpecified() throws Exception {
        String reportName = "Observation report with encounter per row";

        List<ConceptName> objectList = new ArrayList<>();
        objectList.add(new ConceptName("Complaint", null));
        objectList.add(new ConceptName("Notes", null));

        URI getLeafConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/leafConceptNames?conceptNames=History");
        URI getChildConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=History");
        when(httpClient.get(getLeafConceptsUri)).thenReturn(new ObjectMapper().writeValueAsString(objectList));


        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        verify(httpClient, never()).get(getChildConceptsUri);
        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(0, report.rowsCount());
    }

    @Test
    public void shouldShowFullySpecifiedAndShortNameOfConceptByDefault() throws Exception {
        String reportName = "Observation report with default concept name format";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowFullySpecifiedNameOfConceptIfConfigured() throws Exception {
        String reportName = "Observation report with fully specified concept name format";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowFullySpecifiedNameWhenConceptDoesntHaveShortNameIfConfigured() throws Exception {
        String reportName = "Observation report with short concept name preferred format";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 weight 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowFullySpecifiedNameAndShortNameIfConfigured() throws Exception {
        String reportName = "Observation report with fullySpecified and shortName format";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Vitals  2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(3, " "));
    }
    @Test
    public void shouldShowFullySpecifiedNameOfConceptIfConfiguredInEncounterPerRow() throws Exception {
        String reportName = "Observation report with fully specified concept name format in encounter per row";

        List<ConceptName> objectList = new ArrayList<>();
        objectList.add(new ConceptName("Height", "HeightShort"));
        objectList.add(new ConceptName("Weight", null));

        URI getLeafConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/leafConceptNames?conceptNames=Vitals");
        URI getChildConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Vitals");
        when(httpClient.get(getLeafConceptsUri)).thenReturn(new ObjectMapper().writeValueAsString(objectList));


        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        verify(httpClient, never()).get(getChildConceptsUri);
        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertThat(report.getColumnHeaderAtIndex(10), is("Height"));
        assertThat(report.getColumnHeaderAtIndex(11), is("Weight"));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 180 80", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 170 70", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldShowShortNameOfConceptIfConfiguredInEncounterPerRow() throws Exception {
        String reportName = "Observation report with short concept name preferred format in encounter per row";

        List<ConceptName> objectList = new ArrayList<>();
        objectList.add(new ConceptName("Height", "HeightShort"));
        objectList.add(new ConceptName("Weight", null));

        URI getLeafConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/leafConceptNames?conceptNames=Vitals");
        URI getChildConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Vitals");
        when(httpClient.get(getLeafConceptsUri)).thenReturn(new ObjectMapper().writeValueAsString(objectList));


        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        verify(httpClient, never()).get(getChildConceptsUri);
        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertThat(report.getColumnHeaderAtIndex(10), is("HeightShort"));
        assertThat(report.getColumnHeaderAtIndex(11), is("Weight"));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 180 80", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 170 70", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldShowFullySpecifiedAndShortNameOfConceptIfConfiguredInEncounterPerRow() throws Exception {
        String reportName = "Observation report with fully specified and short concept name format in encounter per row";

        List<ConceptName> objectList = new ArrayList<>();
        objectList.add(new ConceptName("Height", "HeightShort"));
        objectList.add(new ConceptName("Weight", null));

        URI getLeafConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/leafConceptNames?conceptNames=Vitals");
        URI getChildConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Vitals");
        when(httpClient.get(getLeafConceptsUri)).thenReturn(new ObjectMapper().writeValueAsString(objectList));


        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        verify(httpClient, never()).get(getChildConceptsUri);
        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertThat(report.getColumnHeaderAtIndex(10), is("Height(HeightShort)"));
        assertThat(report.getColumnHeaderAtIndex(11), is("Weight"));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 180 80", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 170 70", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterOutEmptyValuesWhenIgnoreEmptyValuesIsTrue() throws Exception {
        String reportName = "Observation report without empty obs values";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Weight(weight) 80 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFetchAgeGroupColumnIfConfigured() throws Exception {
        String reportName = "Observation report with age group name";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(17, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 170 2016-04-21 15:30:31.0 21-Apr-2016 21-Apr-2016  > 10 Years", report.getRowAsString(1, " "));
    }


    @Test
    public void shouldApplyDateRangeFilterForObsCreatedDate() throws Exception {
        String reportName = "Observation report apply date range for Obs Created date";

        CsvReport report = fetchCsvReport(reportName, "2020-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(6, report.rowsCount());
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Vitals  2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020", report.getRowAsString(1, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Height 170 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Vitals  2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020", report.getRowAsString(3, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Height 170 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(4, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Weight(weight) 70 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(5, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 BP_Level High 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(6, " "));
    }

    @Test
    public void shouldFilterObservationWithRangeFilter() throws Exception {
        String reportName = "Observation report with numeric range filter";

        CsvReport report = fetchCsvReport(reportName, "20216-03-01", "2019-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(6, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 170 2016-04-21 15:30:31.0 21-Apr-2016 21-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 170 2016-05-23 15:30:31.0 23-May-2016 21-Apr-2016", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(3, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(4, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Height 170 2016-08-02 00:00:00.0 02-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(5, " "));
        assertEquals("OBS2 Generic1 Observation2 5 15-Aug-2009 M Chithari    15-Aug-2008 Height 170 2015-07-02 00:00:00.0 02-Jul-2015 01-Aug-2015 Vitals", report.getRowAsString(6, " "));
    }

    @Test
    public void shouldFilterObservationReportWithMiniumRangeFilter() throws Exception {
        String reportName = "Observation report filter by minimum range";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2018-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Weight(weight) 70 2016-08-02 00:00:00.0 02-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterObservationReportWithMaximumRangeFilter() throws Exception {
        String reportName = "Observation report filter by maximum range";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2018-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterObservationReportWithMultiRangesFilter() throws Exception {
        String reportName = "Observation report with multi numeric ranges filter";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Weight(weight) 70 2016-08-02 00:00:00.0 02-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(3, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Weight(weight) 70 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFilterObservationReportWithTextRangeFilter() throws Exception {
        String reportName = "Observation report with test results filtered";

        CsvReport report = fetchCsvReport(reportName, "2020-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 BP_Level High 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterObservationWithRangeAndTextFilter() throws Exception {
        String reportName = "Observation report filter by numeric range and text value";

        CsvReport report = fetchCsvReport(reportName, "2020-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Height 170 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Height 170 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 BP_Level High 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldFilterObservationReportWithMultiRangesAndTextFilter() throws Exception {
        String reportName = "Observation report filter by multi numeric ranges and text value";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(5, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Weight(weight) 70 2016-08-02 00:00:00.0 02-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(3, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Weight(weight) 70 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(4, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 BP_Level High 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(5, " "));
    }

    @Test
    public void shouldFilterObservationReportWithMiniumRangeAndTextFilter() throws Exception {
        String reportName = "Observation report filter by min range and text value";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Weight(weight) 80 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Weight(weight) 70 2016-08-02 00:00:00.0 02-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Weight(weight) 70 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(3, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 BP_Level High 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFilterObservationReportWithMaximumRangeAndTextFilter() throws Exception {
        String reportName = "Observation report filter by max range and text value";

        CsvReport report = fetchCsvReport(reportName, "2020-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 BP_Level High 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterObservationReportWithTestNameAndResultFilter() throws Exception {
        String reportName = "Observation report filter by test name and result";

        List<ConceptName> objectList = new ArrayList<>();

        URI getChildConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=HIV+PROGRAM");
        when(httpClient.get(getChildConceptsUri)).thenReturn(new ObjectMapper().writeValueAsString(objectList));

        CsvReport report = fetchCsvReport(reportName, "2015-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(5, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari HIV PROGRAM 20-Apr-2016 30-Jun-2016 15-Aug-2008 Height 180 2016-06-01 10:20:00.0 01-Jun-2016 21-Apr-2016 Vitals", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Weight(weight) 70 2016-08-02 00:00:00.0 02-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(3, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Weight(weight) 70 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(4, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 BP_Level High 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(5, " "));
    }

    @Test
    public void shouldFilterObservationReportWithEmptyFilter() throws Exception {
        String reportName = "Observation report filter by empty result";

        CsvReport report = fetchCsvReport(reportName, "2020-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Vitals  2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020", report.getRowAsString(1, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Vitals  2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterObservationReportWithEmptyAndRangeAndTextValueFilter() throws Exception {
        String reportName = "Observation report filter by empty value,range and text value";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(6, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Vitals  2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  15-Aug-2008 Height 180 2016-08-01 10:20:00.0 01-Aug-2016 01-Aug-2016 Vitals", report.getRowAsString(2, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari    15-Aug-2008 Vitals  2016-08-02 00:00:00.0 02-Aug-2016 02-Aug-2016", report.getRowAsString(3, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Vitals  2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020", report.getRowAsString(4, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 Vitals  2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020", report.getRowAsString(5, " "));
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari    15-Aug-2008 BP_Level High 2020-08-02 00:00:00.0 02-Aug-2020 02-Aug-2020 Vitals", report.getRowAsString(6, " "));
    }

    @Test
    public void shouldHaveColumnsProvidedInTheOrderThatTheyHaveConfigured() throws Exception {
        String reportName = "Observation report with order of columns configured";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(0));
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(1));
        assertEquals("Age", report.getColumnHeaderAtIndex(2));
        assertEquals("Patient Identifier", report.getColumnHeaderAtIndex(3));
    }

    @Test
    public void shouldHaveColumnsProvidedInTheOrderThatTheyHaveConfiguredFromDifferentGroups() throws Exception {
        String reportName = "Observation report which have patient attributes configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(19, report.columnsCount());
        assertEquals("class", report.getColumnHeaderAtIndex(0));
        assertEquals("cluster", report.getColumnHeaderAtIndex(1));
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(2));
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(3));
        assertEquals("Age", report.getColumnHeaderAtIndex(4));
        assertEquals("Patient Identifier", report.getColumnHeaderAtIndex(5));
    }

    @Test
    public void shouldIgnoreTheColumnsWhichAreNotPresentInReport() throws Exception {
        String reportName = "Observation report with invalid columns configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(19, report.columnsCount());
        assertEquals("cluster", report.getColumnHeaderAtIndex(0));
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(1));
        assertEquals("Patient Identifier", report.getColumnHeaderAtIndex(2));
        assertEquals("Age", report.getColumnHeaderAtIndex(3));
    }

    @Test
    public void shouldNotIncludeExcludedColumnsEvenThoughTheyAreConfiguredInColumnsOrder() throws Exception {
        String reportName = "Observation report with excluded columns configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(0));
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(1));
        assertEquals("Age", report.getColumnHeaderAtIndex(2));

    }

    @Test
    public void shouldSortTheColumnsBasedOnTheConfiguration() throws Exception {
        String reportName = "Observation report with sort by columns configured";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(16, report.columnsCount());
        assertTrue(report.getRowAsString(1," ").contains("OBS2"));
    }

    @Test
    public void shouldSortInAscendingOrderByDefaultIfSortOrderIsNotMentionedInConfig() throws Exception {
        String reportName = "Observation report with only column configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(19, report.columnsCount());
        assertTrue(report.getRowAsString(1," ").contains("Chithari"));
        assertTrue(report.getRowAsString(1," ").contains("High"));
    }

    @Test
    public void shouldThrowExceptionIfOnlySortByIsConfiguredInConfig() throws Exception {
        String reportName = "Observation report with only sort by configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Column is not configured in sortBy", report.getErrorMessage());
    }

    @Test
    public void shouldThrowExceptionIfInvalidColumnIsConfigured() throws Exception {
        String reportName = "Observation report with invalid column configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Column that you have configured in sortBy is either not present in output of the report or it is invalid column", report.getErrorMessage());

    }

    @Test
    public void shouldThrowExceptionIfInvalidSortOrderIsConfigured() throws Exception {
        String reportName = "Observation report with invalid sortOrder configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Invalid sortOrder in sortBy config. Only asc or desc with case insensitivity is allowed", report.getErrorMessage());
    }

    @Test
    public void shouldSortTheColumnsBasedOnCaseInsensitivityOfColumnNamesAndSortOrderInSortByConfig() throws Exception {
        String reportName = "Observation report with case insensitive column name and sort order configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals(19, report.columnsCount());
        assertTrue(report.getRowAsString(1," ").contains("Chithari"));
        assertTrue(report.getRowAsString(1," ").contains("High"));
    }
}