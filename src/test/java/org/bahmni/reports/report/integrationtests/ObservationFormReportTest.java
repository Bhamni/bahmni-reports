package org.bahmni.reports.report.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.bahmni.reports.model.ConceptName;
import org.bahmni.reports.wrapper.CsvReport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@Ignore
public class ObservationFormReportTest extends BaseIntegrationTest {
    public ObservationFormReportTest() {
        super("src/test/resources/config/observationFormReportConfig.json");
    }

    @Before
    public void setUp() throws Exception {
        executeDataSet("datasets/observationFormReportDataSet.xml");
        List<ConceptName> objectList = new ArrayList<>();
        List<ConceptName> examinationLeafConcepts = new ArrayList<>();
        examinationLeafConcepts.add(new ConceptName("Examination Notes", null));
        objectList.add(new ConceptName("Examination Notes", null));
        objectList.add(new ConceptName("Height", "Ht"));
        objectList.add(new ConceptName("Weight", null));
        objectList.add(new ConceptName("Temperature", "tmp"));
        String latestPublishedForms = FileUtils.readFileToString(new File("src/test/resources/forms/latestPublishedForms.json"));
        String diabetesIntakeForm = FileUtils.readFileToString(new File("src/test/resources/forms/diabetesIntakeForm.json"));
        String examinationForm = FileUtils.readFileToString(new File("src/test/resources/forms/examinationForm.json"));
        URI getLatestPublishedForms = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/bahmniie/form/latestPublishedForms");
        URI getLeafConceptsUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/leafConceptNames?conceptNames=Examination+Notes&conceptNames=Vitals&conceptNames=HEIGHT&conceptNames=WEIGHT&conceptNames=Temperature");
        URI getLeafConceptsOfExaminationUri = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/reference-data/leafConceptNames?conceptNames=Examination+Notes");
        URI getDiabetesForm = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/form/1bda677d-191e-474f-88d1-ca9d2f4228ef?v=custom:(resources:(value))");
        URI getExaminationForm = URI.create(bahmniReportsProperties.getOpenmrsRootUrl() + "/form/2432f3a9-df0b-456b-b84c-cb843eb1c3ef?v=custom:(resources:(value))");
        when(httpClient.get(getLeafConceptsUri)).thenReturn(new ObjectMapper().writeValueAsString(objectList));
        when(httpClient.get(getLeafConceptsOfExaminationUri)).thenReturn(new ObjectMapper().writeValueAsString(examinationLeafConcepts));
        when(httpClient.get(getLatestPublishedForms)).thenReturn(latestPublishedForms);
        when(httpClient.get(getDiabetesForm)).thenReturn(diabetesIntakeForm);
        when(httpClient.get(getExaminationForm)).thenReturn(examinationForm);
    }

    @Test
    public void shouldThrowExceptionIfConceptnamesIsNotConfigured() throws Exception {
        String reportName = "Observation form report without configuring form names";
        CsvReport report = fetchCsvReport(reportName, "2016-08-0ge1", "2020-08-03", true);

        assertEquals("Incorrect Configuration You need configure atleast one observation form to filter", report.getErrorMessage());
    }
    
    @Test
    public void shouldNotShowValuesIfValidFormNameisNotGiven() throws Exception {
        String reportName = "Observation form report with invalid form name";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);
        
