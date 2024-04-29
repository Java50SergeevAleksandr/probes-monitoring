package telran.probes.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfiguration {

	@Value("${app.user.notifier.role}")
	String userNotifierRole;

	@Value("${app.user.range.role}")
	String userRangeRole;

	@Value("${app.admin.notifier.role}")
	String adminNotifierRole;

	@Value("${app.user.accounts.role}")
	String userAccountsRole;

	@Value("${app.admin.range.role}")
	String adminRangeRole;

	@Value("${app.range.provider.path}")
	String rangeSensorUrn;

	@Value("${app.emails.provider.path}")
	String emailsSensorUrn;

	@Value("${app.accounts.provider.urn}")
	String accountsUrn;

	@Value("${app.admin.range.urn}")
	String adminRangeUrn;

	@Value("${app.admin.emails.urn}")
	String adminEmailsUrn;

	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors(customizer -> customizer.disable());
		httpSecurity.csrf(customizer -> customizer.disable());
		httpSecurity.authorizeHttpRequests(customizer -> customizer.requestMatchers(adminRangeUrn + "/**")
				.hasRole(adminRangeRole)
				.requestMatchers(adminEmailsUrn + "/**").hasRole(adminNotifierRole)
				.requestMatchers(rangeSensorUrn + "**").hasRole(userRangeRole)
				.requestMatchers(emailsSensorUrn + "**").hasRole(userNotifierRole)
				.requestMatchers(accountsUrn + "/**").hasRole(userAccountsRole));
		httpSecurity.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		httpSecurity.httpBasic(Customizer.withDefaults());
		return httpSecurity.build();
	}
}