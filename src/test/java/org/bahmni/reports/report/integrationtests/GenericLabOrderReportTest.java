package org.bahmni.reports.report.integrationtests;

import org.bahmni.reports.wrapper.CsvReport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@Ignore
public class GenericLabOrderReportTest extends BaseIntegrationTest {

    public GenericLabOrderReportTest() {
        super("src/test/resources/config/genericLabOrderReportConfig.json");
    }

    @Before
    public void setUp() throws Exception {
        executeDataSet("datasets/genericLabOrderReportDataSet.xml");
    }

    @Test
    public void shouldFetchDefaultColumnsIfNoConfigSpecified() throws Exception {
        String reportName = "LabOrder report without any config";

        CsvReport report = fetchCsvReport(reportName, "2017-02-01", "2017-02-20");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder     No No", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 Hg 78 Normal   Yes No", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldNotFetchStoppedOrders() throws Exception {
        String reportName = "LabOrder report with stopped orders";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2016-01-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFetchRangeValuesForOrdersHavingNumericAnswers() throws Exception {
        String reportName = "LabOrder report with range values";

        CsvReport report = fetchCsvReport(reportName, "2017-02-18", "2017-03-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder     No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 Hg 78 Normal   Yes No", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 26 04-Mar-1991 M 18-Mar-2017 Hg 8 Normal   No No", report.getRowAsString(4, " "));

    }


    @Test
    public void shouldFetchDefaultColumnsIfEmptyConfigSpecified() throws Exception {
        String reportName = "LabOrder report with empty config";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2016-01-20");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowVisitInfoIfConfigured() throws Exception {
        String reportName = "LabOrder report with visit info";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2016-01-30");

        assertEquals(16, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No Initial HIV Clinic Visit 20-Apr-2016 21-May-2016", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No Return TB Clinic Visit 20-Apr-2016 21-May-2016", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldShowProviderInfoIfConfigured() throws Exception {
        String reportName = "LabOrder report with provider info";

        CsvReport report = fetchCsvReport(reportName, "2017-02-01", "2017-02-17");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No Clinical Provider", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowPatientAttributesIfConfigured() throws Exception {
        String reportName = "LabOrder report with patient attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2017-02-17");

        assertEquals(23, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No human 10th pass  8763245677 General  1000 1000 1001 1000", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No human 10th pass  8763245677 General  1000 1001 1003 1001", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No  11th pass  5763245677 General  2000 1000 2001 2000", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowVisitAttributesIfConfigured() throws Exception {
        String reportName = "LabOrder report with visit attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2016-01-30");

        assertEquals(15, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No IPD Admitted", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No PDP Discharged", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldShowPatientAddressIfConfigured() throws Exception {
        String reportName = "LabOrder report with patient address";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2017-02-17");

        assertEquals(15, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No address1_1 Dindori", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No address1_1 Dindori", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No address1_2 Dindori", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldShowDataAnalysisColumnsIfConfigured() throws Exception {
        String reportName = "LabOrder report with data analysis columns";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2016-01-30");

        assertEquals(17, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No 1000 1000 1001 1000", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No 1000 1001 1003 1001", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterByConceptNames() throws Exception {
        String reportName = "LabOrder report filtered by concept names";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"BoneOrder\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=BoneOrder"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2017-02-28");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterByMultipleConceptNames() throws Exception {
        String reportName = "LabOrder report filtered by multiple concept names";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"BoneOrder\",\"BloodOrder\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=BoneOrder&conceptNames=BloodOrder"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2017-02-28");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(5, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder     No No", report.getRowAsString(4, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes", report.getRowAsString(5, " "));

    }

    @Test
    public void shouldFilterByPrograms() throws Exception {
        String reportName = "LabOrder report filtered by program name";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2017-02-30");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No MDR-TB PROGRAM", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder     No No MDR-TB PROGRAM", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes MDR-TB PROGRAM", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 Hg 78 Normal   Yes No MDR-TB PROGRAM", report.getRowAsString(4, " "));

    }

    @Test
    public void shouldFilterByProgramsAndConceptNames() throws Exception {
        String reportName = "LabOrder report filtered by program name and concept name";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"BoneOrder\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=BoneOrder"))).thenReturn(objectList.toString());

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2017-02-28");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No MDR-TB PROGRAM", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldExcludeColumnsSpecifedInTheConfig() throws Exception {
        String reportName = "LabOrder report with excluded columns";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2016-01-30");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No 20-Apr-2016 21-May-2016", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No 20-Apr-2016 21-May-2016", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldThrowAnExceptionIfAllColumnsAreExcluded() throws Exception {

        String reportName = "LabOrder report with all excluded columns";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2016-01-30", true);

        assertEquals("Incorrect Configuration You have excluded all columns.", report.getErrorMessage());
    }

    @Test
    public void shouldShowPendingLabOrderResults() throws Exception {
        String reportName = "LabOrder report with pending orders";

        CsvReport report = fetchCsvReport(reportName, "2020-01-01", "2020-01-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 15-Jan-2020 BoneOrder     No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 16-Jan-2020 BloodOrder     No No", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 16-Jan-2020 BP_Level high Normal  180 No No", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldFilterLabResultOfTextType() throws Exception {
        String reportName = "LabOrder report with test results filtered";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2021-01-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 16-Jan-2020 BP_Level high Normal  180 No No", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterLabResultWithRangeFilter() throws Exception {
        String reportName = "LabOrder report with numeric range filter";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2021-01-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterLabResultWithRangeFilterAndTextValue() throws Exception {
        String reportName = "LabOrder report filter by numeric range and text value";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2021-01-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 16-Jan-2020 BP_Level high Normal  180 No No", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldFilterLabResultWithMiniumRangeFilter() throws Exception {
        String reportName = "LabOrder report filter by minimum range";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2021-01-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 17-Jan-2016 BoneOrder 170 Normal   No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 Hg 78 Normal   Yes No", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 26 04-Mar-1991 M 18-Mar-2017 Hg 8 Normal   No No", report.getRowAsString(4, " "));

    }

    @Test
    public void shouldFilterLabResultWithMaxiumRangeFilter() throws Exception {
        String reportName = "LabOrder report filter by maximum range";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2021-01-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterLabResultWithMultiRangesFilter() throws Exception {
        String reportName = "LabOrder report with multi numeric ranges filter";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2021-01-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 26 04-Mar-1991 M 18-Mar-2017 Hg 8 Normal   No No", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldFilterLabResultWithMultiRangesAndTextValueFilter() throws Exception {
        String reportName = "LabOrder report filter by multi numeric ranges and text value";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2021-01-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 26 04-Mar-1991 M 18-Mar-2017 Hg 8 Normal   No No", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 16-Jan-2020 BP_Level high Normal  180 No No", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFilterLabResultWithTextValueAndMinRangeFilter() throws Exception {
        String reportName = "LabOrder report filter by min range and text value";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2021-01-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 Hg 78 Normal   Yes No", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 26 04-Mar-1991 M 18-Mar-2017 Hg 8 Normal   No No", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 16-Jan-2020 BP_Level high Normal  180 No No", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFilterLabResultWithTextValueAndMaxRangeFilter() throws Exception {
        String reportName = "LabOrder report filter by max range and text value";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2021-01-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier1 PatientName1 familyname1 21 05-Feb-1994 F 29-Jan-2016 BloodOrder 290 Normal   No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 16-Jan-2020 BP_Level high Normal  180 No No", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterByTestNameAndResult() throws Exception {
        String reportName = "LabOrder report filter by test name and result";

        List<String> objectList = new ArrayList<>();
        objectList.add("\"BP_Level\"");

        when(httpClient.get(URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/getChildConcepts?conceptNames=BP_Level"))).thenReturn(objectList.toString());
        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2021-01-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 16-Jan-2020 BP_Level high Normal  180 No No", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFetchLabOrderHavingPanel() throws Exception {
        String reportName = "LabOrder report with panel test";

        CsvReport report = fetchCsvReport(reportName, "2020-01-16", "2022-01-16");

        assertEquals(17, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 16-Jan-2020 BloodOrder     No No 3000 1001  3001", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 28 04-Mar-1991 M 16-Jan-2020 BP_Level high Normal  180 No No 3000 1009 3001 3002", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 30 04-Mar-1991 M 16-Jan-2022 Hg     No No 3000 1006  3003", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 30 04-Mar-1991 M 16-Jan-2022 BP_Level     No No 3000 1009  3003", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchLabOrderHavingPanelAndWithResultsFilled() throws Exception {
        String reportName = "LabOrder report with panel test filled values";

        CsvReport report = fetchCsvReport(reportName, "2022-02-01", "2022-02-16");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier3 PatientName3 familyname3 30 04-Mar-1991 M 16-Feb-2022 BP_Level high Normal 12  No No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 30 04-Mar-1991 M 16-Feb-2022 Hg 290 Normal 123  No No", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFetchLabOrderHavingPanelAndWithPartialResultsFilled() throws Exception {
        String reportName = "LabOrder report with panel test partially filled values";

        CsvReport report = fetchCsvReport(reportName, "2022-03-01", "2022-03-16");

        assertEquals(17, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier3 PatientName3 familyname3 31 04-Mar-1991 M 16-Mar-2022 Hg     No No 3000 1006  3005", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 31 04-Mar-1991 M 16-Mar-2022 BP_Level high Normal 12  No No 3000 1009 3015 3005", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldShowOrderDateTime() throws Exception {
        String reportName = "LabOrder report with order date time";

        CsvReport report = fetchCsvReport(reportName, "2022-03-01", "2022-03-16");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("PatientIdentifier3 PatientName3 familyname3 31 04-Mar-1991 M 16-Mar-2022 Hg     No No 2022-03-16 12:34:31.0", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier3 PatientName3 familyname3 31 04-Mar-1991 M 16-Mar-2022 BP_Level high Normal 12  No No 2022-03-16 12:34:31.0", report.getRowAsString(2, " "));
    }


    @Test
    public void shouldShowExtraIdentifiers() throws Exception {
        String reportName = "LabOrder report with extra identifiers";

        CsvReport report = fetchCsvReport(reportName, "2017-02-01", "2017-02-20");

        assertEquals(15, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No Aadhar2 Pan2", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder     No No Aadhar2 Pan2", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes Aadhar2 Pan2", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 Hg 78 Normal   Yes No Aadhar2 Pan2", report.getRowAsString(4, " "));

    }

    @Test
    public void shouldShowOneExtraIdentifier() throws Exception {
        String reportName = "LabOrder report with one extra identifier";

        CsvReport report = fetchCsvReport(reportName, "2017-02-01", "2017-02-20");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No Pan2", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder     No No Pan2", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes Pan2", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 Hg 78 Normal   Yes No Pan2", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchAgeGroupNameIfConfigured() throws Exception {
        String reportName = "LabOrder report with age group name";

        CsvReport report = fetchCsvReport(reportName, "2017-02-01", "2017-02-20");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No No > 10 Years", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder     No No > 10 Years", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder 8 Normal   No Yes > 10 Years", report.getRowAsString(3, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 Hg 78 Normal   Yes No > 10 Years", report.getRowAsString(4, " "));

    }

    @Test
    public void shouldFilterOutReferredOutTest() throws Exception {
        String reportName = "LabOrder report with referred out filter";

        CsvReport report = fetchCsvReport(reportName, "2017-02-01", "2017-02-20");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 17-Feb-2017 BoneOrder LowBoneMarrowFull(LowBoneMarrowShort) Abnormal   No", report.getRowAsString(1, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 BloodOrder     No", report.getRowAsString(2, " "));
        assertEquals("PatientIdentifier2 PatientName2 familyname2 25 04-Mar-1991 M 18-Feb-2017 Hg 78 Normal   Yes", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldGetReferredOutTestWithObsValue() throws Exception {
        String reportName = "LabOrder report without any config";

        CsvReport report = fetchCsvReport(reportName, "2014-08-9", "2014-09-10");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("PatientIdentifier2 PatientName2 familyname2 23 04-Mar-1991 M 09-Sep-2014 BloodOrder 40 Normal   No Yes", report.getRowAsString(1, " "));

    }

    @Test
    public void shouldReturnTheColumnsInSpecifiedOrder() throws Exception {
        String reportName = "LabOrder report with order of columns configured";
        CsvReport report = fetchCsvReport(reportName, "2014-08-9", "2014-09-10");
        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(0));
        assertEquals("Test Order Date", report.getColumnHeaderAtIndex(1));
        assertEquals("File Uploaded", report.getColumnHeaderAtIndex(2));
        assertEquals("Min Range", report.getColumnHeaderAtIndex(3));
    }

    @Test
    public void shouldReturnTheColumnsInSpecifiedOrderWithDuplicatePreferredColumns() throws Exception {
        String reportName = "LabOrder report with order of columns configured with duplicates";
        CsvReport report = fetchCsvReport(reportName, "2014-08-9", "2014-09-10");
        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(0));
        assertEquals("Test Order Date", report.getColumnHeaderAtIndex(1));
        assertEquals("File Uploaded", report.getColumnHeaderAtIndex(2));
        assertEquals("Min Range", report.getColumnHeaderAtIndex(3));
    }

    @Test
    public void shouldReturnTheColumnsInSpecifiedOrderWhenTheyBelongToDifferentConfigs() throws Exception {
        String reportName = "LabOrder report with order of columns configured belong to two different sections";

        CsvReport report = fetchCsvReport(reportName, "2014-08-9", "2014-09-10");

        assertEquals(22, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals("Visit Status", report.getColumnHeaderAtIndex(0));
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(1));
        assertEquals("Admission Status", report.getColumnHeaderAtIndex(2));
        assertEquals("Visit Start Date", report.getColumnHeaderAtIndex(3));
    }

    @Test
    public void shouldIgnoreInValidColumnConfiguredInOrderOfColumns() throws Exception {
        String reportName = "LabOrder report with invalid columns configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2014-08-9", "2014-09-10");

        assertEquals(19, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals("Visit Status", report.getColumnHeaderAtIndex(0));
        assertEquals("Admission Status", report.getColumnHeaderAtIndex(1));
        assertEquals("Patient Id", report.getColumnHeaderAtIndex(2));
        assertEquals("Concept Id", report.getColumnHeaderAtIndex(3));
    }

    @Test
    public void shouldExcludeTheColumnConfiguredInExcludeColumnsEvenThoughItIsConfiguredInColumnOrder() throws Exception {
        String reportName = "LabOrder report with excluded columns configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2014-08-9", "2014-09-10");

        assertEquals(18, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals("Patient Id", report.getColumnHeaderAtIndex(0));
        assertEquals("Concept Id", report.getColumnHeaderAtIndex(1));

    }

    @Test
    public void shouldSortTheColumnsBasedOnTheConfiguration() throws Exception {
        String reportName = "LabOrder report with sort by columns configured";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(13, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("PatientIdentifier2"));
    }

    @Test
    public void shouldSortInAscendingOrderByDefaultIfSortOrderIsNotMentionedInConfig() throws Exception {
        String reportName = "LabOrder report with only column configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(15, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("BloodOrder"));
        assertTrue(report.getRowAsString(1, " ").contains("28"));
    }

    @Test
    public void shouldThrowExceptionIfOnlySortByIsConfiguredInConfig() throws Exception {
        String reportName = "LabOrder report with only sort by configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Column is not configured in sortBy", report.getErrorMessage());
    }

    @Test
    public void shouldThrowExceptionIfInvalidColumnIsConfigured() throws Exception {
        String reportName = "LabOrder report with invalid column configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Column that you have configured in sortBy is either not present in output of the report or it is invalid column", report.getErrorMessage());

    }

    @Test
    public void shouldThrowExceptionIfInvalidSortOrderIsConfigured() throws Exception {
        String reportName = "LabOrder report with invalid sortOrder configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Invalid sortOrder in sortBy config. Only asc or desc with case insensitivity is allowed", report.getErrorMessage());
    }

    @Test
    public void shouldSortTheColumnsBasedOnCaseInsensitivityOfColumnNamesAndSortOrderInSortByConfig() throws Exception {
        String reportName = "LabOrder report with case insensitive column name and sort order configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals(17, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("1000"));
        assertTrue(report.getRowAsString(1, " ").contains("Abnormal"));
    }

}