package issue_layout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IssueTrackerApplication {

	private static final Logger log = LoggerFactory.getLogger(IssueTrackerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(IssueTrackerApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(TicketRepository repository) {
		return (args) -> {
			// save a couple of issue. 
			repository.save(new Ticket("Kill Bill", "Not enough blood.", "Aaron", "Selya", false, "2018.1.12"));
			repository.save(new Ticket("Godfather", "Too many characters died.", "Alex", "Andrade", false, "2018.2.23"));
			repository.save(new Ticket("Batman", "Not everything, not yet.", "Haoran", "Duan", false, "2019.3.6"));
			repository.save(new Ticket("Bennie and the Jet", "Listen to it all the time.", "I-Yang", "Chen", true, "2020.12.31"));
			repository.save(new Ticket("Beatles", "Let it be.", "Thomas", "Dattilo", false, "1979.5.25"));
/*
			// fetch all issue.
			log.info("Issues found with findAll():");
			log.info("-------------------------------");
			for (Ticket issue : repository.findAll()) {
				log.info(issue.toString());
			}
			log.info("");

			// fetch an individual issue by ID
			Ticket issue = repository.findById(1L).get();
			log.info("Issue found with findOne(1L):");
			log.info("--------------------------------");
			log.info(issue.toString());
			log.info("");

			// fetch issues by last name
			log.info("Issue found with findByLastNameStartsWithIgnoreCase('Bauer'):");
			log.info("--------------------------------------------");
			for (Ticket bauer : repository
					.findByLastNameStartsWithIgnoreCase("Bauer")) {
				log.info(bauer.toString());
			}
			log.info(""); */
		};
	}
}
