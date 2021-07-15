package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;


// 스프링 부트를 사용하지 않았다면 아래의 애노테이션을 붙여야 했음
// @EnableJpaRepositories(basePackages = "study.datajpa")
@SpringBootApplication
@EnableJpaAuditing // 이걸 실수로 안넣으면 JpaAuditing 안 된다.
public class JakeSeoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JakeSeoApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		// 실무에서는 SpringSecurityContext 등에서 유저 ID 등을 가져와서 처리한다.
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
