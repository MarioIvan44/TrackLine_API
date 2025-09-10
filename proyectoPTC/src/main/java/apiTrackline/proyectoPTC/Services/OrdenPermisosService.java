package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.OrdenPermisosEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOOrdenPermisos;
import apiTrackline.proyectoPTC.Repositories.OrdenPermisosRepository;
import apiTrackline.proyectoPTC.Repositories.PermisosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdenPermisosService {
    @Autowired
    private OrdenPermisosRepository repository;

    @Autowired
    private PermisosRepository permisoRepository;

    public List<DTOOrdenPermisos> getData() {
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public String post(DTOOrdenPermisos dto) {
        OrdenPermisosEntity entity = convertToEntity(dto);

        if (dto.getIdPermiso() != null && permisoRepository.findById(dto.getIdPermiso()).isEmpty()) {
            return "Permiso no encontrado. No se pudo guardar la orden.";
        }

        repository.save(entity);
        return "Orden creada correctamente.";
    }



    public String update(Long id, DTOOrdenPermisos dto) {
        Optional<OrdenPermisosEntity> optional = repository.findById(id);
        if (optional.isPresent()) {
            OrdenPermisosEntity entity = optional.get();
            entity.setMarcado(dto.getMarcado());

            if (dto.getIdPermiso() != null) {
                Optional<?> permiso = permisoRepository.findById(dto.getIdPermiso());
                if (permiso.isEmpty()) {
                    return "Permiso no encontrado. No se pudo actualizar la orden.";
                }
                entity.setIdPermiso((apiTrackline.proyectoPTC.Entities.PermisosEntity) permiso.get());
            }

            repository.save(entity);
            return "Orden actualizada correctamente.";
        } else {
            return "Orden no encontrada.";
        }
    }

    public String patch(Long id, DTOOrdenPermisos dto) {
        return update(id, dto); // misma lógica de validación y mensaje
    }

    public String delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Orden eliminada correctamente.";
        } else {
            return "Orden no encontrada.";
        }
    }


    private DTOOrdenPermisos convertToDTO(OrdenPermisosEntity entity) {
        DTOOrdenPermisos dto = new DTOOrdenPermisos();
        dto.setIdOrdenPermisos(entity.getIdOrdenPermisos());
        dto.setMarcado(entity.getMarcado());

        if (entity.getIdPermiso() != null) {
            dto.setIdPermiso(entity.getIdPermiso().getIdPermiso());
            dto.setNombrePermiso(entity.getIdPermiso().getNombrePermiso());
        }
        return dto;
    }

    private OrdenPermisosEntity convertToEntity(DTOOrdenPermisos dto) {
        OrdenPermisosEntity entity = new OrdenPermisosEntity();
        entity.setIdOrdenPermisos(dto.getIdOrdenPermisos());
        entity.setMarcado(dto.getMarcado());

        if (dto.getIdPermiso() != null) {
            permisoRepository.findById(dto.getIdPermiso()).ifPresent(entity::setIdPermiso);
        }

        return entity;
    }
}