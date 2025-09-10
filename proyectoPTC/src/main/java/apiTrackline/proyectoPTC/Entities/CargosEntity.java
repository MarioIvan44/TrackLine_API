package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TB_CARGOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CargosEntity {


    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cargosSeq")
    @SequenceGenerator(name = "cargosSeq", sequenceName = "SEQ_ID_CARG", allocationSize = 1)
    @Column(name = "IDCARGOS")
    private Long idCargos;

    @ManyToOne
    @JoinColumn(name = "IDTIPODATOCONTABLE", referencedColumnName = "IDTIPODATOCONTABLE")
    private TipoDatoContableEntity tipoDatoContable;

    @Column(name = "MONTO")
    private Long monto;
}
