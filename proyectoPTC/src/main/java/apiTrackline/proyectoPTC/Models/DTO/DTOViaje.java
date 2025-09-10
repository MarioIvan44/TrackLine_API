package apiTrackline.proyectoPTC.Models.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Getter
@Setter
public class DTOViaje {

    private Long idViaje;

    // ------- ORDEN DE SERVICIO -------
    @NotNull(message = "El ID de orden de servicio es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El ID de orden de servicio debe ser positivo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idOrdenServicio;
    private String clienteNIT;
    private String nombreCliente;
    private String apellidoCliente;
    private String telefonoCliente;
    private String correoCliente;
    private String codEmpresaCliente;
    private Long idUsuarioCliente;
    private String nombreUsuarioCliente;

    private Long idOrdenEncabezado;
    private LocalDate fechaOrden;
    private String encargadoUno;
    private String referencia;
    private String importador;
    private String nitUno;
    private String registroIvaUno;
    private String facturaA;
    private String encargadoDos;
    private String nitDos;
    private String registroIvaDos;

    private Long idAduana;
    private String dm;
    private String primeraModalidad;
    private String segundaModalidad;
    private String digitador;
    private String tramitador;
    private Long idTipoServicio;
    private String nombreTipoServicio;

    private Long idTransporteOrdenServicio; // transporte asignado en la orden
    private Long idTransportistaOrden;
    private String nombreTransportistaOrden;
    private String apellidoTransportistaOrden;
    private String telefonoTransportistaOrden;
    private String correoTransportistaOrden;
    private String nitTransportistaOrden;
    private Long idUsuarioTransportistaOrden;
    private String nombreUsuarioTransportistaOrden;
    private Long idServicioTransporteOrden;
    private String placaServicioOrden;
    private String tarjetaCirculacionServicioOrden;
    private String capacidadServicioOrden;

    // ------- TRANSPORTE DEL VIAJE -------
    // Relaciones: Transporte
    @NotNull(message = "El ID de transporte es obligatorio", groups = {OnCreate.class, OnUpdate.class})
    @Positive(message = "El ID de transporte debe ser positivo", groups = {OnCreate.class, OnUpdate.class, OnPatch.class})
    private Long idTransporteViaje;
    private Long idTransportistaViaje;
    private String nombreTransportistaViaje;
    private String apellidoTransportistaViaje;
    private String telefonoTransportistaViaje;
    private String correoTransportistaViaje;
    private String nitTransportistaViaje;
    private Long idUsuarioTransportistaViaje;
    private String nombreUsuarioTransportistaViaje;
    private Long idServicioTransporteViaje;
    private String placaServicioViaje;
    private String tarjetaCirculacionServicioViaje;
    private String capacidadServicioViaje;

    public interface OnCreate {}
    public interface OnUpdate {}
    public interface OnPatch {}
}
