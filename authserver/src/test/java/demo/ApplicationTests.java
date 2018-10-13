package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = AuthserverApplication.class)
//@WebAppConfiguration
//@IntegrationTest("server.port:0")
@SpringBootTest(webEnvironment = DEFINED_PORT,classes = AuthserverApplication.class)
public class ApplicationTests {

	@Value("${local.server.port}")
	private int port;

	@Autowired
	private TestRestTemplate template;


	@Test
	public void homePageProtected() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/uaa/", String.class);
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
	}

	@Test
	public void authorizationRedirects() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/uaa/oauth/authorize", String.class);
		assertEquals(HttpStatus.FOUND, response.getStatusCode());
		String location = response.getHeaders().getFirst("Location");
		assertTrue("Wrong header: " + location,
				location.startsWith("http://localhost:" + port + "/uaa/login"));
	}

	@Test
	public void loginSucceeds() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/uaa/login", String.class);
		String csrf = getCsrf(response.getBody());
		MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("username", "user");
		form.set("password", "password");
		form.set("_csrf", csrf);
		HttpHeaders headers = new HttpHeaders();
		headers.put("COOKIE", response.getHeaders().get("Set-Cookie"));
		RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<MultiValueMap<String, String>>(
				form, headers, HttpMethod.POST, URI.create("http://localhost:" + port
						+ "/uaa/login"));
		ResponseEntity<Void> location = template.exchange(request, Void.class);
		String loca = location.getHeaders().getFirst("Location");
		assertEquals("http://localhost:" + port + "/uaa/",

				loca);
	}

	private String getCsrf(String soup) {
		Matcher matcher = Pattern.compile("(?s).*name=\"_csrf\".*?value=\"([^\"]+).*")
				.matcher(soup);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return null;
	}
	@Test
	public void printPassWord(){
		getPassword("user","password");

	}

	public String getPassword(String userName, String password){
		UserDetails user = User.withDefaultPasswordEncoder()
				.username(userName)
				.password(password)
				.roles("user")
				.build();
		System.out.println(user.getPassword());
		return user.getPassword();
	}

}
