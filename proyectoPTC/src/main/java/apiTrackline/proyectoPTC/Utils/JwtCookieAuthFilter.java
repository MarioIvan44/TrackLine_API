package apiTrackline.proyectoPTC.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtCookieAuthFilter.class);
    private static final String AUTH_COOKIE_NAME = "authToken";

    private final JWTUtils jwtUtils;

    @Autowired
    public JwtCookieAuthFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            if (isPublicEndpoint(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 1️⃣ Extraer token de cookie
            String token = extractTokenFromCookies(request);

            // 2️⃣ Si no está en cookie, intentar header Authorization
            if (token == null || token.isBlank()) {
                token = extractTokenFromHeader(request);
            }

            // 3️⃣ Si no hay token, devolver 401
            if (token == null || token.isBlank()) {
                log.info("No se encontró token en cookie ni header");
                sendError(response, "Token no encontrado", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 4️⃣ Validar token
            if (!jwtUtils.validate(token)) {
                log.info("Token inválido o expirado: {}", token);
                sendError(response, "Token inválido o expirado", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Claims claims = jwtUtils.parseToken(token);
            String rol = jwtUtils.extractRol(token);

            if (rol == null || rol.isBlank()) {
                sendError(response, "Rol no encontrado en el token", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Collection<SimpleGrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 5️⃣ Continuar con la cadena de filtros
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            log.warn("JWT inválido o expirado: {}", e.getMessage());
            sendError(response, "Token inválido o expirado", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error inesperado en autenticación", e);
            sendError(response, "Error de autenticación", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    // ---------- Métodos auxiliares ----------

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.info("No hay cookies en la petición");
            return null;
        }

        Arrays.stream(cookies).forEach(c -> log.info("Cookie recibida: {} = {}", c.getName(), c.getValue()));

        return Arrays.stream(cookies)
                .filter(c -> AUTH_COOKIE_NAME.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private String extractTokenFromHeader(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            log.info("Token extraído del header Authorization");
            return header.substring(7);
        }
        return null;
    }

    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(String.format("{\"error\": \"%s\", \"status\": %d}", message, status));
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Endpoints públicos
        return (path.equals("/api/auth/login") && "POST".equals(method)) ||
                (path.equals("/apiUsuario/postUsuario") && "POST".equals(method));
    }
}