        assertEquals("Incorrect Configuration Please provide a valid Form Name", report.getErrorMessage());
    }

    @Test
    public void shouldFetchAllMandatoryDataInOneRowPerEncounter() throws Exception {
        String reportName = "Observation form report with Mandatory config";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");
        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari disease 180 80 99", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari summary 170 70 98.6", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldGetValuesOfMultipleFormsWhenMultipleFormConfigured() throws Exception {
        String reportName = "Observation form report should contain multiple values when multiple forms configured";
        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");
        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari examDetails,summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowProviderInfoIfConfigured() throws Exception {
        String reportName = "Observation form report with provider info";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(11, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari Clinical Provider summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowVisitInfoIfConfigured() throws Exception {
        String reportName = "Observation form report with visit info";
        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari Initial HIV Clinic Visit 01-Jun-2016 30-Jun-2016 summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowPatientAttributesIfConfigured() throws Exception {
        String reportName = "Observation form report with patient attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(14, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari 10th pass  8763245677 General summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowVisitAttributesIfConfigured() throws Exception {
        String reportName = "Observation form report with visit attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari OPD  summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowPatientAddressIfConfigured() throws Exception {
        String reportName = "Observation form report with patient address";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari  Dindori summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldShowDataAnalysisColumnsIfConfigured() throws Exception {
        String reportName = "Observation form report with data analysis columns";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari summary 180 80 172 1000 1100 1100", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByFormNamesAndClass() throws Exception {
        String reportName = "Observation form report filtered by form names and class";


        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari  180 80", report.getRowAsString(1, " "));
    }


    @Test
    public void shouldFilterByFormNamesAndLocationTags() throws Exception {
        String reportName = "Observation form report filtered by form names and location tags";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari disease 180 80 99", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByFormNamesAndConceptClassesAndLocationTags() throws Exception {
        String reportName = "Observation form report filtered by form names and concept class and location tags";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari  180 80", report.getRowAsString(1, " "));
    }


    @Test
    public void shouldFilterByProgramsAndFormNames() throws Exception {
        String reportName = "Observation form report filtered by program name and form name";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  disease 180 80 99", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByProgramswithProgramAttributes() throws Exception {
        String reportName = "Observation form report filtered by program with program attributes";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(15, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  false  disease 180 80 99", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldNotshowProgramAttributesWhenFilterByprogramsIsNotConfigured() throws Exception {
        String reportName = "Observation form report should not show program attributes when filter by programs disabled";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari disease 180 80 99", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari summary 170 70 98.6", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldFilterByProgramsAndFormNameAndLocationTags() throws Exception {
        String reportName = "Observation form report filtered by program name and form names and location tags";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  disease 180 80 99", report.getRowAsString(1, " "));
    }


    @Test
    public void shouldFilterByProgramsAndFormNameAndConceptClass() throws Exception {
        String reportName = "Observation form report filtered by program name and form names and concept class";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016   180 80", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldFilterByVisitTypes() throws Exception {
        String reportName = "Observation form report filtered by visit types";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldApplyDateRangeFilterForVisitStartDate() throws Exception {
        String reportName = "Observation form report apply date range for visit start date";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-01");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari disease 180 80 99", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari summary 170 70 98.6", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldApplyDateRangeFilterForVisitStopDate() throws Exception {
        String reportName = "Observation form report apply date range for visit stop date";

        CsvReport report = fetchCsvReport(reportName, "2016-08-30", "2016-08-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari disease 180 80 99", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari summary 170 70 98.6", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldApplyDateRangeFilterForProgramEnrollment() throws Exception {
        String reportName = "Observation form report apply date range for program enrollment";

        CsvReport report = fetchCsvReport(reportName, "2016-08-30", "2016-08-30");

        assertEquals(13, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari MDR-TB PROGRAM 01-Aug-2016  disease 180 80 99", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldExcludeSpecifiedFromTheDefaultColumns() throws Exception {
        String reportName = "Observation form report excluding default columns";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(9, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("Generic Observation1 11 15-Aug-2004 F Ganiyari summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldExcludeSpecifiedFromTheVisitInfoColumns() throws Exception {
        String reportName = "Observation form report excluding visit info columns";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari 01-Jun-2016 30-Jun-2016 summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldExcludeSpecifiedFromPatientAttributeColumns() throws Exception {
        String reportName = "Observation form report excluding patient attribute columns";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(15, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari 10th pass  8763245677 General  summary 180 80 172", report.getRowAsString(1, " "));

    }

    @Test
    public void shouldExcludeSpecifiedFromVisitAttributeColumns() throws Exception {

        String reportName = "Observation form report excluding visit attribute columns";
        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(11, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari  summary 180 80 172", report.getRowAsString(1, " "));

    }

    @Test
    public void shouldExcludePatientAddressColumn() throws Exception {
        String reportName = "Observation form report excluding patient address columns";


        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(11, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari  summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldExcludeProviderNameColumn() throws Exception {
        String reportName = "Observation form report excluding provider info column";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari summary 180 80 172", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldExcludeDataAnalysisColumns() throws Exception {
        String reportName = "Observation form report excluding data analysis columns";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari summary 180 80 172 1100 1100", report.getRowAsString(1, " "));
    }

    @Test
    public void shouldExcludeConceptColumns() throws Exception {
        String reportName = "Observation form report excluding concept name columns ignore case";


        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(9, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari disease 80 99", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari summary 70 98.6", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldShowExtraIdentifiersIfConfigured() throws Exception {
        String reportName = "Observation form report having multiple identifiers";

        CsvReport report = fetchCsvReport(reportName, "2016-06-01", "2016-06-30");

        assertEquals(12, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari Adhar11 Pan11 summary 180 80 172", report.getRowAsString(1, " "));
    }
    
    @Test
    public void shouldShowFullySpecifiedNameOfConceptIfConfigured() throws Exception {
        String reportName = "Observation form report with fully specified concept name format";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertThat(report.getColumnHeaderAtIndex(6), is("Examination Notes"));
        assertThat(report.getColumnHeaderAtIndex(7), is("Height"));
        assertThat(report.getColumnHeaderAtIndex(8), is("Weight"));
        assertThat(report.getColumnHeaderAtIndex(9), is("Temperature"));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari disease 180 80 99", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari summary 170 70 98.6", report.getRowAsString(2, " "));
    }
    
    @Test
    public void shouldShowFullySpecifiedNameAndShortNameOfConceptIfConfigured() throws Exception {
        String reportName = "Observation form report with fully specified and short name concept name format";
        
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertThat(report.getColumnHeaderAtIndex(6), is("Examination Notes"));
        assertThat(report.getColumnHeaderAtIndex(7), is("Height(Ht)"));
        assertThat(report.getColumnHeaderAtIndex(8), is("Weight"));
        assertThat(report.getColumnHeaderAtIndex(9), is("Temperature(tmp)"));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari disease 180 80 99", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari summary 170 70 98.6", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldShowShortNameOfConceptIfConfigured() throws Exception {
        String reportName = "Observation form report with short concept name preferred format";
        
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertThat(report.getColumnHeaderAtIndex(6), is("Examination Notes"));
        assertThat(report.getColumnHeaderAtIndex(7), is("Ht"));
        assertThat(report.getColumnHeaderAtIndex(8), is("Weight"));
        assertThat(report.getColumnHeaderAtIndex(9), is("tmp"));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari disease 180 80 99", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari summary 170 70 98.6", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldShowShortNameOfConceptIfNotConfigured() throws Exception {
        String reportName = "Observation form report with default short concept name format";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2016-08-30");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(2, report.rowsCount());
        assertThat(report.getColumnHeaderAtIndex(6), is("Examination Notes"));
        assertThat(report.getColumnHeaderAtIndex(7), is("Ht"));
        assertThat(report.getColumnHeaderAtIndex(8), is("Weight"));
        assertThat(report.getColumnHeaderAtIndex(9), is("tmp"));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Ganiyari disease 180 80 99", report.getRowAsString(1, " "));
        assertEquals("OBS1 Generic Observation1 11 15-Aug-2004 F Chithari summary 170 70 98.6", report.getRowAsString(2, " "));
    }

    @Test
    public void shouldApplyDateRangeFilterForObsCreatedDate() throws Exception {
        String reportName = "Observation form report apply date range for Obs Created date";

        CsvReport report = fetchCsvReport(reportName, "2020-08-01", "2020-08-03");

        assertEquals(10, report.columnsCount());
        assertEquals(reportName, report.getReportName());
        assertEquals(1, report.rowsCount());
        assertEquals("OBS2 Generic1 Observation2 10 15-Aug-2009 M Chithari notes 170 70 100", report.getRowAsString(1, " "));
    }


    @Test
    public void shouldHaveColumnsProvidedInTheOrderThatTheyHaveConfigured() throws Exception {
        String reportName = "Observation form report with order of columns configured";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(10, report.columnsCount());
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(0));
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(1));
        assertEquals("Age", report.getColumnHeaderAtIndex(2));
        assertEquals("Patient Identifier", report.getColumnHeaderAtIndex(3));
    }

    @Test
    public void shouldIgnoreTheColumnsWhichAreNotPresentInReport() throws Exception {
        String reportName = "Observation form report with invalid columns configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(13, report.columnsCount());
        assertEquals("cluster", report.getColumnHeaderAtIndex(0));
        assertEquals("Patient Name", report.getColumnHeaderAtIndex(1));
        assertEquals("Patient Identifier", report.getColumnHeaderAtIndex(2));
        assertEquals("Age", report.getColumnHeaderAtIndex(3));
    }

    @Test
    public void shouldNotIncludeExcludedColumnsEvenThoughTheyAreConfiguredInColumnsOrder() throws Exception {
        String reportName = "Observation form report with excluded columns configured in order of columns";

        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(9, report.columnsCount());
        assertEquals("Birthdate", report.getColumnHeaderAtIndex(0));
        assertEquals("Age", report.getColumnHeaderAtIndex(1));
        assertEquals("Patient Identifier", report.getColumnHeaderAtIndex(2));

    }

    @Test
    public void shouldSortTheColumnsBasedOnTheConfiguration() throws Exception {
        String reportName = "Observation form report with sort by columns configured";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");

        assertEquals(10, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("OBS2"));
        assertTrue(report.getRowAsString(2, " ").contains("OBS1"));
    }

    @Test
    public void shouldSortInAscendingOrderByDefaultIfSortOrderIsNotMentionedInConfig() throws Exception {
        String reportName = "Observation form report with only column configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03");
        
        assertEquals(13, report.columnsCount());
        assertEquals(3, report.rowsCount());
        assertTrue(report.getRowAsString(1, " ").contains("Chithari"));
        assertTrue(report.getRowAsString(3, " ").contains("Ganiyari"));
    }

    @Test
    public void shouldThrowExceptionIfOnlySortByIsConfiguredInConfig() throws Exception {
        String reportName = "Observation form report with only sort by configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Column is not configured in sortBy", report.getErrorMessage());
    }

    @Test
    public void shouldThrowExceptionIfInvalidColumnIsConfigured() throws Exception {
        String reportName = "Observation form report with invalid column configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Column that you have configured in sortBy is either not present in output of the report or it is invalid column", report.getErrorMessage());

    }

    @Test
    public void shouldThrowExceptionIfInvalidSortOrderIsConfigured() throws Exception {
        String reportName = "Observation form report with invalid sortOrder configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals("Incorrect Configuration Invalid sortOrder in sortBy config. Only asc or desc with case insensitivity is allowed", report.getErrorMessage());
    }

    @Test
    public void shouldSortTheColumnsBasedOnCaseInsensitivityOfColumnNamesAndSortOrderInSortByConfig() throws Exception {
        String reportName = "Observation form report with case insensitive column name and sort order configured in  sort by columns";
        CsvReport report = fetchCsvReport(reportName, "2016-08-01", "2020-08-03", true);

        assertEquals(13, report.columnsCount());
        assertTrue(report.getRowAsString(1, " ").contains("Chithari"));
        assertTrue(report.getRowAsString(3, " ").contains("Ganiyari"));
    }

}