package org.bahmni.reports.report.integrationtests;

import org.bahmni.reports.builder.VisitBuilder;
import org.bahmni.reports.wrapper.CsvReport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.*;
import org.openmrs.api.context.Context;

import static org.junit.Assert.assertEquals;

@Ignore
public class DrugOrderReportTest extends BaseIntegrationTest {

    @Before
    public void setUp() throws Exception {

        Patient patient = Context.getPatientService().getPatient(2);

        Visit visit1 = new VisitBuilder().withPatient(patient).withVisitType(1).withStartDate("2016-01-17").build();
        Context.getVisitService().saveVisit(visit1);

        Visit visit2 = new VisitBuilder().withPatient(patient).withVisitType(1).withStartDate("2016-02-17").build();
        Context.getVisitService().saveVisit(visit2);


        executeDataSet("datasets/DrugOrderReportTest-DrugOrders.xml");
        getConnection().commit();
        Context.clearSession();

    }

    @Test
    public void shouldRetrieveDrugOrderReportWithFourteenColmns() throws Exception {
        CsvReport drugOrderReport = fetchCsvReport("Drug Order report","2016-02-01","2016-02-29");
        assertEquals(14, drugOrderReport.columnsCount());
        assertEquals(16, drugOrderReport.rowsCount());
    }
}