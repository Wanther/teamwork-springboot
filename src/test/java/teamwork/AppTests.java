package teamwork;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.profiles.active=dev"})
public class AppTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testPasswordEncoder() {
		var encoder = new BCryptPasswordEncoder(10);

		var phpPassword = "$2a$10$lf3gFWexppvnd2HoDO81SuYhFUkhPwwF1v7Nbh8TEjbFeQHOOiQSe";

		Assert.assertTrue(encoder.matches("aA111111", phpPassword));

	}

	@Test
	public void testUUID() {
		String uuid = UUID.randomUUID().toString();
		System.out.println(String.format("%d-%s", uuid.length(), uuid));
	}

}
