package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class DTOTracking {

    private Long idTracking;

    // Campos relacionados con Viaje
    @Positive(message = "El id de viaje no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    @NotNull(message = "El id de transporte no puede estar vacío", groups = {OnCreate.class, OnUpdate.class})
    private Long idViaje;
    // Desde Viaje -> Transporte (solo campos necesarios)
    private Long idTransporte;
    // Desde Transporte -> ServicioTransporte (solo campos necesarios)
    private Long idServicioTransporte;
    private String placaServicio;
    private String tarjetaCirculacionServicio;
    private String capacidadServicio;


    // Estado
    @NotNull(message = "El id de estado no puede estar vacío", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El id de estado no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idEstado;
    private Boolean documentos;
    private Boolean clasificacion;
    private Boolean digitacion;
    private Boolean registro;
    private Boolean pago;
    //Estado --> selectivo
    private Long idSelectivo;
    private String colorSelectivo;
    private Boolean levantePago;
    private Boolean equipoTransporte;
    private Boolean carga;
    private Boolean enCamino;
    private Boolean entregada;
    private Boolean facturacion;

    // Campos propios Tracking
    private Timestamp horaEstimadaPartida;
    private Timestamp horaEstimadaLlegada;
    private Timestamp horaSalida;
    private Timestamp horaLlegada;

    @NotBlank(message = "El lugar de partida no puede estar vacío", groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 5, max = 70, message = "El lugar de partida debe tener entre 5 y 70 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String lugarPartida;
    @NotBlank(message = "El lugar de llegada no puede estar vacío", groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 5, max = 70, message = "El lugar de llegada debe tener entre 5 y 70 caracteres", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private String lugarLlegada;

    @Positive(message = "El progreso no puede ser negativo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long progreso;

    public interface OnCreate{}
    public interface OnUpdate{}
    public interface OnPatch{}
}
