package org.bahmni.reports.persistence;

import org.bahmni.reports.model.AllDatasources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = {"org.bahmni.reports.persistence"})
@EnableTransactionManagement
public class PersistenceConfiguration {

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(AllDatasources allDatasources) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(allDatasources.dataSourceFor("bahmniReports"));
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("org.bahmni.reports.persistence");

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        jpaProperties.setProperty("hibernate.show_sql", "false");
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
