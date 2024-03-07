package ru.clevertec.ManagementNews.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.clevertec.ManagementNews.multiFeign.user.UserClient;


import javax.crypto.SecretKey;
/**
 * SecurityConfig class configures the security settings for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String secretKey;
    @Autowired
    private ApplicationContext context;
    /**
     * Provides the SecretKey bean for JWT token generation and validation.
     *
     * @return The SecretKey bean.
     */
    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    /**
     * Provides the JwtService bean for JWT token operations.
     *
     * @return The JwtService bean.
     */
    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }
    /**
     * Provides the UserClient bean for user-related operations.
     *
     * @return The UserClient bean.
     */
    @Bean
    public UserClient userClient() {
        return context.getBean(UserClient.class);
    }
    /**
     * Provides the JwtTokenFilter bean for JWT token authentication.
     *
     * @return The JwtTokenFilter bean.
     */
    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(secretKey,userClient());
    }
    /**
     * Configures HTTP security settings for the application.
     *
     * @param http The HttpSecurity instance to configure.
     * @return The SecurityFilterChain instance.
     * @throws Exception If an exception occurs during configuration.
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/news").hasAuthority("JOURNALIST")
                        .requestMatchers(HttpMethod.GET, "/news/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/news/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/news").permitAll()
                        .requestMatchers(HttpMethod.GET, "/news/{newsId}/comments").authenticated()
                        .requestMatchers(HttpMethod.GET, "/news/{newsId}/comments/{commentId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/news/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/news/title/{title}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/news/text/{text}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/check/*").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}
