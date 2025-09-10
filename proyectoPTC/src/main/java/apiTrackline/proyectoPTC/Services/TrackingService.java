package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.*;
import apiTrackline.proyectoPTC.Exceptions.TrackingExceptions.*;
import apiTrackline.proyectoPTC.Models.DTO.DTOAduana;
import apiTrackline.proyectoPTC.Models.DTO.DTOTipoServicio;
import apiTrackline.proyectoPTC.Models.DTO.DTOTracking;
import apiTrackline.proyectoPTC.Repositories.EstadosRepository;
import apiTrackline.proyectoPTC.Repositories.TrackingRepository;
import apiTrackline.proyectoPTC.Repositories.ViajeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrackingService {

    @Autowired
    private TrackingRepository repo;

    @Autowired
    private ViajeRepository viajeRepo;

    @Autowired
    private EstadosRepository estadosRepo;

    // MÉTODOS PRINCIPALES

    //El metodo pide una lista porque en el front end solo se puede mostrar un DTO
    public Page<DTOTracking> obtenerTrackings(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<TrackingEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirATrackingDTO);
        //TODO LO QUE SALE DE LA BASE SALE COMO ENTIDAD
        //TODO LO QUE ENTRA A LA BASE DEBE ENTRAR COMO ENTIDAD
    }

    // Convertir de Entity → DTO
    private DTOTracking convertirATrackingDTO(TrackingEntity entity) {
        DTOTracking dto = new DTOTracking();
        dto.setIdTracking(entity.getIdTracking());

        // --- Viaje
        if (entity.getViaje() != null) {
            dto.setIdViaje(entity.getViaje().getIdViaje());

            if (entity.getViaje().getTransporte() != null) {
                dto.setIdTransporte(entity.getViaje().getTransporte().getIdTransporte());

                if (entity.getViaje().getTransporte().getServicioTransporte() != null) {
                    var servicio = entity.getViaje().getTransporte().getServicioTransporte();
                    dto.setIdServicioTransporte(servicio.getIdServicioTransporte());
                    dto.setPlacaServicio(servicio.getPlaca());
                    dto.setTarjetaCirculacionServicio(servicio.getTarjetaCirculacion());
                    dto.setCapacidadServicio(servicio.getCapacidad());
                }
            }
        }

        // --- Estado
        if (entity.getEstado() != null) {
            dto.setIdEstado(entity.getEstado().getIdEstado());
            dto.setDocumentos(entity.getEstado().getDocumentos());
            dto.setClasificacion(entity.getEstado().getClasificacion());
            dto.setDigitacion(entity.getEstado().getDigitacion());
            dto.setRegistro(entity.getEstado().getRegistro());
            dto.setPago(entity.getEstado().getPago());
            if (entity.getEstado().getSelectivo() != null) {
                dto.setIdSelectivo(entity.getEstado().getSelectivo().getIdSelectivo());
                dto.setColorSelectivo(entity.getEstado().getSelectivo().getColorSelectivo());
            }
            dto.setLevantePago(entity.getEstado().getLevantePago());
            dto.setEquipoTransporte(entity.getEstado().getEquipoTransporte());
            dto.setCarga(entity.getEstado().getCarga());
            dto.setEnCamino(entity.getEstado().getEnCamino());
            dto.setEntregada(entity.getEstado().getEntregada());
            dto.setFacturacion(entity.getEstado().getFacturacion());
        }

        // --- Tracking propio
        dto.setHoraEstimadaPartida(entity.getHoraEstimadaPartida());
        dto.setHoraEstimadaLlegada(entity.getHoraEstimadaLlegada());
        dto.setHoraSalida(entity.getHoraSalida());
        dto.setHoraLlegada(entity.getHoraLlegada());
        dto.setLugarPartida(entity.getLugarPartida());
        dto.setLugarLlegada(entity.getLugarLlegada());

        return dto;
    }

    // CREATE
    public DTOTracking agregarTracking(DTOTracking dto) {
        if (dto == null) throw new IllegalArgumentException("No puedes agregar un tracking vacío");

        ViajeEntity viaje = viajeRepo.findById(dto.getIdViaje())
                .orElseThrow(() -> new ExceptionViajeNoEncontrado("Viaje no encontrado con id " + dto.getIdViaje()));

        EstadosEntity estado = estadosRepo.findById(dto.getIdEstado())
                .orElseThrow(() -> new ExceptionEstadoNoEncontrado("Estado no encontrado con id " + dto.getIdEstado()));

        try {
            TrackingEntity entity = new TrackingEntity();
            entity.setViaje(viaje);
            entity.setEstado(estado);

            entity.setHoraEstimadaPartida(dto.getHoraEstimadaPartida());
            entity.setHoraEstimadaLlegada(dto.getHoraEstimadaLlegada());
            entity.setHoraSalida(dto.getHoraSalida());
            entity.setHoraLlegada(dto.getHoraLlegada());
            entity.setLugarPartida(dto.getLugarPartida());
            entity.setLugarLlegada(dto.getLugarLlegada());
            entity.setProgreso(null);
            TrackingEntity creado = repo.save(entity);
            return convertirATrackingDTO(creado);

        } catch (Exception e) {
            log.error("Error al registrar el tracking: " + e);
            throw new ExceptionTrackingNoRegistrado("Error: tracking no registrado");
        }
    }

    // UPDATE (PUT)
    public DTOTracking actualizarTracking(Long id, DTOTracking dto) {
        TrackingEntity tracking = repo.findById(id)
                .orElseThrow(() -> new ExceptionTrackingNoEncontrado("Tracking no encontrado con id " + id));

        ViajeEntity viaje = viajeRepo.findById(dto.getIdViaje())
                .orElseThrow(() -> new ExceptionViajeNoEncontrado("Viaje no encontrado con id " + dto.getIdViaje()));

        EstadosEntity estado = estadosRepo.findById(dto.getIdEstado())
                .orElseThrow(() -> new ExceptionEstadoNoEncontrado("Estado no encontrado con id " + dto.getIdEstado()));

        tracking.setViaje(viaje);
        tracking.setEstado(estado);

        tracking.setHoraEstimadaPartida(dto.getHoraEstimadaPartida());
        tracking.setHoraEstimadaLlegada(dto.getHoraEstimadaLlegada());
        tracking.setHoraSalida(dto.getHoraSalida());
        tracking.setHoraLlegada(dto.getHoraLlegada());
        tracking.setLugarPartida(dto.getLugarPartida());
        tracking.setLugarLlegada(dto.getLugarLlegada());
        tracking.setProgreso(dto.getProgreso());
        return convertirATrackingDTO(repo.save(tracking));
    }

    // ================== PATCH ==================
    public DTOTracking patchTracking(Long id, DTOTracking dto) {
        TrackingEntity tracking = repo.findById(id)
                .orElseThrow(() -> new ExceptionTrackingNoEncontrado("Tracking no encontrado con id " + id));

        if (dto.getIdViaje() != null) {
            ViajeEntity viaje = viajeRepo.findById(dto.getIdViaje())
                    .orElseThrow(() -> new ExceptionViajeNoEncontrado("Viaje no encontrado con id " + dto.getIdViaje()));
            tracking.setViaje(viaje);
        }

        if (dto.getIdEstado() != null) {
            EstadosEntity estado = estadosRepo.findById(dto.getIdEstado())
                    .orElseThrow(() -> new ExceptionEstadoNoEncontrado("Estado no encontrado con id " + dto.getIdEstado()));
            tracking.setEstado(estado);
        }

        if (dto.getHoraEstimadaPartida() != null) tracking.setHoraEstimadaPartida(dto.getHoraEstimadaPartida());
        if (dto.getHoraEstimadaLlegada() != null) tracking.setHoraEstimadaLlegada(dto.getHoraEstimadaLlegada());
        if (dto.getHoraSalida() != null) tracking.setHoraSalida(dto.getHoraSalida());
        if (dto.getHoraLlegada() != null) tracking.setHoraLlegada(dto.getHoraLlegada());
        if (dto.getLugarPartida() != null) tracking.setLugarPartida(dto.getLugarPartida());
        if (dto.getLugarLlegada() != null) tracking.setLugarLlegada(dto.getLugarLlegada());
        if (dto.getProgreso() != null) tracking.setProgreso(dto.getProgreso());

        return convertirATrackingDTO(repo.save(tracking));
    }

    // DELETE
    public String eliminarTracking(Long id) {
        TrackingEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionTrackingNoEncontrado("Tracking no encontrado con id " + id));
        try {
            repo.delete(entity);
            return "Tracking eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionTrackingRelacionado("No se pudo eliminar el tracking porque tiene registros relacionados");
        }
    }

    // GET BY ID
    public DTOTracking buscarTrackingPorId(Long id) {
        TrackingEntity entity = repo.findById(id)
                .orElseThrow(() -> new ExceptionTrackingNoEncontrado("No se encontró tracking con ID: " + id));
        return convertirATrackingDTO(entity);
    }
}
