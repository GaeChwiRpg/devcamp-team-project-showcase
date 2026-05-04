package co.dingcodingco.ticketsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Phase 2: 시연 환경. X-User-Id 헤더 기반 stub 인증 (XUserIdAuthFilter).
 * 인증 컨텍스트가 채워지면 @PreAuthorize 가 실제로 강제된다.
 *
 * Phase 3 (DECISIONS.md D-007 후속):
 * - JWT 토큰 발급 (POST /api/auth/login)
 * - JwtAuthenticationFilter 가 XUserIdAuthFilter 를 대체
 * - CustomUserDetailsService (UserRepository 사용)
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final XUserIdAuthFilter xUserIdAuthFilter;

    public SecurityConfig(XUserIdAuthFilter xUserIdAuthFilter) {
        this.xUserIdAuthFilter = xUserIdAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**", "/h2-console/**").permitAll()
                .anyRequest().permitAll()  // method-level @PreAuthorize 로 권한 강제
            )
            .addFilterBefore(xUserIdAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
