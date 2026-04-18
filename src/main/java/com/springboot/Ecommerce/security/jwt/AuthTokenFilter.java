package com.springboot.Ecommerce.security.jwt;

import com.springboot.Ecommerce.security.sevices.UserDetailsImpl;
import com.springboot.Ecommerce.security.sevices.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            System.out.println("jwt filter");
            String jwt = parseJwt(request);
            String username = null;
            UserDetailsImpl userDetails = null;
            if(jwt != null && jwtUtils.validateJwtToken(jwt)){
                username = jwtUtils.getUsernameFromToken(jwt);
            }
            if(username != null){
                 userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
            }
            if(userDetails != null){
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        } catch (Exception e) {
            System.out.println("JWT Error:"+ e.getMessage());
        }
        filterChain.doFilter(request,response);
    }

    private String parseJwt(HttpServletRequest http){
        String token = jwtUtils.getTokenFromHeader(http);
        return token;
    }

}
