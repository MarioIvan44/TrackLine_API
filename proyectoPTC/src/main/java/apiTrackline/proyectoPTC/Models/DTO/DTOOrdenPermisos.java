package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DTOOrdenPermisos {
    private Long idOrdenPermisos;


    //Llave foranea Permisos
    @Positive(message = "El id de Permiso no puede ser negativo")
    private Long idPermiso;
    private String nombrePermiso;

    private Boolean marcado;
}
