package com.example.m08;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class AuthorizationAspect {

    private final HttpSession session;

    public AuthorizationAspect(HttpSession session) {
        this.session = session;
    }

    // Pointcut untuk targetkan method yang memiliki anotasi @RequiredRole
    @Pointcut("@annotation(requiredRole)")
    public void methodWithRequiredRole(RequiredRole requiredRole) {}

    @Before("methodWithRequiredRole(requiredRole)")
    public void checkAuthorization(RequiredRole requiredRole) throws UnauthorizedException {
        // 1. Periksa apakah terdapat atribut "username" dalam session
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new UnauthorizedException("User is not logged in.");
        }

        // 2. Periksa apakah terdapat role "*" dalam RequiredRole
        String[] allowedRoles = requiredRole.value();
        if (allowedRoles.length == 1 && allowedRoles[0].equals("*")) {
            return; // Role apa saja diperbolehkan, tidak ada pemeriksaan lebih lanjut
        }

        // 3. Periksa apakah role pengguna ada di dalam array requiredRole
        String userRole = (String) session.getAttribute("role");
        if (userRole == null || !isRoleAllowed(userRole, allowedRoles)) {
            throw new UnauthorizedException("User does not have the required role.");
        }
    }

    // Membantu memeriksa apakah role pengguna ada dalam daftar role yang diperbolehkan
    private boolean isRoleAllowed(String userRole, String[] allowedRoles) {
        for (String allowedRole : allowedRoles) {
            if (allowedRole.equals(userRole)) {
                return true; // Role ditemukan dalam allowedRoles
            }
        }
        return false;
    }
}
