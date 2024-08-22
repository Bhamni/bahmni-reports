package org.bahmni.reports.report.integrationtests;

import org.bahmni.reports.wrapper.CsvReport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class GenericVisitReportTest extends BaseIntegrationTest {
    public GenericVisitReportTest() {
        super("src/test/resources/config/genericVisitReportConfig.json");
    }

    @Before
    public void setUp() throws Exception {
        executeDataSet("datasets/genericVisitReportDataSet.xml");
    }

    @Test
    public void shouldFetchBasicColumnsIfNoConfigSpecified() throws Exception {
        String reportName = "Generic Visit Report Without Config";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFetchVisitIdAndPatientIdAlongWithBasicColumns() throws Exception {
        String reportName = "Generic Visit Report With Data Analysis";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No 2 2", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFetchConfiguredPatientAttributesAlongWithBasicColumns() throws Exception {
        String reportName = "Generic Visit Report With Patient Attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(18, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No  10th pass   General", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFetchConfiguredVisitAttributesAlongWithBasicColumns() throws Exception {
        String reportName = "Generic Visit Report With Visit Attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No  Admitted", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFetchConfiguredAddressFieldsAlongWithBasicColumnsWhenConfigured() throws Exception {
        String reportName = "Generic Visit Report With Patient Addresses";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No  Dindori", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldConsiderVisitStopDateInDateRangeIfSpecified() throws Exception {
        String reportName = "Generic Visit Report With Visit Stopped Date For Date Range";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Return TB Clinic Visit 20-Mar-2016 21-Apr-2016 01-Jan-2017  No", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldConsiderDateOfAdmissionInDateRangeIfSpecified() throws Exception {
        String reportName = "Generic Visit Report With Date Of Admission For Date Range";

        CsvReport report = fetchCsvReport(reportName, "2017-01-01", "2017-03-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Return TB Clinic Visit 20-Mar-2016 21-Apr-2016 01-Jan-2017  No", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldConsiderDateOfDischargeInDateRangeIfSpecified() throws Exception {
        String reportName = "Generic Visit Report With Date Of Discharge For Date Range";

        CsvReport report = fetchCsvReport(reportName, "2017-01-01", "2017-04-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 23 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2017 21-May-2017 01-Apr-2017 01-Apr-2017 No", report.getRowAsString(1, " "));
    }


    @Test
    public void shouldConsiderVisitStartDateInDateRangeIfSpecified() throws Exception {
        String reportName = "Generic Visit Report With Visit Start Date For Date Range";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByVisitTypesIfSpecified() throws Exception {
        String reportName = "Generic Visit Report Filtered By Visit Types";

        CsvReport report = fetchCsvReport(reportName, "2016-03-01", "2016-04-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFetchAllConfiguredData() throws Exception {
        String reportName = "Generic Visit Report With Full Config";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-05-30");

        assertEquals(23, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No  General 10th pass   Dindori Ramgarh  Admitted 2 2", report.getRowAsString(1, " "));
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Return TB Clinic Visit 20-Mar-2016 21-Apr-2016 01-Jan-2017  No  General 10th pass   Dindori Ramgarh  Admitted 2 3", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldNotFetchLocationInfoIfLocationIsVoided() throws Exception {
        String reportName = "Generic Visit Report Filtered By Voided Location Tags";

        CsvReport report = fetchCsvReport(reportName, "2017-04-01", "2017-05-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 23 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2017 21-May-2017 01-Jan-2005 01-Jan-2005 No", report.getRowAsString(1, " "));
        assertEquals("GAN1234 Horatio Hornblower 23 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2017 21-May-2017 01-Apr-2017 01-Apr-2017 No", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldExcludeColumnsSpecifiedInTheConfig() throws Exception {
        String reportName = "Generic Visit Report With Excluded Column";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-05-30");

        assertEquals(11, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldThrowAnExceptionIfAllColumnsAreExcluded() throws Exception {

        String reportName = "Generic Visit Report With Excluded All Column Ignore Case";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2016-01-30", true);

        assertEquals("Incorrect Configuration You have excluded all columns.", report.getErrorMessage());
    }

    @Test
    public void shouldShowConfiguredExtraIdentifiers() throws Exception {
        String reportName = "Generic Visit Report With Extra Identifiers";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No Adhar1 Pan1", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowConfiguredOneExtraIdentifier() throws Exception {
        String reportName = "Generic Visit Report With One Extra Identifiers";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No Pan1", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFetchAgeGroupColumnIfConfigured() throws Exception {
        String reportName = "Generic Visit Report With Age Group Name";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 22 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 20-Apr-2016 21-May-2016 01-Jan-2005  No > 10 Years", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowNewPatientVisitAsYesForThePatientsFirstVisit() throws Exception {
        String reportName = "Generic Visit Report Without Config";

        CsvReport report = fetchCsvReport(reportName, "2008-08-10", "2008-09-15");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("GAN1234 Horatio Hornblower 14 02-Oct-1993 M 15-Aug-2008 Initial HIV Clinic Visit 15-Aug-2008 01-Oct-2008   Yes", report.getRowAsString(1, " "));

    }

    @Test
    public void shouldHaveColumnsProvidedInTheOrderThatTheyHaveConfigured() throws Exception {
        String reportName = "visit report with order of columns configured";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(12, report.columnsCount());
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(0));
        assertEquals("Visit Type", report.getColumnHeaderAtIndex(1));
        assertEquals("Age", report.getColumnHeaderAtIndex(2));
        assertEquals("Patient Identifier", report.getColumnHeaderAtIndex(3));
        assertEquals("New patient visit", report.getColumnHeaderAtIndex(4));
    }

    @Test
    public void shouldHaveColumnsProvidedInTheOrderThatTheyHaveConfiguredFromDifferentGroups() throws Exception {
        String reportName = "visit report which have patient attributes configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(19, report.columnsCount());
        assertEquals("class", report.getColumnHeaderAtIndex(0));
        assertEquals("cluster", report.getColumnHeaderAtIndex(1));
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(2));
        assertEquals("Visit Status", report.getColumnHeaderAtIndex(3));
        assertEquals("Patient Id", report.getColumnHeaderAtIndex(4));
    }

    @Test
    public void shouldIgnoreTheColumnsWhichAreNotPresentInReport() throws Exception {
        String reportName = "visit report with invalid columns configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(15, report.columnsCount());
        assertEquals("Date Of Admission", report.getColumnHeaderAtIndex(0));
    }

    @Test
    public void shouldNotIncludeExcludedColumnsEvenThoughTheyAreConfiguredInColumnsOrder() throws Exception {
        String reportName = "visit report with excluded columns configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(9, report.columnsCount());
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(0));
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(1));
        assertEquals("Age", report.getColumnHeaderAtIndex(2));
    }

    @Test
    public void shouldBeCaseInsensitiveForConfiguredColumnsOrder() throws Exception {
        String reportName = "visit report with change case columns configured in order of columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(12, report.columnsCount());
        assertEquals("Date Of Discharge", report.getColumnHeaderAtIndex(0));
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(1));
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(2));
        assertEquals("Age", report.getColumnHeaderAtIndex(3));
    }

    @Test
    public void shouldBeCaseInsensitiveForConfiguredColumnsOrderAndExcludeColumns() throws Exception {
        String reportName = "visit report with excluded columns configured and order of columns with case insensitivity";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(9, report.columnsCount());
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(0));
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(1));
        assertEquals("Age", report.getColumnHeaderAtIndex(2));
    }

    @Test
    public void shouldIgnoreDuplicatePreferredColumn() throws Exception {
        String reportName = "visit report which have patient attributes configured in order of columns with duplicate preferred column";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(19, report.columnsCount());
        assertEquals("class", report.getColumnHeaderAtIndex(0));
        assertEquals("cluster", report.getColumnHeaderAtIndex(1));
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(2));
        assertEquals("Visit Status", report.getColumnHeaderAtIndex(3));
        assertEquals("Patient Id", report.getColumnHeaderAtIndex(4));
    }
    public void shouldSortTheColumnsBasedOnTheConfiguration() throws Exception {
        String reportName = "visit report with sort by columns configured";
        CsvReport report = fetchCsvReport(reportName, "2016-03-01", "2020-08-03");

        assertEquals(12, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("Return TB Clinic Visit"));
        assertTrue(report.getRowAsString(2, " ").contains("Initial HIV Clinic Visit"));
    }

    @Test
    public void shouldSortInAscendingOrderByDefaultIfSortOrderIsNotMentionedInConfig() throws Exception {
        String reportName = "visit report with only column configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-03-01", "2020-08-03");

        assertEquals(13, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("Admitted"));
        assertTrue(report.getRowAsString(1, " ").contains("Return TB Clinic Visit"));
        assertTrue(report.getRowAsString(2, " ").contains("Initial HIV Clinic Visit"));
        assertTrue(report.getRowAsString(3, " ").contains("Discharged"));
    }

    @Test
    public void shouldThrowExceptionIfOnlySortByIsConfiguredInConfig() throws Exception {
        String reportName = "visit report with only sort by configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Column is not configured in sortBy", report.getErrorMessage());
    }

    @Test
    public void shouldThrowExceptionIfInvalidColumnIsConfigured() throws Exception {
        String reportName = "visit report with invalid column configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Column that you have configured in sortBy is either not present in output of the report or it is invalid column", report.getErrorMessage());

    }

    @Test
    public void shouldThrowExceptionIfInvalidSortOrderIsConfigured() throws Exception {
        String reportName = "visit report with invalid sortOrder configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Invalid sortOrder in sortBy config. Only asc or desc with case insensitivity is allowed", report.getErrorMessage());
    }

    @Test
    public void shouldSortTheColumnsBasedOnCaseInsensitivityOfColumnNamesAndSortOrderInSortByConfig() throws Exception {
        String reportName = "visit report with case insensitive column name and sort order configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-03-01", "2020-08-03", true);

        assertEquals(14, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("2"));
        assertTrue(report.getRowAsString(1, " ").contains("8"));
        assertTrue(report.getRowAsString(2, " ").contains("7"));
        assertTrue(report.getRowAsString(3, " ").contains("3"));
    }
}