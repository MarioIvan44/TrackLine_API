package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @EqualsAndHashCode @ToString
@Entity
@Table(name = "TB_VIAJES")
public class ViajeEntity {

    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "viajeSeq")
    @SequenceGenerator(name = "viajeSeq", sequenceName = "SEQ_ID_VIAJE", allocationSize = 1)
    @Column(name = "IDVIAJE")
    private Long idViaje;

    //Relacion con OrdenServicio
    @ManyToOne
    @JoinColumn(name = "IDORDENSERVICIO", referencedColumnName = "IDORDENSERVICIO")
    private OrdenServicioEntity OrdenServicio;

    //Relacion con Transporte
    @ManyToOne
    @JoinColumn(name = "IDTRANSPORTE", referencedColumnName = "IDTRANSPORTE")
    private TransporteEntity transporte;
}
