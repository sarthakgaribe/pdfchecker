package com.niyamr.pdfchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(
	    exclude = {
	        DataSourceAutoConfiguration.class,
	        HibernateJpaAutoConfiguration.class
	    }
	)
public class PdfcheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfcheckerApplication.class, args);
	}

}
