package pl.daveproject.dietapp.configuration;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.daveproject.dietapp.security.ui.LoginView;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends VaadinWebSecurity {

    private static final String USER_BY_EMAIL_QUERY =
            "select email, password, enabled from application_user where email = ?";
    private static final String AUTHORITIES_BY_EMAIL_QUERY =
            "select email, role from authority where email = ?";

    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/public/**"))
                .permitAll());

        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Bean
    public UserDetailsManager userDetailsService() {
        var jdbcManager = new JdbcUserDetailsManager(dataSource);
        jdbcManager.setUsersByUsernameQuery(USER_BY_EMAIL_QUERY);
        jdbcManager.setAuthoritiesByUsernameQuery(AUTHORITIES_BY_EMAIL_QUERY);
        return jdbcManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
