package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT,classes = SsoApplication.class)
public class ApplicationTests {

	@Value("${local.server.port}")
	private int port;

	@Value("${security.oauth2.client.userAuthorizationUri}")
	private String authorizeUri;

	@Autowired
	private TestRestTemplate template;
	@Test
	public void homePageLoads() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void userEndpointProtected() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/dashboard/user", String.class);
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		String location = response.getHeaders().getFirst("Location");
		assertTrue("Wrong location: " + location,
				location.startsWith("http://localhost:" + port + "/dashboard/login"));
	}

	@Test
	public void envEndpointProtected() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/actuator/env", String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertEquals("Basic realm=\"Spring\"",
				response.getHeaders().getFirst("WWW-Authenticate"));
	}

	@Test
	public void loginRedirects() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/dashboard/login", String.class);
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		String location = response.getHeaders().getFirst("Location");
		assertTrue("Wrong location: " + location + " not " + authorizeUri, location.startsWith(authorizeUri));
	}

}
