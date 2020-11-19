package org.springframework.samples.petclinic.position;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

@Entity
@Table(name = "types")
public class PetType extends NamedEntity {

}
