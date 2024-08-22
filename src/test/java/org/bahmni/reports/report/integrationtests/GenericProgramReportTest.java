package org.bahmni.reports.report.integrationtests;

import org.bahmni.reports.wrapper.CsvReport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class GenericProgramReportTest extends BaseIntegrationTest {
    public GenericProgramReportTest() {
        super("src/test/resources/config/genericProgramReportConfig.json");
    }

    @Before
    public void setUp() throws Exception {
        executeDataSet("datasets/genericProgramReportDataSet.xml");
    }

    @Test
    public void shouldFetchBasicColumnsIfNoConfigSpecified() throws Exception {
        String reportName = "Generic Patient Program Report Without Config";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2", report.getRowAsString(4, " "));
    }


    @Test
    public void shouldFetchBasicColumnsOnlyIfEmptyConfigSpecified() throws Exception {
        String reportName = "Generic Patient Program Report With Empty Config";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchProgramsEnrolledWithInSpecifiedDateRange() throws Exception {
        String reportName = "Generic Patient Program Report Without Config";

        CsvReport report = fetchCsvReport(reportName, "2016-04-20", "2016-04-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(3, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2", report.getRowAsString(3, " "));
    }

    @Test
    public void shouldNotThrowErrorIfStartAndEndDatesAreEmpty() throws Exception {
        String reportName = "Generic Patient Program Report Without Config";

        CsvReport report = fetchCsvReport(reportName, "", "");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(0, report.rowsCount());
    }

    @Test
    public void shouldFetchConfiguredPatientAttributesAlongWithBasicColumns() throws Exception {
        String reportName = "Generic Patient Program Report With Patient Attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(15, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2 10th pass  8763245677 General", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1 10th pass  8763245677 General", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2 10th pass  8763245677 General", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2 10th pass  8763245677 General", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldNotFetchPatientAttributesWhenEmptyListIsConfigured() throws Exception {
        String reportName = "Generic Patient Program Report With Empty Patient Attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());

        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldNotFetchPersonAddressesWhenEmptyListIsConfigured() throws Exception {
        String reportName = "Generic Patient Program Report With Empty Patient Addresses";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());


        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchConfiguredPatientAddressesAlongWithBasicColumns() throws Exception {
        String reportName = "Generic Patient Program Report With Patient Addresses";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2 Ramgarh Dindori", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1 Ramgarh Dindori", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2 Hyderabad Address Three", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2 Hyderabad Address Three", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchActiveProgramsWithInTheDateRange() throws Exception {
        String reportName = "Generic Patient Program Report Without Config";

        CsvReport report = fetchCsvReport(reportName, "2016-04-31", "2016-05-30");

        assertEquals(reportName, report.getReportName());
        assertEquals(10, report.columnsCount());
        assertEquals(1, report.rowsCount());
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFetchFieldsForDataAnalysisAlongWithBasic() throws Exception {
        String reportName = "Generic Patient Program Report For Data Analysis";

        CsvReport report = fetchCsvReport(reportName, "2016-04-31", "2016-05-30");

        assertEquals(reportName, report.getReportName());
        assertEquals(13, report.columnsCount());
        assertEquals(1, report.rowsCount());
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2 1,001 1", report.getRowAsString(1, " "));

    }

    @Test
    public void shouldFetchBasicFieldsWithDataAnalysisFalse() throws Exception {
        String reportName = "Generic Patient Program Report For Data Analysis False";

        CsvReport report = fetchCsvReport(reportName, "2016-04-31", "2016-05-30");

        assertEquals(reportName, report.getReportName());
        assertEquals(10, report.columnsCount());
        assertEquals(1, report.rowsCount());
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFetchConfiguredProgramAttributes() throws Exception {
        String reportName = "Generic Patient Program Report With Program Attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(reportName, report.getReportName());
        assertEquals(13, report.columnsCount());
        assertEquals(4, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2   true", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1  FOLLOWING false", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2  DIED true", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchDataFilteredByConfiguredProgramName() throws Exception {
        String reportName = "Generic Patient Program Report Filtered By Program Names";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(reportName, report.getReportName());
        assertEquals(10, report.columnsCount());
        assertEquals(2, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1", report.getRowAsString(1, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFetchBasicColumnsWhenProgramNamesAreEmpty() throws Exception {
        String reportName = "Generic Patient Program Report With Empty Program Names";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(reportName, report.getReportName());
        assertEquals(10, report.columnsCount());
        assertEquals(4, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchAllStatesWhenConfiguredToShowAll() throws Exception {
        String reportName = "Generic Patient Program Report With All The States";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(reportName, report.getReportName());
        assertEquals(12, report.columnsCount());
        assertEquals(6, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 1 19-Apr-2016 22-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2 21-Apr-2016", report.getRowAsString(2, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1 21-Apr-2016", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 1 19-Apr-2016 20-Apr-2016", report.getRowAsString(4, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2 21-Apr-2016", report.getRowAsString(5, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2 21-Apr-2016", report.getRowAsString(6, " "));
    }

    @Test
    public void shouldFetchAllTheDataWithFullConfig() throws Exception {
        String reportName = "Generic Patient Program Report With Full Config";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(reportName, report.getReportName());
        assertEquals(25, report.columnsCount());
        assertEquals(2, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1 21-Apr-2016  10th pass  8763245677 General   FOLLOWING false Ramgarh Dindori 1,000 2", report.getRowAsString(1, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2 21-Apr-2016  10th pass  8763245677 General     Hyderabad Address Three 1,001 2", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFetchAllTheStatesForAParticularPatientWhenConfiguredToSeeAll() throws Exception {
        String reportName = "Generic Patient Program To See All States As Multiple Rows";

        CsvReport report = fetchCsvReport(reportName, "2016-04-19", "2016-04-23");

        assertEquals(reportName, report.getReportName());
        assertEquals(12, report.columnsCount());
        assertEquals(6, report.rowsCount());

        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 1 19-Apr-2016 22-Apr-2016", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2 21-Apr-2016", report.getRowAsString(2, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1 21-Apr-2016", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 1 19-Apr-2016 20-Apr-2016", report.getRowAsString(4, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2 21-Apr-2016", report.getRowAsString(5, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2 21-Apr-2016", report.getRowAsString(6, " "));
    }

    @Test
    public void shouldFetchTheProgramEvenThoughIfItDoesNotHaveState() throws Exception{
        String reportName = "Generic Patient Program Report Without Config";

        CsvReport report = fetchCsvReport(reportName, "2017-04-01", "2017-04-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2", report.getRowAsString(1, " "));
        assertEquals("prog2 Generic Program2 22 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2017 21-Apr-2017", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldExcludeColumnsSpecifedInTheConfig() throws Exception {
        String reportName = "Generic Patient Program Report With Excluded Column";


        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(reportName, report.getReportName());
        assertEquals(23, report.columnsCount());
        assertEquals(2, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1 21-Apr-2016  8763245677 General   FOLLOWING false Ramgarh Dindori 1,000 2", report.getRowAsString(1, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2 21-Apr-2016  8763245677 General     Hyderabad Address Three 1,001 2", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldThrowAnExceptionIfAllColumnsAreExcluded() throws Exception {

        String reportName = "Generic Patient Program Report With Excluded All Column Ignore case";

        CsvReport report = fetchCsvReport(reportName, "2016-01-01", "2016-01-30", true);

        assertEquals("Incorrect Configuration You have excluded all columns.", report.getErrorMessage());
    }

    @Test
    public void shouldFetchReportWithExtraIdentifiers() throws Exception {
        String reportName = "Generic Patient Program Report with extra identifiers";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2 Aadhar1 Pan1", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1 Aadhar1 Pan1", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2 Aadhar2 Pan2", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2 Aadhar2 Pan2", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchReportWithOneExtraIdentifiers() throws Exception {
        String reportName = "Generic Patient Program Report with one extra identifiers";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(11, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2 Pan1", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1 Pan1", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2 Pan2", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2 Pan2", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldFetchAgeGroupColumnIfConfigured() throws Exception {
        String reportName = "Generic Patient Program Report With Age Group Name";

        CsvReport report = fetchCsvReport(reportName, "2016-04-01", "2016-04-30");

        assertEquals(11, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(4, report.rowsCount());
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 HIV PROGRAM 20-Apr-2016 30-Apr-2016 State 2 ≤ 10 Years", report.getRowAsString(1, " "));
        assertEquals("prog1 Generic Program1 7 15-Aug-2008 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 30-Apr-2016 State 1 ≤ 10 Years", report.getRowAsString(2, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 HIV PROGRAM 19-Apr-2016  State 2 > 10 Years", report.getRowAsString(3, " "));
        assertEquals("prog2 Generic Program2 21 15-Aug-1994 F 15-Aug-2008 MDR-TB PROGRAM 19-Apr-2016 19-Apr-2016 State 2 > 10 Years", report.getRowAsString(4, " "));
    }

    @Test
    public void shouldHaveColumnsProvidedInTheOrderThatTheyHaveConfigured() throws Exception {
        String reportName = "program report with order of columns configured";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(10, report.columnsCount());
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(0));
        assertEquals("Age", report.getColumnHeaderAtIndex(1));
        assertEquals("Patient Identifier", report.getColumnHeaderAtIndex(2));
        assertEquals("Program Name", report.getColumnHeaderAtIndex(3));
    }

    @Test
    public void shouldHaveColumnsProvidedInTheOrderThatTheyHaveConfiguredFromDifferentGroups() throws Exception {
        String reportName = "program report which have patient attributes configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(18, report.columnsCount());
        assertEquals("class", report.getColumnHeaderAtIndex(0));
        assertEquals("Registration Id", report.getColumnHeaderAtIndex(1));
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(2));
        assertEquals("High Risk Reasons", report.getColumnHeaderAtIndex(3));
        assertEquals("OutCome", report.getColumnHeaderAtIndex(4));
    }

    @Test
    public void shouldIgnoreTheColumnsWhichAreNotPresentInReport() throws Exception {
        String reportName = "program report with invalid columns configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(13, report.columnsCount());
        assertEquals("Completed Date", report.getColumnHeaderAtIndex(0));
    }

    @Test
    public void shouldNotIncludeExcludedColumnsEvenThoughTheyAreConfiguredInColumnsOrder() throws Exception {
        String reportName = "program report with excluded columns configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(8, report.columnsCount());
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(0));
        assertEquals("Age", report.getColumnHeaderAtIndex(1));
    }

    @Test
    public void shouldSortTheColumnsBasedOnTheConfiguration() throws Exception {
        String reportName = "program report with sort by columns configured";
        CsvReport report = fetchCsvReport(reportName, "2016-03-01", "2020-08-03");

        assertEquals(10, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("MDR-TB PROGRAM"));
        assertTrue(report.getRowAsString(4, " ").contains("HIV PROGRAM"));
    }

    @Test
    public void shouldSortInAscendingOrderByDefaultIfSortOrderIsNotMentionedInConfig() throws Exception {
        String reportName = "program report with only column configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-03-01", "2020-08-03");

        assertEquals(12, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("19-Apr-2016"));
        assertTrue(report.getRowAsString(7, " ").contains("20-Apr-2016"));
    }

    @Test
    public void shouldThrowExceptionIfOnlySortByIsConfiguredInConfig() throws Exception {
        String reportName = "program report with only sort by configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Column is not configured in sortBy", report.getErrorMessage());
    }

    @Test
    public void shouldThrowExceptionIfInvalidColumnIsConfigured() throws Exception {
        String reportName = "program report with invalid column configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Column that you have configured in sortBy is either not present in output of the report or it is invalid column", report.getErrorMessage());

    }

    @Test
    public void shouldThrowExceptionIfInvalidSortOrderIsConfigured() throws Exception {
        String reportName = "program report with invalid sortOrder configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Invalid sortOrder in sortBy config. Only asc or desc with case insensitivity is allowed", report.getErrorMessage());
    }

    @Test
    public void shouldSortTheColumnsBasedOnCaseInsensitivityOfColumnNamesAndSortOrderInSortByConfig() throws Exception {
        String reportName = "program report with case insensitive column name and sort order configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-03-01", "2020-08-03", true);

        assertEquals(13, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("1,000"));
        assertTrue(report.getRowAsString(1, " ").contains("2"));
        assertTrue(report.getRowAsString(2, " ").contains("1"));
        assertTrue(report.getRowAsString(3, " ").contains("1,001"));
    }
}