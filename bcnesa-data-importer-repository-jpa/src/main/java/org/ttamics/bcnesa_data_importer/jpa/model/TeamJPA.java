package org.ttamics.bcnesa_data_importer.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name="Team", schema="bcnesa-data")
public class TeamJPA {

    @Id
    private String id;

    private String name;
}
