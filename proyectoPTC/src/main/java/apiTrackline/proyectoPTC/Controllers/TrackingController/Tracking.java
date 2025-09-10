package apiTrackline.proyectoPTC.Controllers.TrackingController;

import apiTrackline.proyectoPTC.Exceptions.TrackingExceptions.*;
import apiTrackline.proyectoPTC.Models.DTO.DTOAduana;
import apiTrackline.proyectoPTC.Models.DTO.DTOTracking;
import apiTrackline.proyectoPTC.Services.TrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/apiTracking")
@CrossOrigin
public class Tracking {

    @Autowired
    private TrackingService service;

    // MÉTODO GET POR ID
    // RUTA: localhost:8080/apiTracking/obtenerTrackingPorId/{id}
    @GetMapping("/obtenerTrackingPorId/{id}")
    public ResponseEntity<?> obtenerTrackingPorId(@PathVariable Long id) {
        try {
            DTOTracking tracking = service.buscarTrackingPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", tracking
            ));
        } catch (ExceptionTrackingNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al obtener tracking por id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error inesperado al buscar tracking por ID"
            ));
        }
    }


    // MÉTODO GET
    // RUTA: localhost:8080/apiTracking/obtenerTrackings
    @GetMapping("/obtenerTrackings")
    public ResponseEntity<?> obtenerTrackings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        if (page < 0) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Error de validación",
                    "message", "El número de página no puede ser negativo"
            ));
        }

        if (size <= 0 || size > 50) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Error de validación",
                    "message", "El tamaño de la página debe estar entre 1 y 50"
            ));
        }

        Page<DTOTracking> trackings = service.obtenerTrackings(page, size);
        if (trackings == null || trackings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay trackings registradas"
            ));
        }
        return ResponseEntity.ok(trackings);
    }



    // MÉTODO POST
    // RUTA: localhost:8080/apiTracking/agregarTracking
    @PostMapping("/agregarTracking")
    public ResponseEntity<?> agregarTracking(@Validated(DTOTracking.OnCreate.class) @RequestBody DTOTracking json) {
        try {
            DTOTracking respuesta = service.agregarTracking(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Tracking creado correctamente"
            ));
        } catch (ExceptionViajeNoEncontrado | ExceptionEstadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionTrackingNoRegistrado e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error al registrar",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al agregar tracking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error interno",
                    "message", "Error no controlado al registrar tracking"
            ));
        }
    }

    // MÉTODO PUT
    // RUTA: localhost:8080/apiTracking/actualizarTracking/{id}
    @PutMapping("/actualizarTracking/{id}")
    public ResponseEntity<?> actualizarTracking(@PathVariable Long id, @Validated(DTOTracking.OnUpdate.class) @RequestBody DTOTracking dtoTracking) {
        try {
            DTOTracking tracking = service.actualizarTracking(id, dtoTracking);
            return ResponseEntity.ok(tracking);
        } catch (ExceptionTrackingNoEncontrado | ExceptionViajeNoEncontrado | ExceptionEstadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar tracking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al actualizar tracking"
            ));
        }
    }

    // MÉTODO PATCH
    // RUTA: localhost:8080/apiTracking/actualizarParcialmente/{id}
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> actualizarParcialmenteTracking(@PathVariable Long id, @Validated(DTOTracking.OnPatch.class) @RequestBody DTOTracking json) {
        try {
            DTOTracking actualizado = service.patchTracking(id, json);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionTrackingNoEncontrado | ExceptionViajeNoEncontrado | ExceptionEstadoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error inesperado al actualizar parcialmente tracking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al editar parcialmente tracking"
            ));
        }
    }

    // MÉTODO DELETE
    // RUTA: localhost:8080/apiTracking/eliminarTracking/{id}
    @DeleteMapping("/eliminarTracking/{id}")
    public ResponseEntity<?> eliminarTracking(@PathVariable Long id) {
        try {
            service.eliminarTracking(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "El tracking ha sido eliminado correctamente"
            ));
        } catch (ExceptionTrackingNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionTrackingRelacionado e) {
            log.error("Error al eliminar tracking", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", "El tracking no se pudo eliminar porque tiene registros relacionados"
            ));
        } catch (Exception e) {
            log.error("Error inesperado al eliminar tracking", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar tracking"
            ));
        }
    }
}
