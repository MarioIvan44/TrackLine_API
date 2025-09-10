package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.*;
import apiTrackline.proyectoPTC.Exceptions.EstadosExceptions.ExceptionOrdenServicioNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.TransporteExceptions.ExceptionTransporteNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.ViajeExceptions.ExceptionViajeNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.ViajeExceptions.ExceptionViajeNoRegistrado;
import apiTrackline.proyectoPTC.Exceptions.ViajeExceptions.ExceptionViajeRelacionada;
import apiTrackline.proyectoPTC.Models.DTO.DTOViaje;
import apiTrackline.proyectoPTC.Repositories.ViajeRepository;
import apiTrackline.proyectoPTC.Repositories.OrdenServicioRepository;
import apiTrackline.proyectoPTC.Repositories.TransporteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ViajeService {

    @Autowired
    private ViajeRepository repo;

    @Autowired
    private OrdenServicioRepository ordenRepo;

    @Autowired
    private TransporteRepository transporteRepo;

    // Obtener todos los viajes
    public List<DTOViaje> getAll() {
        List<ViajeEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Convertir a DTO
    private DTOViaje convertirADTO(ViajeEntity entity) {
        DTOViaje dto = new DTOViaje();
        dto.setIdViaje(entity.getIdViaje());

        // --- ORDEN SERVICIO ---
        OrdenServicioEntity orden = entity.getOrdenServicio();
        if (orden != null) {
            dto.setIdOrdenServicio(orden.getIdOrdenServicio());
            dto.setClienteNIT(orden.getClienteNIT());

            if (orden.getCliente() != null) {
                dto.setNombreCliente(orden.getCliente().getNombre());
                dto.setApellidoCliente(orden.getCliente().getApellido());
                dto.setTelefonoCliente(orden.getCliente().getTelefono());
                dto.setCorreoCliente(orden.getCliente().getCorreo());
                dto.setCodEmpresaCliente(orden.getCliente().getCodEmpresa());

                if (orden.getCliente().getUsuario() != null) {
                    dto.setIdUsuarioCliente(orden.getCliente().getUsuario().getIdUsuario());
                    dto.setNombreUsuarioCliente(orden.getCliente().getUsuario().getUsuario());
                }
            }
        }

        // --- TRANSPORTE DEL VIAJE ---
        TransporteEntity transporte = entity.getTransporte();
        if (transporte != null) {
            dto.setIdTransporteViaje(transporte.getIdTransporte());

            if (transporte.getTransportista() != null) {
                var t = transporte.getTransportista();
                dto.setIdTransportistaViaje(t.getIdTransportista());
                dto.setNombreTransportistaViaje(t.getNombre());
                dto.setApellidoTransportistaViaje(t.getApellido());
                dto.setTelefonoTransportistaViaje(t.getTelefono());
                dto.setCorreoTransportistaViaje(t.getCorreo());
                dto.setNitTransportistaViaje(t.getNit());

                if (t.getUsuarioT() != null) {
                    dto.setIdUsuarioTransportistaViaje(t.getUsuarioT().getIdUsuario());
                    dto.setNombreUsuarioTransportistaViaje(t.getUsuarioT().getUsuario());
                }
            }

            if (transporte.getServicioTransporte() != null) {
                var st = transporte.getServicioTransporte();
                dto.setIdServicioTransporteViaje(st.getIdServicioTransporte());
                dto.setPlacaServicioViaje(st.getPlaca());
                dto.setTarjetaCirculacionServicioViaje(st.getTarjetaCirculacion());
                dto.setCapacidadServicioViaje(st.getCapacidad());
            }
        }

        return dto;
    }

    // Crear viaje
    public DTOViaje create(Long idOrdenServicio, Long idTransporte) {
        try {
            OrdenServicioEntity orden = ordenRepo.findById(idOrdenServicio)
                    .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado(
                            "No se encontró orden de servicio con id: " + idOrdenServicio));

            TransporteEntity transporte = transporteRepo.findById(idTransporte)
                    .orElseThrow(() -> new ExceptionTransporteNoEncontrado(
                            "No se encontró transporte con id: " + idTransporte));

            ViajeEntity viaje = new ViajeEntity();
            viaje.setOrdenServicio(orden);
            viaje.setTransporte(transporte);

            ViajeEntity guardado = repo.save(viaje);
            return convertirADTO(guardado);

        } catch (ExceptionOrdenServicioNoEncontrado | ExceptionTransporteNoEncontrado e) {
            throw e; //
        } catch (Exception e) {
            log.error("Error al crear viaje", e);
            throw new ExceptionViajeNoRegistrado("Error inesperado al registrar viaje");
        }
    }

    // Eliminar viaje
    public String delete(Long id) {
        ViajeEntity viaje = repo.findById(id)
                .orElseThrow(() -> new ExceptionViajeNoEncontrado("No se encontró viaje con id: " + id));
        try {
            repo.delete(viaje);
            return "Viaje eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionViajeRelacionada("No se pudo eliminar el viaje porque tiene registros relacionados");
        }
    }

    // Actualización parcial (PATCH)
    public DTOViaje patch(Long id, Long idOrdenServicio, Long idTransporte) {
        ViajeEntity viaje = repo.findById(id)
                .orElseThrow(() -> new ExceptionViajeNoEncontrado("No se encontró viaje con id: " + id));

        if (idOrdenServicio != null) {
            OrdenServicioEntity orden = ordenRepo.findById(idOrdenServicio)
                    .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado("No se encontró orden de servicio con id: " + idOrdenServicio));
            viaje.setOrdenServicio(orden);
        }

        if (idTransporte != null) {
            TransporteEntity transporte = transporteRepo.findById(idTransporte)
                    .orElseThrow(() -> new ExceptionTransporteNoEncontrado("No se encontró transporte con id: " + idTransporte));
            viaje.setTransporte(transporte);
        }

        return convertirADTO(repo.save(viaje));
    }

    // Actualización total (PUT)
    public DTOViaje putUpdate(Long id, Long idOrdenServicio, Long idTransporte) {
        if (idOrdenServicio == null || idTransporte == null) {
            throw new IllegalArgumentException("Se requiere ambos campos: idOrdenServicio e idTransporte");
        }

        ViajeEntity viaje = repo.findById(id)
                .orElseThrow(() -> new ExceptionViajeNoEncontrado("No se encontró viaje con id: " + id));

        OrdenServicioEntity orden = ordenRepo.findById(idOrdenServicio)
                .orElseThrow(() -> new ExceptionOrdenServicioNoEncontrado("No se encontró orden de servicio con id: " + idOrdenServicio));

        TransporteEntity transporte = transporteRepo.findById(idTransporte)
                .orElseThrow(() -> new ExceptionTransporteNoEncontrado("No se encontró transporte con id: " + idTransporte));

        viaje.setOrdenServicio(orden);
        viaje.setTransporte(transporte);

        return convertirADTO(repo.save(viaje));
    }

    // Buscar por ID
    public DTOViaje buscarPorId(Long id) {
        ViajeEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionViajeNoEncontrado("No se encontró viaje con id: " + id));
        return convertirADTO(entity);
    }
}
