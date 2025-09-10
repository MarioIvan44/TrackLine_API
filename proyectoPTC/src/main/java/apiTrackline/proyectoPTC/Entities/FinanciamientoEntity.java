package apiTrackline.proyectoPTC.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TB_FINANCIAMIENTOS")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FinanciamientoEntity {

    @Id
    //generator: Generador en código Java
    //name: Nombre del generador en el código Java
    //sequenceName: Nombre de la secuencia en oracle
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "financiamientoSeq")
    @SequenceGenerator(name = "financiamientoSeq", sequenceName = "SEQ_ID_FIN", allocationSize = 1)
    @Column(name = "IDFINANCIAMIENTO")
    private Long idFinanciamiento;

    @ManyToOne
    @JoinColumn(name = "IDTIPOFINANCIAMIENTO", referencedColumnName = "IDTIPOFINANCIAMIENTO")
    private TipoFinanciamientosEntity tipoFinanciamiento;

    @Column(name = "MONTO")
    private Long monto;
}