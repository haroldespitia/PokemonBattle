package com.techicalTest.formula.monks.PokemonBattle.db.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TYPE_CHART")
public class TypeChart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "pokemon_type_id", referencedColumnName = "id")
    private Type type;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "pokemon_type_id_strong_to", referencedColumnName = "id")
    private Type typeStrongTo;
}
