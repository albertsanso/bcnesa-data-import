package org.ttamics.bcnesa_data_importer.jpa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name="ClubMember", schema="bcnesa-data")
public class ClubMemberJPA {

    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "club_id")
    private ClubJPA club;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "practicioner_id")
    private PracticionerJPA practicioner;

    @ElementCollection
    @CollectionTable(
            name = "ClubMemberYearRange",
            schema = "bcnesadata",
            joinColumns = @JoinColumn(name = "club_member_id")
    )
    @Column(name = "year_range")
    @OrderColumn(name = "order_index")
    private List<String> yearRanges = new ArrayList<>();
}
