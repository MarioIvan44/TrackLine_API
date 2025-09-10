package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DTOFinanciamiento {

    private Long idFinanciamiento;

    @NotNull(message = "El ID del tipo de financiamiento es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El id tipo financiamiento no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnUpdate.class})
    private Long idTipoFinanciamiento;
    private String nombreTipoFinanciamiento;

    @NotNull(message = "El monto es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "0.00", message = "El monto no puede ser negativo.", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long monto;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}
