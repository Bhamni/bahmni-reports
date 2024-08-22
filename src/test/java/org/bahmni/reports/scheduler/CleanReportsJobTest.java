package org.bahmni.reports.scheduler;

import org.bahmni.reports.BahmniReportsProperties;
import org.bahmni.reports.persistence.ScheduledReport;
import org.bahmni.reports.persistence.ScheduledReportRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@Ignore
@PowerMockIgnore("jakarta.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(CleanReportsJob.class)
public class CleanReportsJobTest {

    @Mock
    private BahmniReportsProperties bahmniReportsProperties;

    @Mock
    private ScheduledReportRepository scheduledReportRepository;

    @Mock
    private JobExecutionContext jobExecutionContext;

    @InjectMocks
    private CleanReportsJob cleanReportsJob;

    @Test
    public void shouldNotDeleteFileWhenTheDBReportFileNameIsNull() throws Exception {
        when(bahmniReportsProperties.getDaysForHistoryReportsCleanup()).thenReturn("60");
        File mockFile = mock(File.class);
        whenNew(File.class).withArguments("directory", "null").thenReturn(mockFile);
        List<ScheduledReport> scheduledReports = new ArrayList<>();
        scheduledReports.add(new ScheduledReport("1", "testReport", "super", null, new Date(), new Date(), "test", "test", new Date()));
        when(bahmniReportsProperties.getReportsSaveDirectory()).thenReturn("directory");
        when(scheduledReportRepository.findByRequestDateTime(any(Date.class))).thenReturn(scheduledReports);
        cleanReportsJob.execute(jobExecutionContext);
        verify(mockFile, never()).delete();
        verify(scheduledReportRepository).delete(scheduledReports.get(0));
    }

    @Test
    public void testCleanupJobDeletesFileAndDbReportWhenTriggers() throws Exception {
        when(bahmniReportsProperties.getDaysForHistoryReportsCleanup()).thenReturn("60");
        File mockFile = mock(File.class);
        whenNew(File.class).withArguments("directory", "testFileName").thenReturn(mockFile);
        List<ScheduledReport> scheduledReports = new ArrayList<>();
        scheduledReports.add(new ScheduledReport("1", "testReport", "super", "testFileName", new Date(), new Date(), "test", "test", new Date()));
        when(bahmniReportsProperties.getReportsSaveDirectory()).thenReturn("directory");
        when(scheduledReportRepository.findByRequestDateTime(any(Date.class))).thenReturn(scheduledReports);
        cleanReportsJob.execute(jobExecutionContext);
        verify(mockFile, times(1)).delete();
        verify(scheduledReportRepository).delete(scheduledReports.get(0));
    }

    @Test
    public void testCleanupJobDeletesMultipleFileAndDbReportWhenTriggers() throws Exception {
        when(bahmniReportsProperties.getDaysForHistoryReportsCleanup()).thenReturn(null);
        File mockFile = mock(File.class);
        whenNew(File.class).withArguments("directory", "testFileName1").thenReturn(mockFile);
        whenNew(File.class).withArguments("directory", "testFileName2").thenReturn(mockFile);
        List<ScheduledReport> scheduledReports = new ArrayList<>();
        scheduledReports.add(new ScheduledReport("1", "testReport1", "super", "testFileName1", new Date(), new Date(), "test", "test", new Date()));
        scheduledReports.add(new ScheduledReport("2", "testReport2", "super", "testFileName2", new Date(), new Date(), "test", "test", new Date()));
        when(bahmniReportsProperties.getReportsSaveDirectory()).thenReturn("directory");
        when(scheduledReportRepository.findByRequestDateTime(any(Date.class))).thenReturn(scheduledReports);
        cleanReportsJob.execute(jobExecutionContext);
        verify(mockFile, times(2)).delete();
        verify(scheduledReportRepository).delete(scheduledReports.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionsWhenDaysAreNegetive() throws JobExecutionException {
        when(bahmniReportsProperties.getDaysForHistoryReportsCleanup()).thenReturn("-60");
        cleanReportsJob.execute(jobExecutionContext);
    }

    @Test
    public void shouldReturnProperCleupDate() throws Exception {
        when(bahmniReportsProperties.getDaysForHistoryReportsCleanup()).thenReturn("10");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = simpleDate.parse("2016-09-21 12:00:00");
        Date oldDate = simpleDate.parse("2016-09-11 12:00:00");
        whenNew(Date.class).withNoArguments().thenReturn(currentDate);
        cleanReportsJob.execute(jobExecutionContext);
        verify(scheduledReportRepository).findByRequestDateTime(oldDate);
    }
}
