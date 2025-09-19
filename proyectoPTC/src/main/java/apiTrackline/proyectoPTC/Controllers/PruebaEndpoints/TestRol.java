package apiTrackline.proyectoPTC.Controllers.PruebaEndpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

    @RestController
    @RequestMapping("api/test")
    public class TestRol {

        @GetMapping("/cliente-only")
        @PreAuthorize("hasAuthority('ROLE_Cliente')")
        public ResponseEntity<?> clienteEndPoint(){
            return ResponseEntity.ok("Exito: Acceso de cliente");
        }

        @GetMapping("/admin-only")
        @PreAuthorize("hasAuthority('ROLE_Administrador')")
        public ResponseEntity<?> adminEndPoint(){
            return ResponseEntity.ok("Exito: Acceso de Administrador");
        }

        @GetMapping("/Transportista-only")
        @PreAuthorize("hasAuthority('ROLE_Transportista')")
        public ResponseEntity<?> AlmacenistaEndPoint(){
            return ResponseEntity.ok("Exito: Acceso de almacenista");
        }

        @GetMapping("/admin-cliente-only")
        @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Cliente')")
        public ResponseEntity<?> clienteAdminEndPoint(){
            return ResponseEntity.ok("Exito: Acceso para administradores y clientes");
        }

        @GetMapping("/Transportista-cliente-only")
        @PreAuthorize("hasAnyAuthority('ROLE_Transportista', 'ROLE_Cliente')")
        public ResponseEntity<?> TransportitaClienteEndpoint(){
            return ResponseEntity.ok("Exito: Acceso para Transportitas y clientes");
        }

        @GetMapping("/debug-auth")
        public ResponseEntity<?> debugAuth(Authentication authentication) {
            return ResponseEntity.ok(Map.of(
                    "name", authentication.getName(),
                    "authorities", authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()),
                    "authenticated", authentication.isAuthenticated()
            ));
        }
    }


