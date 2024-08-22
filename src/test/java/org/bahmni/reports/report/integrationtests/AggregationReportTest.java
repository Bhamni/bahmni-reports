package org.bahmni.reports.report.integrationtests;

import org.bahmni.reports.wrapper.CsvReport;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@Ignore
public class AggregationReportTest extends BaseIntegrationTest {
    public AggregationReportTest() {
        super("src/test/resources/config/aggregationReportConfig.json");
    }

    @Test
    public void shouldGivePatientCountOnGroupByAgeAndGender() throws Exception {
        executeDataSet("datasets/genericObservationReportDataSet.xml");

        String reportName = "Aggregate Report Name";

        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2016-08-30");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F M", report.getRowAsString(1, " "));
        assertEquals("5 0 1", report.getRowAsString(2, " "));
        assertEquals("11 1 0", report.getRowAsString(3, " "));
    }


    @Test
    public void shouldGiveOnlyOnePatientOnGroupByAgeAndGenderForObsReportWhenConceptNameFilterIsAppliedInConfig() throws Exception {
        executeDataSet("datasets/genericObservationReportDataSet.xml");

        String reportName = "Aggregate Report with config";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"Weight\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=Weight"))).thenReturn(objectList.toString());


        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2016-08-30");
        assertEquals(2, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("F", report.getRowAsString(1, " "));
        assertEquals("11 1", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldGivePatientOnGroupByAgeAndPatientIdAsRowGroupAndGenderAsColumnGroupForObsReport() throws Exception {
        executeDataSet("datasets/genericObservationReportDataSet.xml");

        String reportName = "Aggregate Report with multiple Row Groups config";

        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2016-08-30");
        assertEquals(4, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F M", report.getRowAsString(1, " "));
        assertEquals("5 1001 0 1", report.getRowAsString(2, " "));
        assertEquals("11 1000 1 0", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldGivePatientOnGroupByAgeAsRowGroupAndPatientIdAndGenderAsColumnGroupForObsReport() throws Exception {
        executeDataSet("datasets/genericObservationReportDataSet.xml");

        String reportName = "Aggregate Report with multiple Column Groups config";

        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2016-08-30");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("F M", report.getRowAsString(1, " "));
        assertEquals("1000 1001", report.getRowAsString(2, " "));
        assertEquals("5 0 2", report.getRowAsString(3, " "));
        assertEquals("11 3 0", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchAggregateReportForVisitsReport() throws Exception {
        executeDataSet("datasets/genericObservationReportDataSet.xml");

        String reportName = "Aggregated report for visits report";

        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2017-08-30");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F M", report.getRowAsString(1, " "));
        assertEquals("Initial HIV Clinic Visit 1 1", report.getRowAsString(2, " "));
        assertEquals("Return TB Clinic Visit 1 0", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldFetchAggregateReportForProgramsReport() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");
        String reportName = "Aggregated report for programs report";

        CsvReport report = fetchCsvReport(reportName, "2016-04-20", "2016-05-01");
        assertEquals(2, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("F", report.getRowAsString(1, " "));
        assertEquals("HIV PROGRAM 2", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldShowTotalColumnIfConfigured() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");
        String reportName = "Aggregated report for programs with total column";

        CsvReport report = fetchCsvReport(reportName, "2016-04-20", "2016-05-01");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("F Total", report.getRowAsString(1, " "));
        assertEquals("HIV PROGRAM 2 2", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldShowTotalRowIfConfigured() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");
        String reportName = "Aggregated report for programs with total row";

        CsvReport report = fetchCsvReport(reportName, "2016-04-20", "2016-05-01");
        assertEquals(2, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F", report.getRowAsString(1, " "));
        assertEquals("HIV PROGRAM 2", report.getRowAsString(2, " "));
        assertEquals("Total 2", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowTotalRowAndColumnIfConfigured() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");
        String reportName = "Aggregated report for programs with total row and column";

        CsvReport report = fetchCsvReport(reportName, "2016-04-20", "2016-05-01");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F Total", report.getRowAsString(1, " "));
        assertEquals("HIV PROGRAM 2 2", report.getRowAsString(2, " "));
        assertEquals("Total 2 2", report.getRowAsString(3, " "));
    }

    @Test
    public void aggregationReportForObservationsWithAgeGroup() throws Exception {
        executeDataSet("datasets/genericObservationReportDataSet.xml");
        String reportName = "Aggregated report for observations with age group";

        CsvReport report = fetchCsvReport(reportName, "2014-08-01", "2016-08-30");
        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F M", report.getRowAsString(1, " "));
        assertEquals("> 10 Years 1 0", report.getRowAsString(2, " "));
        assertEquals("≤ 10 Years 0 1", report.getRowAsString(3, " "));

    }
    @Test
    public void aggregationReportForLabOrdersWithAgeGroup() throws Exception {
        executeDataSet("datasets/genericLabOrderReportDataSet.xml");
        String reportName = "Aggregated report for labOrders with age group";

        CsvReport report = fetchCsvReport(reportName, "2017-02-01", "2017-02-20");

        assertEquals(2, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("M", report.getRowAsString(1, " "));
        assertEquals("> 10 Years 1", report.getRowAsString(2, " "));

    }

    @Test
    public void aggregationReportForVisitWithAgeGroup() throws Exception {
        executeDataSet("datasets/genericVisitReportDataSet.xml");
        String reportName = "Aggregated report for visits with age group";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(2, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("M", report.getRowAsString(1, " "));
        assertEquals("> 10 Years 1", report.getRowAsString(2, " "));
    }

    @Test
    public void aggregationReportForProgramsWithAgeGroup() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");
        String reportName = "Aggregated report for programs with age group";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(3, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("> 10 Years ≤ 10 Years", report.getRowAsString(1, " "));
        assertEquals("F 1 1", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldThrowExceptionWhenRowGroupIsNotConfigured() throws Exception {

        String reportName = "Aggregated report without rowGroup";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30",true);

        assertEquals("Incorrect Configuration You have not configured rowGroups.", report.getErrorMessage());
    }

    @Test
    public void shouldApplyOnlyRowGroupsWhenColumnGroupIsNotConfigured() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");

        String reportName = "Aggregated report without columnGroup";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(2, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("F 2", report.getRowAsString(2, " "));
        assertEquals("Total 2", report.getRowAsString(3, " "));
    }

}
