package apiTrackline.proyectoPTC.Controllers.Auth;

import apiTrackline.proyectoPTC.Entities.UsuarioEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOUsuario;
import apiTrackline.proyectoPTC.Services.AuthService;
import apiTrackline.proyectoPTC.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;
    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    private ResponseEntity<String> login(@Valid @RequestBody DTOUsuario data, HttpServletResponse response){
        if (data.getUsuario() == null || data.getUsuario().isBlank() ||
                data.getContrasenia() == null || data.getContrasenia().isBlank()) {
            return ResponseEntity.status(401).body("Error: Credenciales incompletas");
        }

        if(service.Login(data.getUsuario(), data.getContrasenia())){
            addTokenCookie(response, data.getUsuario());
            return ResponseEntity.ok("Inicio de sesión exitoso");
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    private void addTokenCookie(HttpServletResponse response, String Usuario) {
        // Obtener el usuario completo de la base de datos
        Optional<UsuarioEntity> userOpt = service.obtenerUsuario(Usuario);

        if (userOpt.isPresent()) {
            UsuarioEntity user = userOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getIdUsuario()),
                    user.getUsuario(),
                    user.getRol().getRol() // ← Usar el nombre real del tipo
            );

            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
    }

}
