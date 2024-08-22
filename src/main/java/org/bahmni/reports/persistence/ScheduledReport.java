package org.bahmni.reports.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "scheduled_report")
public class ScheduledReport {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "user")
    private String user;


    @Column(name = "file_name")
    private String fileName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status")
    private String status;

    @Column(name = "format")
    private String format;

    @Column(name = "request_datetime")
    private Date requestDatetime;

    @Column(name = "error_message")
    private String errorMessage;

    public ScheduledReport() {

    }

    public ScheduledReport(String id, String name, String user, String fileName, Date startDate, Date endDate, String status, String format, Date requestDatetime) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.fileName = fileName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.format = format;
        this.requestDatetime = requestDatetime;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getFileName() {
        return fileName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getFormat() {
        return format;
    }

    public Date getRequestDatetime() {
        return requestDatetime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
