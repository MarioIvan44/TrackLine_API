package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "TB_TRACKING")
public class TrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tracking")
    @SequenceGenerator(name = "seq_tracking", sequenceName = "SEQ_ID_TRACK", allocationSize = 1)
    @Column(name = "IDTRACKING")
    private Long idTracking;

    //Referencia a tabla foránea "Tb_Viajes"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDVIAJE", referencedColumnName = "IDVIAJE", insertable = false, updatable = false)
    private ViajeEntity viaje;

    //Referencia a tabla foránea "Tb_Estado"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDESTADO", referencedColumnName = "IDESTADO", insertable = false, updatable = false)
    private EstadosEntity estado;

    @Column(name = "HORAESTIMADAPARTIDA")
    private Timestamp horaEstimadaPartida;

    @Column(name = "HORAESTIMADALLEGADA")
    private Timestamp horaEstimadaLlegada;

    @Column(name = "HORASALIDA")
    private Timestamp horaSalida;

    @Column(name = "HORALLEGADA")
    private Timestamp horaLlegada;

    @Column(name = "LUGARPARTIDA")
    private String lugarPartida;

    @Column(name = "LUGARLLEGADA")
    private String lugarLlegada;

    @Column(name = "PROGRESO")
    private Long progreso;
}