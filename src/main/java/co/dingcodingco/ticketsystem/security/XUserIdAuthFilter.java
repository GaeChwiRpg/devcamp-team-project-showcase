package co.dingcodingco.ticketsystem.security;

import co.dingcodingco.ticketsystem.domain.user.User;
import co.dingcodingco.ticketsystem.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Phase 2 한정 stub 인증 필터.
 *
 * X-User-Id 헤더로 사용자를 식별하고 SecurityContext 에 ROLE_{ADMIN|OPERATOR|MEMBER} 권한을 세팅한다.
 * Phase 3 (DECISIONS.md D-007 후속) 에서 JwtAuthenticationFilter 로 교체될 예정.
 *
 * 시연/CI 단계에서 @PreAuthorize 가 실제로 강제되도록 인증 컨텍스트만 채우는 역할.
 */
@Component
public class XUserIdAuthFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-User-Id";

    private final UserRepository userRepository;

    public XUserIdAuthFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(HEADER);
        if (header != null && !header.isBlank()) {
            try {
                Long userId = Long.parseLong(header);
                userRepository.findById(userId).ifPresent(this::setAuthentication);
            } catch (NumberFormatException ignored) {
                // 잘못된 헤더는 익명 처리
            }
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(User user) {
        var authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        var auth = new UsernamePasswordAuthenticationToken(
            user.getId(), null, List.of(authority)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
