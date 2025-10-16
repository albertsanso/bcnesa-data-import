package org.ttamics.bcnesa_data_importer.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name="Practicioner", schema="bcnesadata")
public class PracticionerJPA {
    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="second_name")
    private String secondName;

    @NotNull
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birth_date")
    private Date birthDate;
}
