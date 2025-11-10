package com.no99.edunexususer_and_role_management.security;

import com.no99.edunexususer_and_role_management.service.CustomUserDetailsService;
import com.no99.edunexususer_and_role_management.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                System.out.println("JWT token found in request to: " + request.getRequestURI());
                if (tokenProvider.validateToken(jwt)) {
                    String username = tokenProvider.getUsernameFromToken(jwt);
                    System.out.println("Valid JWT token for user: " + username);

                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("User authenticated successfully in filter: " + username);
                } else {
                    System.err.println("Invalid JWT token");
                }
            } else {
                System.out.println("No JWT token found in request to: " + request.getRequestURI());
            }
        } catch (Exception ex) {
            System.err.println("Could not set user authentication in security context: " + ex.getMessage());
            ex.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
