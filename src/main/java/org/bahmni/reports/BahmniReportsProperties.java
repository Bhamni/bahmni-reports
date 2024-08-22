package org.bahmni.reports;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class BahmniReportsProperties {

    protected Properties props;

    public BahmniReportsProperties(String fileName) {
        this(System.getProperty("user.home") + File.separator + ".bahmni-reports" + File.separator, fileName);
    }

    public BahmniReportsProperties() {
        this(System.getProperty("user.home") + File.separator + ".bahmni-reports" + File.separator
                , "bahmni-reports.properties");
    }

    private BahmniReportsProperties(String directory, String file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(directory + file);
            props = new Properties();
            props.load(new InputStreamReader(inputStream));
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getConfigFileUrl() {
        return props.getProperty("reports.config.url");
    }

    public String getOpenelisUrl() {
        return props.getProperty("openelis.url");
    }

    public String getOpenelisUser() {
        return props.getProperty("openelis.username");
    }

    public String getOpenelisPassword() {
        return props.getProperty("openelis.password");
    }

    public String getOpenERPUrl() {
        return props.getProperty("openerp.url");
    }

    public String getOpenERPUser() {
        return props.getProperty("openerp.username");
    }

    public String getOpenERPPassword() {
        return props.getProperty("openerp.password");
    }

    public String getOpenmrsUrl() {
        return props.getProperty("openmrs.url");
    }

    public String getBahmniReportsDbUrl() {
        return props.getProperty("bahmnireports.db.url");
    }

    public String getReportsUser() {
        return props.getProperty("reports.username");
    }

    public String getReportsPassword() {
        return props.getProperty("reports.password");
    }

    public String getOpenmrsUser() {
        return props.getProperty("openmrs.username");
    }

    public String getOpenmrsPassword() {
        return props.getProperty("openmrs.password");
    }


    public String getOpenmrsRootUrl() {
        return props.getProperty("openmrs.service.rootUrl");
    }

    public String getOpenmrsServiceUser() {
        return props.getProperty("openmrs.service.user");
    }

    public String getOpenmrsServicePassword() {
        return props.getProperty("openmrs.service.password");
    }

    public Integer getOpenmrsConnectionTimeout() {
        return Integer.valueOf(props.getProperty("openmrs.connectionTimeoutInMilliseconds"));
    }

    public Integer getOpenmrsReplyTimeout() {
        return Integer.valueOf(props.getProperty("openmrs.replyTimeoutInMilliseconds"));
    }

    public String getMacroTemplatesTempDirectory() {
        return props.getProperty("macrotemplates.temp.directory");
    }

    public String getBahmniLoginUrl() {
        return props.getProperty("bahmni.login.url");
    }

    public String getReportsSaveDirectory() {
        return props.getProperty("reports.save.directory");
    }

    public String getDaysForHistoryReportsCleanup() {
        return props.getProperty("reports.cleanup.keepItForNDays");
    }

    public String getCleanupJobTriggerTime() {
        return props.getProperty("reports.cleanup.triggerTime");
    }

    public String getTrustSSLConnection() {
        return props.getProperty("reports.json.ssl.accept-untrusted-certificates","true");
    }

    public String getMartUrl() {
        return props.getProperty("bahmni.mart.url");
    }

    public String getMartUser() {
        return props.getProperty("bahmni.mart.username");
    }

    public String getMartPassword() {
        return props.getProperty("bahmni.mart.password");
    }
}
