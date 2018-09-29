package org.o7planning.springmvcshoppingcart.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.o7planning.springmvcshoppingcart.dao.AccountDAO;
import org.o7planning.springmvcshoppingcart.dao.OrderDAO;
import org.o7planning.springmvcshoppingcart.dao.ProductDAO;
import org.o7planning.springmvcshoppingcart.dao.impl.AccountDAOImpl;
import org.o7planning.springmvcshoppingcart.dao.impl.OrderDAOImpl;
import org.o7planning.springmvcshoppingcart.dao.impl.ProductDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan("org.o7planning.springmvcshoppingcart.*")
@EnableTransactionManagement
public class ApplicationContextConfig {
	 @Bean
	 public PropertyPlaceholderConfigurer getPropertyPlaceholder(){
	        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
	        propertyPlaceholderConfigurer.setLocation(new FileSystemResource(this.getClass().getClassLoader().
	                getResource("ds-hibernate-cfg.properties").getPath()));

	        return propertyPlaceholderConfigurer;
	 }
	 
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rb = new ResourceBundleMessageSource();
		// Load property in message/validator.properties 
		rb.setBasenames(new String[] { "messages/validator" });
		return rb;
	}
	
	@Bean(name = "viewResolver")
	public InternalResourceViewResolver getViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/pages/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
		}
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		return commonsMultipartResolver;
	}
	
	@Bean(name = "dataSource")
	public DataSource getDataSource(@Value("${app.ora.driver}")String databaseDriver,
			@Value("${app.ora.url}") String databaseUrl,
			@Value("${app.ora.uname}") String databaseUname,
			@Value("${app.ora.passwd}") String databasePasswd) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		System.out.println(databaseDriver);
		// See: ds-hibernate-cfg.properties
		dataSource.setDriverClassName(databaseDriver);
		dataSource.setUrl(databaseUrl);
		dataSource.setUsername(databaseUname);
		dataSource.setPassword(databasePasswd);
		
		System.out.println("## getDataSource: " + dataSource);
		return dataSource;
	}
	
	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource,@Value("${hibernate.dialect}")String hibernateDialect,
			@Value("${hibernate.show_sql}") String hibernateShowSql,
			@Value("${current_session_context_class}") String currentSessionContext) throws Exception {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", hibernateDialect);
		properties.put("hibernate.show_sql", hibernateShowSql);
		properties.put("current_session_context_class", currentSessionContext);
		
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
		
		factoryBean.setPackagesToScan(new String[] { "org.o7planning.springmvcshoppingcart.entity" });
		factoryBean.setDataSource(dataSource);
		factoryBean.setHibernateProperties(properties);
		factoryBean.afterPropertiesSet();
		SessionFactory sf = factoryBean.getObject();
		System.out.println("## getSessionFactory: " + sf);
		return sf;
	}
	
	
	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
		return transactionManager;
	}
	
	@Bean(name = "accountDAO")
	public AccountDAO getApplicantDAO() {
		return new AccountDAOImpl();
	}
	
	@Bean(name = "productDAO")
	public ProductDAO getProductDAO() {
		return new ProductDAOImpl();
	}
	
	@Bean(name = "orderDAO")
	public OrderDAO getOrderDAO() {
		return new OrderDAOImpl();
	}
	
	@Bean(name = "accountDAO")
	public AccountDAO getAccountDAO() {
		return new AccountDAOImpl();
	}	
}