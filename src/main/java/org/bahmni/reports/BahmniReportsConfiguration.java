package org.bahmni.reports;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.reports.builder.ComboPooledDataSourceBuilder;
import org.bahmni.webclients.AllTrustedSSLSocketFactory;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.HttpClient;
import org.bahmni.webclients.openmrs.OpenMRSLoginAuthenticator;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.beans.PropertyVetoException;

@Configuration
public class BahmniReportsConfiguration {


    private static final long DEFAULT_MAX_UPLOAD_SIZE = 5242880L;
    private static int IDLE_CONNECTION_TEST_TIME = 300; //in seconds

    private BahmniReportsProperties bahmniReportsProperties;
    private static final Logger logger = LogManager.getLogger(BahmniReportsConfiguration.class);

    @Autowired
    public BahmniReportsConfiguration(BahmniReportsProperties bahmniReportsProperties){
        this.bahmniReportsProperties = bahmniReportsProperties;
    }

    @Bean
    public HttpClient httpClient(Registry<ConnectionSocketFactory> schemeRegistry) {

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(schemeRegistry);
        connectionManager.setDefaultMaxPerRoute(10);

        ConnectionDetails connectionDetails = new ConnectionDetails(bahmniReportsProperties.getOpenmrsRootUrl() + "/session",
                bahmniReportsProperties.getOpenmrsServiceUser(),
                bahmniReportsProperties.getOpenmrsServicePassword(), bahmniReportsProperties.getOpenmrsConnectionTimeout(),
                bahmniReportsProperties.getOpenmrsReplyTimeout(), connectionManager);
        if(!"true".equals(bahmniReportsProperties.getTrustSSLConnection())){
            return new HttpClient(connectionDetails);
        }else {
            return new HttpClient(connectionDetails, new OpenMRSLoginAuthenticator(connectionDetails));
        }
    }


    @Bean
    public Registry<ConnectionSocketFactory> schemeRegistry(SSLConnectionSocketFactory allTrustSSLSocketFactory){

        Registry<ConnectionSocketFactory> schemeRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", allTrustSSLSocketFactory)
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .build();
        return schemeRegistry;
    }
    @Bean
    public SSLConnectionSocketFactory allTrustSSLSocketFactory(){
        return new AllTrustedSSLSocketFactory().getSSLSocketFactory();
    }

    @Bean
    public ComboPooledDataSource openmrsDataSource() throws PropertyVetoException {
        ComboPooledDataSourceBuilder comboPooledDataSourceBuilder = new ComboPooledDataSourceBuilder();
        ComboPooledDataSource dataSource = comboPooledDataSourceBuilder.withUrl(bahmniReportsProperties.getOpenmrsUrl())
                .withUser(bahmniReportsProperties.getOpenmrsUser())
                .withPassword(bahmniReportsProperties.getOpenmrsPassword())
                .withDriver(com.mysql.cj.jdbc.Driver.class).build();

        dataSource.setIdleConnectionTestPeriod(IDLE_CONNECTION_TEST_TIME);
        dataSource.setPreferredTestQuery("SELECT 1;");
        return dataSource;
    }

    @Bean
    public ComboPooledDataSource openelisDataSource() throws PropertyVetoException {
        ComboPooledDataSourceBuilder comboPooledDataSourceBuilder = new ComboPooledDataSourceBuilder();
        ComboPooledDataSource dataSource = comboPooledDataSourceBuilder.withUrl(bahmniReportsProperties.getOpenelisUrl())
                .withUser(bahmniReportsProperties.getOpenelisUser())
                .withPassword(bahmniReportsProperties.getOpenelisPassword())
                .withDriver(Driver.class).build();

        dataSource.setIdleConnectionTestPeriod(IDLE_CONNECTION_TEST_TIME);
        dataSource.setPreferredTestQuery("SELECT 1;");
        return dataSource;
    }

    @Bean
    public ComboPooledDataSource martDataSource() throws PropertyVetoException {
        ComboPooledDataSourceBuilder comboPooledDataSourceBuilder = new ComboPooledDataSourceBuilder();
        ComboPooledDataSource dataSource = comboPooledDataSourceBuilder.withUrl(bahmniReportsProperties.getMartUrl())
                .withUser(bahmniReportsProperties.getMartUser())
                .withPassword(bahmniReportsProperties.getMartPassword())
                .withDriver(Driver.class).build();

        dataSource.setIdleConnectionTestPeriod(IDLE_CONNECTION_TEST_TIME);
        dataSource.setPreferredTestQuery("SELECT 1;");
        return dataSource;
    }

    @Bean
    public ComboPooledDataSource bahmniReportsDataSource() throws PropertyVetoException {
        ComboPooledDataSourceBuilder comboPooledDataSourceBuilder = new ComboPooledDataSourceBuilder();
        ComboPooledDataSource dataSource = comboPooledDataSourceBuilder.withUrl(bahmniReportsProperties.getBahmniReportsDbUrl())
                .withUser(bahmniReportsProperties.getReportsUser())
                .withPassword(bahmniReportsProperties.getReportsPassword())
                .withDriver(com.mysql.cj.jdbc.Driver.class).build();

        dataSource.setIdleConnectionTestPeriod(IDLE_CONNECTION_TEST_TIME);
        dataSource.setPreferredTestQuery("SELECT 1;");
        return dataSource;
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public ComboPooledDataSource openerpDataSource() throws PropertyVetoException {
        ComboPooledDataSourceBuilder comboPooledDataSourceBuilder = new ComboPooledDataSourceBuilder();
        ComboPooledDataSource dataSource = comboPooledDataSourceBuilder.withUrl(bahmniReportsProperties.getOpenERPUrl())
                .withUser(bahmniReportsProperties.getOpenERPUser())
                .withPassword(bahmniReportsProperties.getOpenERPPassword())
                .withDriver(Driver.class).build();

        dataSource.setIdleConnectionTestPeriod(IDLE_CONNECTION_TEST_TIME);
        dataSource.setPreferredTestQuery("SELECT 1;");
        return dataSource;
    }
}
