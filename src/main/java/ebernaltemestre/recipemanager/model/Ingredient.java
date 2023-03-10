package ebernaltemestre.recipemanager.model;

import javax.persistence.*;

import ebernaltemestre.recipemanager.utils.MeasureType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer amount;

    @Column(name = "measure_type")
    @Enumerated(EnumType.STRING)
    private MeasureType measureType;

}
