package apiTrackline.proyectoPTC.Services;

import apiTrackline.proyectoPTC.Entities.CargosEntity;
import apiTrackline.proyectoPTC.Entities.TipoDatoContableEntity;
import apiTrackline.proyectoPTC.Models.DTO.DTOCargos;
import apiTrackline.proyectoPTC.Repositories.CargosRepository;
import apiTrackline.proyectoPTC.Repositories.TipoDatoContableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CargosService {

    @Autowired
    private CargosRepository repo;

    @Autowired
    private TipoDatoContableRepository tipoDatoRepo;

    public List<DTOCargos> getData() {
        List<CargosEntity> lista = repo.findAll();
        return lista.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    private DTOCargos convertirADTO(CargosEntity c) {
        DTOCargos dto = new DTOCargos();
        dto.setIdCargos(c.getIdCargos());
        dto.setMonto(c.getMonto());
        if (c.getTipoDatoContable() != null) {
            dto.setIdTipoDatoContable(c.getTipoDatoContable().getIdTipoDatoContable());
            dto.setNombreTipoDatoContable(c.getTipoDatoContable().getNombre());
        }
        return dto;
    }

    public String post(DTOCargos dto) {
        try {
            CargosEntity c = new CargosEntity();
            Optional<TipoDatoContableEntity> tipo = tipoDatoRepo.findById(dto.getIdTipoDatoContable());
            if (tipo.isEmpty()) return "Tipo de dato contable no encontrado.";

            c.setTipoDatoContable(tipo.get());
            c.setMonto(dto.getMonto());
            repo.save(c);
            return "Cargo creado correctamente.";
        } catch (Exception e) {
            return "Error al crear cargo: " + e.getMessage();
        }
    }

    public String update(Long id, DTOCargos dto) {
        Optional<CargosEntity> optional = repo.findById(id);
        if (optional.isEmpty()) {
            return "Cargo no encontrado.";
        }

        CargosEntity c = optional.get();
        c.setMonto(dto.getMonto());

        if (dto.getIdTipoDatoContable() != null) {
            Optional<TipoDatoContableEntity> tipo = tipoDatoRepo.findById(dto.getIdTipoDatoContable());
            if (tipo.isEmpty()) {
                return "Tipo de dato contable no encontrado.";
            }
            c.setTipoDatoContable(tipo.get());
        }

        repo.save(c);
        return "Cargo actualizado correctamente.";
    }


    public String patch(Long id, DTOCargos dto) {
        Optional<CargosEntity> optional = repo.findById(id);
        if (optional.isEmpty()) {
            return "Cargo no encontrado.";
        }

        CargosEntity c = optional.get();

        if (dto.getMonto() != null) {
            c.setMonto(dto.getMonto());
        }

        if (dto.getIdTipoDatoContable() != null) {
            Optional<TipoDatoContableEntity> tipo = tipoDatoRepo.findById(dto.getIdTipoDatoContable());
            if (tipo.isEmpty()) {
                return "Tipo de dato contable no encontrado.";
            }
            c.setTipoDatoContable(tipo.get());
        }

        repo.save(c);
        return "Cargo actualizado parcialmente.";
    }


    public String delete(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return "Cargo eliminado correctamente.";
        } else {
            return "Cargo no encontrado.";
        }
    }
}
