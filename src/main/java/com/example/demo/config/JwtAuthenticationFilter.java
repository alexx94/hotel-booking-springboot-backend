package com.example.demo.config;

import com.example.demo.service.JwtService;
import com.example.demo.service.MyUserDetailsService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.logging.Handler;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            MyUserDetailsService myUserDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // Try cookies first
            String jwt = null;
            jakarta.servlet.http.Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (jakarta.servlet.http.Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        System.out.println("JWT found in cookies: " + jwt);
                        break;
                    }
                }
            }

            // Try authorization header if nothing found in cookies
            if (jwt == null) {
                final String authHeader = request.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                jwt = authHeader.substring(7);
                System.out.println("JWT Token from Authorization Header: " + jwt);
            }


            final String userEmail = jwtService.extractUsername(jwt);
            System.out.println("Extracted UserEmail: " + userEmail);  // Log extracted email

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Authentication = " + authentication);

            // If the authentication is null and the userEmail is not null, proceed
            if (userEmail != null && authentication == null) {
                System.out.println("Attempting to load user by email: " + userEmail);
                UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(userEmail);

                // Ensure user details were loaded correctly
                System.out.println("Loaded UserDetails: " + userDetails.getAuthorities());

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // Token is invalid, send 401 Unauthorized
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid token");
                    return;  // Stop further request processing
                }
            }

            filterChain.doFilter(request, response);  // Allow request to proceed if valid

        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 Unauthorized
            response.getWriter().write("Authentication error: " + exception.getMessage());
        }
    }

}
