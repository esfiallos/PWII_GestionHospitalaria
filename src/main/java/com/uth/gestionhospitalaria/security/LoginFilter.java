package com.uth.gestionhospitalaria.security;

import com.uth.gestionhospitalaria.data.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // La URL que el usuario está pidiendo
        String reqURI = req.getRequestURI();

        // 1. Revisa si el usuario está en sesión
        boolean loggedIn = (session != null) && (session.getAttribute("usuarioLogueado") != null);

        // 2. Revisa si está pidiendo una página pública
        boolean isLoginPage = reqURI.contains("/login.xhtml");
        boolean isPublicPage = isLoginPage || reqURI.contains("/index.xhtml")
                || reqURI.contains("/jakarta.faces.resource/"); // Para CSS/JS

        // Lógica del filtro
        if (loggedIn || isPublicPage) {
            // Si está logueado O es una página pública, déjalo pasar
            chain.doFilter(request, response);
        } else {
            // No está logueado Y quiere una página privada
            // Redirigir al login
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }
    }
}