package co.dingcodingco.ticketsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Phase 2: 시연 환경. 모든 endpoint permitAll. @PreAuthorize는 어노테이션 의도만 노출.
 *
 * Phase 3 (DECISIONS.md D-007 후속):
 * - JWT 토큰 발급 (POST /api/auth/login)
 * - JwtAuthenticationFilter
 * - CustomUserDetailsService (UserRepository 사용)
 * - 위 어노테이션이 실제 강제됨
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // Phase 3에서 인증/권한으로 교체
            );
        return http.build();
    }
}
