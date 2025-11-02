package com.soumaya.orders_management_app.backend.ExceptionHandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        String exceptionType = (String)request.getAttribute("jwtException");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        if ("expired".equals(exceptionType)){
            response.getWriter().write("{\"error\": \"jwt expiré\"}");
            
        }else if("invalid".equals(exceptionType)){
            response.getWriter().write("{\"error\": \"jwt non valide\"}");
        }
        else{
            response.getWriter().write("{\"error\": \"non autorisé\"}");
        }

    }
}
