package com.studdybuddy.calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;




@EnableMongoRepositories(basePackages = "com.studdybuddy.calendar.repository")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })




public class CalendarApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarApplication.class, args);
	}

}
