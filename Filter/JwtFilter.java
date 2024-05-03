package com.cafe.cafe_management.Filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cafe.cafe_management.Config.CostumUserDetailsService;
import com.cafe.cafe_management.JWT.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter  extends OncePerRequestFilter{


    @Autowired
    private JwtUtil  jwtUtil;

    @Autowired
    private CostumUserDetailsService service;

    Claims  claims = null;
    private String userName = null;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
            //   if (httpServletRequest.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")) {
                if(request.getServletPath().matches("/user/login|/user/forgetPassword|/user/signup")){
                    filterChain.doFilter(request, response);
                
              }   else {
                    String authorizationHeader = request.getHeader("Authorization");
                    String token = null;

                    if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")){
                        token = authorizationHeader.substring(7);
                        userName = jwtUtil.extractUsername(token);
                        claims = jwtUtil.extractAllClaims(token);

                    }

                    if(userName != null && SecurityContextHolder.getContext().getAuthentication() ==null){
                        UserDetails userDetails = service. loadUserByUsername(userName);


                        if(jwtUtil.validateToken(token, userDetails)){
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null , userDetails.getAuthorities());
                             usernamePasswordAuthenticationToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                             );
                             SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    }
                    filterChain.doFilter(request, response);
              }
       

    }
    public boolean isAdmin(){
        return "admin".equalsIgnoreCase((String)claims.get("role"));
      }


    public boolean isUser(){
        return "user".equalsIgnoreCase((String)claims.get("role"));
      }

      public String getCurrentUser(){
        return userName;
      }
      

      


    
}
