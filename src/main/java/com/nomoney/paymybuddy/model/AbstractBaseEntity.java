package com.nomoney.paymybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * The AbstractBaseEntity class is a mapped superclass that serves as the base class for all entities in the system.
 * It defines the common properties for all entities, such as the ID field and basic methods for equality and hash code.
 */
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractBaseEntity {
    public static final int START_SEQ = 1000;

    @Id
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    protected Long id;

    /**
     * The equals method is overridden to compare entities based on their ID field.
     * Two entities are considered equal if they have the same ID, regardless of their other properties.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBaseEntity that = (AbstractBaseEntity) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    /**
     * The hashCode method is overridden to generate a hash code based on the ID field.
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}