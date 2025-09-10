package apiTrackline.proyectoPTC.Controllers.FinanciamientoController;

import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionFinanciamientoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionFinanciamientoRelacionado;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionTipoFinanciamientoNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOFinanciamiento;
import apiTrackline.proyectoPTC.Services.FinanciamientoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/apiFinanciamiento")
public class Financiamiento {

    @Autowired
    private FinanciamientoService service;

    //RUTA localhost:8080/apiFinanciamiento/data
    @GetMapping("/datosFinanciamiento")
    public ResponseEntity<?> getFinanciaminetos(
            @RequestParam(defaultValue =  "0") int page,
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

        Page<DTOFinanciamiento> financiamiento = service.obtenerFinanciamiento(page, size);
        if (financiamiento == null || financiamiento.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", "No hay financiamiento registradas"
            ));
        }
        return ResponseEntity.ok(financiamiento);
    }

    //MÉTODO POST
    //RUTA: localhost:8080/apiAduana/agregarAduana
    @PostMapping("/agregarFinanciamiento")
    //Se pasan los valores de DTO al método del service
    public ResponseEntity<?> agregarFinanciamiento(@Validated(DTOFinanciamiento.OnCreate.class) @RequestBody DTOFinanciamiento json){
        try {
            DTOFinanciamiento respuesta = service.agregarFinanciamineto(json);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Error al insertar datos",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Verfique los valores de los campos"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "Éxito",
                    "data", respuesta,
                    "message", "Financiamiento creado correctamente"
            ));
        } //Cuando se ingresen los datos, no se encuentra el id del tipo de servicio (llave foránea) se captura la exception
        catch (ExceptionTipoFinanciamientoNoEncontrado noEncontrado){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status:", "Error",
                    "message", noEncontrado.getMessage()
            ));
        }
        //"Red de seguridad" para que capture todos los errores no previstos ya sea lógicos o de BD
        catch (Exception e){
            log.error("Error inesperado al agregar el financiamiento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al agregar"
            ));
        }
    }

    //MÉTODO PUT
    //RUTA: localhost:8080/apiAduana/actualizarAduana/{id}
    @PutMapping("/actualizarFinanciamiento/{id}")
    public ResponseEntity<?> actualizarFinanciamiento(@PathVariable Long id, @Validated(DTOFinanciamiento.OnUpdate.class) @RequestBody DTOFinanciamiento dtoFinanciamiento){
        try {
            //Intentar actualizar la aduana llamando al método del service
            DTOFinanciamiento financiamiento = service.actualizarFinanciamiento(id, dtoFinanciamiento);
            return ResponseEntity.ok(financiamiento);
        }
        catch (ExceptionFinanciamientoNoEncontrado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        //Cuando se ingresen los datos, no se encuentra el id del tipo de servicio (llave foránea) se captura la exception
        catch (ExceptionTipoFinanciamientoNoEncontrado noEncontrado){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status:", "Error",
                    "message", noEncontrado.getMessage()
            ));
        }
        catch (Exception e){
            log.error("Error inesperado al actualizar el financiamiento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al actualizar el financiamiento"
            ));
        }
    }


    //MÉTODO PATCH
    //RUTA: Localhost:8080/apiAduana/actualizarParcialmente/{id}
    @PatchMapping("/actualizarParcialmente/{id}")
    public ResponseEntity<?> actualizarParcialmenteFinanciamiento(@PathVariable Long id, @Validated(DTOFinanciamiento.OnPatch.class) @RequestBody DTOFinanciamiento json){
        try {
            DTOFinanciamiento actualizado = service.patchFinanciamiento(id, json);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "data", actualizado
            ));
        } catch (ExceptionFinanciamientoNoEncontrado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionTipoFinanciamientoNoEncontrado exeption){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "Error: LLave foránea no encontrada",
                    "message", exeption.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "Error de validación",
                    "message", e.getMessage()
            ));
        }
        catch (Exception exceptionCodigo){
            log.error("Error inesperado al actualizar parcialmente el financiamiento", exceptionCodigo);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al editar parcialmente"
            ));
        }
    }

    //MÉTODO DELETE
    //RUTA: localhost:8080/apiAduana/eliminarAduana/{id}
    @DeleteMapping("/eliminarFinanciamiento/{id}")
    public ResponseEntity<?> eliminarFinanciamiento(@PathVariable Long id){
        try {
            service.eliminarFinanciamiento(id);
            return ResponseEntity.ok(Map.of(
                    "status", "Éxito",
                    "message", "El financiamiento ha sido eliminada correctamente"
            ));
        } catch (ExceptionFinanciamientoNoEncontrado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status:", "Error",
                    "message", e.getMessage()
            ));
        } catch (ExceptionFinanciamientoRelacionado e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "Error",
                    "message", "El financiamiento no se pudo eliminar porque tiene registros relacionados"
            ));
        } catch (Exception e){
            log.error("Error inesperado al eliminar el financiamiento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "Error no controlado",
                    "message", "Error no controlado al eliminar"
            ));
        }
    }
}


