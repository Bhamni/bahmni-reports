package org.bahmni.reports.template;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.column.DRColumn;
import net.sf.dynamicreports.report.builder.datatype.DoubleType;
import net.sf.dynamicreports.report.builder.datatype.IntegerType;
import net.sf.dynamicreports.report.builder.datatype.StringType;
import net.sf.dynamicreports.report.definition.component.DRIComponent;
import net.sf.dynamicreports.report.definition.component.DRITextField;
import org.bahmni.reports.report.integrationtests.BaseIntegrationTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SqlReportTemplateTest extends BaseIntegrationTest {

	public SqlReportTemplateTest() {
		super("./src/test/resources/config/reports.json");
	}

	@Before
	public void setUp() throws Exception {
		executeDataSet("datasets/genericObservationReportDataSet.xml");
	}

	@Ignore
	@Test
	public void ensureThatTheDatatypeOfColumnsArePreserved() throws Exception {
		String reportName = "ObsSqlReport";
		JasperReportBuilder reportBuilder = fetchReportBuilder(reportName, "2016-04-01", "2016-04-30");

		//obs_id
		DRColumn<?> drColumn = reportBuilder.getReport().getColumns().get(0);
		DRIComponent field1 = drColumn.getComponent();
		assertTrue(field1 instanceof DRITextField);
		assertTrue(((DRITextField)field1).getDataType() instanceof IntegerType);

		//value_numeric
		drColumn = reportBuilder.getReport().getColumns().get(1);
		field1 = drColumn.getComponent();
		assertTrue(field1 instanceof DRITextField);
		assertTrue(((DRITextField)field1).getDataType() instanceof DoubleType);

		//value_text
		drColumn = reportBuilder.getReport().getColumns().get(2);
		field1 = drColumn.getComponent();
		assertTrue(field1 instanceof DRITextField);
		assertTrue(((DRITextField)field1).getDataType() instanceof StringType);
	}
}
