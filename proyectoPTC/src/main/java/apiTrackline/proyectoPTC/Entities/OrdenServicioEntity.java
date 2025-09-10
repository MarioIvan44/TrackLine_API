package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "TB_ORDENSERVICIOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class OrdenServicioEntity {

    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordenServiciosSeq")
    @SequenceGenerator(name = "ordenServiciosSeq", sequenceName = "SEQ_ID_OS", allocationSize = 1)
    @Column(name = "IDORDENSERVICIO")
    private Long idOrdenServicio;

    // Campo clienteNIT (clave foránea en la tabla)
    @Column(name = "CLIENTENIT", nullable = false)
    private String clienteNIT;

    // Relación con ClientesEntity (sin afectar el insert/update de clienteNIT)
    @ManyToOne
    @JoinColumn(name = "CLIENTENIT", referencedColumnName = "CLIENTENIT", insertable = false, updatable = false, nullable = true)
    private ClientesEntity cliente;

    @ManyToOne
    @JoinColumn(name = "IDORDENENCABEZADO" , nullable = true)
    private OrdenEncabezadoEntity idOrdenEncabezado;

    @ManyToOne
    @JoinColumn(name = "IDINFOEMBARQUE", nullable = true)
    private InfoEmbarqueEntity idInfoEmbarque;

    @ManyToOne
    @JoinColumn(name = "IDADUANA", nullable = true)
    private AduanaEntity aduana;

    @ManyToOne
    @JoinColumn(name = "IDTRANSPORTE", nullable = true)
    private TransporteEntity idTransporte;

    @ManyToOne
    @JoinColumn(name = "IDRECOLECCION", nullable = true)
    private RecoleccionEntity idRecoleccion;

    @ManyToOne
    @JoinColumn(name = "IDCARGOS", nullable = true)
    private CargosEntity idCargos;

    @ManyToOne
    @JoinColumn(name = "IDFINANCIAMIENTO", nullable = true)
    private FinanciamientoEntity financiamiento;

    @ManyToOne
    @JoinColumn(name = "IDOBSERVACIONES", nullable = true)
    private ObservacionesEntity idObservaciones;

    @OneToMany(mappedBy = "OrdenServicio", cascade = CascadeType.ALL)
    private List<ViajeEntity> viajes;

    @OneToMany(mappedBy = "OrdenServicioEstados", cascade = CascadeType.ALL)
    private List<EstadosEntity> estados;

}