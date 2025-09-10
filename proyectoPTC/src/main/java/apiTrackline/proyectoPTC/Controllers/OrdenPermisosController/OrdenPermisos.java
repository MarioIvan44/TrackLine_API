package apiTrackline.proyectoPTC.Controllers.OrdenPermisosController;

import apiTrackline.proyectoPTC.Models.DTO.DTOOrdenPermisos;
import apiTrackline.proyectoPTC.Services.OrdenPermisosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/apiOrdenPermisos")
@CrossOrigin
public class OrdenPermisos {

    @Autowired
    private OrdenPermisosService service;

    //METODO GET
    //RUTA: localhost:8080/apiOrdenPermisos/datosAduana
    @GetMapping("/obtenerDatos")
    public List<DTOOrdenPermisos> obtenerDatos() {
        return service.getData();
    }


    //METODO POST
    //RUTA: localhost:8080/apiOrdenPermisos/agregarOrden
    @PostMapping("/agregarOrden")
    public String agregarOrdenPermisos(@Valid @RequestBody DTOOrdenPermisos dto) {
        return service.post(dto);
    }

    //METODO PUT
    //RUTA: localhost:8080/apiOrdenPermisos/actualizarOrden/id
    @PutMapping("/actualizarOrden/{id}")
    public String actualizarOrdenPermisos(@PathVariable Long id, @RequestBody DTOOrdenPermisos dto) {
        return service.update(id, dto);
    }

    //METODO PATCH
    //RUTA: localhost:8080/apiOrdenPermisos/actualizarParcialmente/id
    @PatchMapping("/actualizarParcialmente/{id}")
    public String actualizarParcialOrdenPermisos(@PathVariable Long id, @RequestBody DTOOrdenPermisos dto) {
        return service.patch(id, dto);
    }

    //METODO DELETE
    //RUTA: localhost:8080/apiOrdenPermisos/eliminarOrden/id
    @DeleteMapping("/eliminarOrden/{id}")
    public String eliminarOrdenPermisos(@PathVariable Long id) {
        return service.delete(id);
    }
}
