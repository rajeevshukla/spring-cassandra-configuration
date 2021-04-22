package com.example.demo;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.autoconfigure.cassandra.DriverConfigLoaderBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.SessionBuilderConfigurer;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.ProgrammaticDriverConfigLoaderBuilder;

@Configuration
@EnableCassandraRepositories
public class CassandraConfig extends AbstractCassandraConfiguration {
	@Value("${spring.data.cassandra.request.timeout}")
	private Duration duration;

	@Value("${spring.data.cassandra.username}")
	private String userName;

	@Value("${spring.data.cassandra.password}")
	private String password;

	@Value("${spring.data.cassandra.port}")
	private Integer port;

	@Value("${spring.data.cassandra.keyspace-name}")
	private String keyspaceName;

	@Value("${spring.data.cassandra.local-datacenter}")
	private String dataCenter;

	@Value("${spring.data.cassandra.contact-points}")
	private String contactPoints;

	@Value("${spring.data.cassandra.schema-action}")
	private String schemaAction;

	/*
	 * @Bean DriverConfigLoaderBuilderCustomizer cassandraDriverCustomizer() {
	 * return (builder) ->
	 * builder.withDuration(DefaultDriverOption.CONTROL_CONNECTION_TIMEOUT,
	 * Duration.ofSeconds(30)); }
	 */


	/*
	 * @Bean(name = "session")
	 * 
	 * @Primary public CqlSessionFactoryBean cassandraSession() {
	 * CqlSessionFactoryBean factory = new CqlSessionFactoryBean();
	 * factory.setUsername(userName); factory.setPassword(password);
	 * factory.setPort(port); factory.setKeyspaceName(keyspaceName);
	 * factory.setContactPoints(contactPoints);
	 * factory.setLocalDatacenter(dataCenter);
	 * factory.setSessionBuilderConfigurer(getSessionBuilderConfigurer()); return
	 * factory; }
	 */

	/*
	 * @Bean public SessionFactoryFactoryBean
	 * cassandraSessionFactory(CqlSessionFactoryBean cqlSession) {
	 * SessionFactoryFactoryBean sessionFactory = new SessionFactoryFactoryBean();
	 * sessionFactory.setSession(cqlSession.getObject());
	 * sessionFactory.setConverter(cassandraConverter());
	 * sessionFactory.setSchemaAction(getSchemaAction()); return sessionFactory; }
	 * 
	 */
	/*
	 * @Bean SessionFactoryInitializer sessionFactoryInitializer(SessionFactory
	 * sessionFactory) {
	 * 
	 * SessionFactoryInitializer initializer = new SessionFactoryInitializer();
	 * initializer.setSessionFactory(sessionFactory);
	 * 
	 * 
	 * 
	 * 
	 * ResourceKeyspacePopulator populator1 = new ResourceKeyspacePopulator();
	 * populator1.setSeparator(";"); populator1.setScripts(new
	 * ClassPathResource("com/myapp/cql/db-schema.cql"));
	 * 
	 * ResourceKeyspacePopulator populator2 = new ResourceKeyspacePopulator();
	 * populator2.setSeparator("@@"); populator2.setScripts(new
	 * ClassPathResource("classpath:com/myapp/cql/db-test-data-1.cql"), // new
	 * ClassPathResource("classpath:com/myapp/cql/db-test-data-2.cql"));
	 * 
	 * initializer.setKeyspacePopulator(new CompositeKeyspacePopulator(populator1,
	 * populator2));
	 * 
	 * 
	 * return initializer; }
	 */

	@Override
	protected String getContactPoints() {
		return contactPoints;
	}

	@Override
	protected String getLocalDataCenter() {
		return dataCenter;
	}

	@Override
	protected int getPort() {
		return port;
	}

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.valueOf(schemaAction);
	}

	@Override
	protected String getKeyspaceName() {
		return keyspaceName;
	}

	protected SessionBuilderConfigurer getSessionBuilderConfigurer() {
		return new SessionBuilderConfigurer() {

			@Override
			public CqlSessionBuilder configure(CqlSessionBuilder cqlSessionBuilder) {
				ProgrammaticDriverConfigLoaderBuilder config = DriverConfigLoader.programmaticBuilder()
						.withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofSeconds(30))
						.withBoolean(DefaultDriverOption.RECONNECT_ON_INIT, true)
						.withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(30))
						.withDuration(DefaultDriverOption.CONTROL_CONNECTION_TIMEOUT, Duration.ofSeconds(20));
				return cqlSessionBuilder.withAuthCredentials(userName, password).withConfigLoader(config.build());
			}
		};
	}

	/*
	 * @Override protected SessionBuilderConfigurer getSessionBuilderConfigurer() {
	 * return new SessionBuilderConfigurer() {
	 * 
	 * @Override public CqlSessionBuilder configure(CqlSessionBuilder
	 * cqlSessionBuilder) { return cqlSessionBuilder.withAuthCredentials(userName,
	 * password)
	 * .withConfigLoader(DriverConfigLoader.programmaticBuilder().withDuration(
	 * DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(15000)).build()); } };
	 * }
	 */

	@Bean
	public CqlSessionBuilderCustomizer cqlSessionBuilderCustomizer() {
		return cqlSessionBuilder -> cqlSessionBuilder.withAuthCredentials(userName, password)
				.withConfigLoader(DriverConfigLoader.programmaticBuilder()
						.withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(15000))
						.withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofSeconds(30))
						.withBoolean(DefaultDriverOption.RECONNECT_ON_INIT, true)
						.withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(30))
						.withDuration(DefaultDriverOption.CONTROL_CONNECTION_TIMEOUT, Duration.ofSeconds(20)).build());
	}

	@Bean
	public DriverConfigLoaderBuilderCustomizer driverConfigLoaderBuilderCustomizer() {
		return loaderBuilder -> loaderBuilder.withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(10))
				.withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofSeconds(10))
				.withDuration(DefaultDriverOption.CONTROL_CONNECTION_TIMEOUT, Duration.ofSeconds(10))
				.withBoolean(DefaultDriverOption.RECONNECT_ON_INIT, true);
	}
}
