package com.willyoubackend.domain.user.security;

import com.willyoubackend.domain.user.entity.RefreshToken;
import com.willyoubackend.domain.user.entity.UserRoleEnum;
import com.willyoubackend.domain.user.jwt.JwtUtil;
import com.willyoubackend.domain.user.repository.RefreshTokenRepository;
import com.willyoubackend.global.exception.CustomException;
import com.willyoubackend.global.exception.ErrorCode;
import com.willyoubackend.global.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.getJwtFromHeader(req, JwtUtil.AUTHORIZATION_HEADER);
        String refreshToken = jwtUtil.getJwtFromHeader(req, JwtUtil.REFRESH_HEADER);
        log.info(accessToken);
        log.info(refreshToken);

        if (StringUtils.hasText(accessToken)) {
            // accessToken validation check
            if (!jwtUtil.validateToken(accessToken)) {
                // Wooyong Jeong
                // String refresh = req.getHeader(JwtUtil.REFRESH_HEADER);
//                if (!jwtUtil.validateToken(refreshToken) || !refreshTokenRepository.existsByToken(refreshToken)){
//                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    log.info("Token이 만료 되었습니다.");
//                    throw new JwtException("Refresh Token Error");
//                }
                // error
                throw new JwtException(499+"");
                // 추후 필요시 사용
//                if (!jwtUtil.validateToken(refreshToken)) {
//                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    log.info("Token이 만료 되었습니다.");
//                    throw new JwtException("Refresh Token Error");
//                }
//                // Redis : Wooyong Jeong
//                log.info(refresh);
//                if (!refreshTokenRepository.existsByToken(refresh)) {
//                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    log.info("Token이 만료 되었습니다.");
//                    throw new JwtException("Refresh Token Error");
//                }
//                log.info("Access Token reCreate");
//                Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
//                String username = info.getSubject();
//                UserRoleEnum role = UserRoleEnum.valueOf(String.valueOf(info.get("auth")));
//
//                accessToken = jwtUtil.createToken(username, role);
//                res.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
//                accessToken = jwtUtil.substringToken(accessToken);
            }

            Claims info = jwtUtil.getUserInfoFromToken(accessToken);

            log.info("Token Authorization");
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}