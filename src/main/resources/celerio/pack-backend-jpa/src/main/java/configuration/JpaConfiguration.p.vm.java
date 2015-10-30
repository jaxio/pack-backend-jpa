## Copyright 2015 JAXIO http://www.jaxio.com
##
## Licensed under the Apache License, Version 2.0 (the "License");
## you may not use this file except in compliance with the License.
## You may obtain a copy of the License at
##
##    http://www.apache.org/licenses/LICENSE-2.0
##
## Unless required by applicable law or agreed to in writing, software
## distributed under the License is distributed on an "AS IS" BASIS,
## WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
## See the License for the specific language governing permissions and
## limitations under the License.
##
$output.java($Configuration, "JpaConfiguration")##

$output.require("javax.annotation.Resource")##
$output.require("java.util.Properties")##
$output.require("javax.sql.DataSource")##
$output.require("org.hibernate.SessionFactory")##
$output.require("org.hibernate.jpa.HibernateEntityManagerFactory")##
$output.require("org.springframework.beans.factory.annotation.Value")##
$output.require("org.springframework.context.annotation.Bean")##
$output.require("org.springframework.context.annotation.Configuration")##
$output.require("org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor")##
$output.require("org.springframework.orm.jpa.JpaTransactionManager")##
$output.require("org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean")##
$output.require("org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter")##

@Configuration
public class $output.currentClass {

    @Value("classpath:hibernate.properties")
    private Properties jpaProperties;

    @Resource(name = "dataSource")
    private DataSource dataSource;

    /**
     * Enable exception translation for beans annotated with @Repository
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    /**
     * @see read http://www.springframework.org/docs/reference/transaction.html
     */
    @Bean
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
    
    /**
     * Build the entity manager with Hibernate as a provider.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        // We set the persistenceXmlLocation to a different name to make it work on JBoss.
        emf.setPersistenceXmlLocation("classpath:META-INF/spring-persistence.xml");
        emf.setPersistenceUnitName("${configuration.applicationName}PU");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setJpaProperties(jpaProperties);
        return emf;
    }

    @Bean
    public SessionFactory sessionFactory(HibernateEntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.getSessionFactory();
    }
}
