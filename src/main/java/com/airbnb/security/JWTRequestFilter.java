package com.airbnb.security;

import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private PropertyUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader!=null && tokenHeader.startsWith("Bearer ")){
            String token = tokenHeader.substring(8,tokenHeader.length()-1);
            String username = jwtTokenService.getUsername(token);
            Optional<PropertyUser> byUsername = userRepository.findByUsername(username);
            if (byUsername.isPresent()){
                PropertyUser user = byUsername.get();
                UsernamePasswordAuthenticationToken authentication  = new UsernamePasswordAuthenticationToken(user,null, Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())));
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);

    }
}
