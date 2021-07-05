package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


// 스프링 부트를 사용하지 않았다면 아래의 애노테이션을 붙여야 했음
// @EnableJpaRepositories(basePackages = "study.datajpa")
@SpringBootApplication
public class JakeSeoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JakeSeoApplication.class, args);
	}

}
