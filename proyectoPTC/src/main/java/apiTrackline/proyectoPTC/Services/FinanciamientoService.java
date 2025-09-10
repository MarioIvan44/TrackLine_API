package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.*;
import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionAduanaNoRegistrada;
import apiTrackline.proyectoPTC.Exceptions.AduanaExceptions.ExceptionAduanaRelacionada;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionFinanciamientoNoEncontrado;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionFinanciamientoRelacionado;
import apiTrackline.proyectoPTC.Exceptions.FinanciamientoExceptions.ExceptionTipoFinanciamientoNoEncontrado;
import apiTrackline.proyectoPTC.Models.DTO.DTOFinanciamiento;
import apiTrackline.proyectoPTC.Repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
@Slf4j
@Service
public class FinanciamientoService {

    @Autowired
    private FinanciamientoRepository repo;

    @Autowired
    private TipoFinanciamientosRepository tipoRepo;

    //El metodo pide una lista porque en el front end solo se puede mostrar un DTO
    public Page<DTOFinanciamiento> obtenerFinanciamiento(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FinanciamientoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
        //TODO LO QUE SALE DE LA BASE SALE COMO ENTIDAD
        //TODO LO QUE ENTRA A LA BASE DEBE ENTRAR COMO ENTIDAD
    }

    private DTOFinanciamiento convertirADTO(FinanciamientoEntity financiamientoEntity) {
        DTOFinanciamiento dto = new DTOFinanciamiento();
        dto.setIdFinanciamiento(financiamientoEntity.getIdFinanciamiento());
        dto.setMonto(financiamientoEntity.getMonto());
        if (financiamientoEntity.getTipoFinanciamiento() != null) {
            dto.setIdTipoFinanciamiento(financiamientoEntity.getTipoFinanciamiento().getIdTipoFinanciamiento());
            dto.setNombreTipoFinanciamiento(financiamientoEntity.getTipoFinanciamiento().getNombre());
        }
        return dto;
    }

    public DTOFinanciamiento agregarFinanciamineto(DTOFinanciamiento json) {
        if (json == null) {
            throw new IllegalArgumentException("No puedes agregar un registro sin datos.");
        }
        //Buscar el tipo de financiamiento por el ID que viene en el DTO
        TipoFinanciamientosEntity tipoFinanciamiento = tipoRepo.findById(json.getIdTipoFinanciamiento())
                .orElseThrow(() -> new ExceptionTipoFinanciamientoNoEncontrado("Tipo financiamiento no encontrado con id " + json.getIdTipoFinanciamiento()));
        try {
            FinanciamientoEntity entity = new FinanciamientoEntity();
            entity.setTipoFinanciamiento(tipoFinanciamiento);
            entity.setMonto(json.getMonto());

            FinanciamientoEntity financiamientoCreado = repo.save(entity);
            return convertirADTO(financiamientoCreado);
        } catch (Exception e) {
            log.error("Error al registrar el financiamiento: " + e.getMessage());
            throw new ExceptionAduanaNoRegistrada("Error: Financiamiento no registrado");
        }
    }

    public DTOFinanciamiento actualizarFinanciamiento(Long id, DTOFinanciamiento dto) {
        FinanciamientoEntity financiamiento = repo.findById(id)
                .orElseThrow(() -> new ExceptionFinanciamientoNoEncontrado("Financiamiento no encontrado con id " + id));

        financiamiento.setMonto(dto.getMonto());

        if (dto.getIdTipoFinanciamiento() != null) {
            TipoFinanciamientosEntity tipoFinanciamiento = tipoRepo.findById(dto.getIdTipoFinanciamiento())
                    .orElseThrow(() -> new ExceptionTipoFinanciamientoNoEncontrado("Tipo financiamiento no encontrado con id " + dto.getIdTipoFinanciamiento()));
            financiamiento.setTipoFinanciamiento(tipoFinanciamiento);
        }
        return convertirADTO(repo.save(financiamiento));
    }


    public DTOFinanciamiento patchFinanciamiento(Long id, DTOFinanciamiento json) {
        FinanciamientoEntity financiamiento = repo.findById(id)
                .orElseThrow(() -> new ExceptionFinanciamientoNoEncontrado("Financiamiento no encontrada con id " + id));

        if (json.getMonto() != null) financiamiento.setMonto(json.getMonto());

        if (json.getIdTipoFinanciamiento() != null) {
            TipoFinanciamientosEntity tipoFinanciamiento = tipoRepo.findById(json.getIdFinanciamiento())
                    .orElseThrow(() -> new ExceptionTipoFinanciamientoNoEncontrado(
                            "Tipo financiamiento no encontrado con id " + json.getIdFinanciamiento()));
            financiamiento.setIdFinanciamiento(tipoFinanciamiento.getIdTipoFinanciamiento());
        }
        return convertirADTO(repo.save(financiamiento));
    }

    public String eliminarFinanciamiento(Long id) {
        FinanciamientoEntity financiamiento = repo.findById(id)
                .orElseThrow(() -> new ExceptionFinanciamientoNoEncontrado("Financiamineto no encontrado con id " + id));
        try {
            repo.delete(financiamiento);
            return "Financiamineto eliminado correctamente";
        } catch (DataIntegrityViolationException e) {
            throw new ExceptionFinanciamientoRelacionado("No se pudo eliminar el financiamiento porque tiene registros relacionados");
        }
    }
}