package com.springboot.config.security;

import com.springboot.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConfig extends OncePerRequestFilter {
    private static final String[] ignoreURI = {"/user/login/", "/user/register/", "/user/visitor-token-check/", "/user/get-verify-code/",
            "/user/forget-password/", "/user/url-token-check/", "/user/reset-password/", "/user/confirm-permission-application/"};
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ") || shouldIgnore(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(7);

        String email = JwtUtils.extractEmail(token);
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 从数据库中查找并返回 userDetails
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (JwtUtils.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }
    }

    private boolean shouldIgnore(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        for (String uri : ignoreURI) {
            if (requestURI.startsWith(uri)) {
                return true;
            }
        }
        return false;
    }
}
