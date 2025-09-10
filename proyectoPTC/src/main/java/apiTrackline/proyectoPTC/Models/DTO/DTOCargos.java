package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DTOCargos {

    private Long idCargos;

    @NotNull(message = "El ID del tipo de dato contable es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El ID de tipo de datos contables no puede ser negativa", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idTipoDatoContable;
    private String nombreTipoDatoContable;

    @NotNull(message = "El monto es obligatorio.", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "0.00", message = "El monto no puede ser negativo.", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long monto;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}

