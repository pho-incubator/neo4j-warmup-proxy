package org.phonetworks.neo4j.warmupproxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {

	@Autowired
	private Application application;

	@Test
	public void contextLoads() {
		assertNotNull(application.getConnectionFactory());
		assertNotNull(application.inputChannel());
		assertNotNull(application.inputTransformer());
		assertNotNull(application.outputChannel());
	}
}
