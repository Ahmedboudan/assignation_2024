package ma.nemo.assignment;

import ma.nemo.assignment.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NiceStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(NiceStoreApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
	}
}
