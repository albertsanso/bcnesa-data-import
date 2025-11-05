package org.ttamics.bcnesa_data_importer.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.ttamics.bcnesa_data_importer.core.model.Competition;
import org.ttamics.bcnesa_data_importer.core.model.CompetitionType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(
        name="PlayersSingleMatch",
        schema="bcnesa-data",
        indexes = {
                @Index(name="idx_player_result_abc_id", columnList="player_result_abc_id"),
                @Index(name="idx_player_result_xyz_id", columnList="player_result_xyz_id"),
                @Index(name="idx_unique_row_match_id", columnList="unique_row_match_id")
        }
)
public class PlayersSingleMatchJPA {

    @Id
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "player_result_abc_id")
    private SeasonPlayerResultJPA seasonPlayerResultAbc;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "player_result_xyz_id")
    private SeasonPlayerResultJPA seasonPlayerResultXyz;

    @Column(name = "season", length = 9)
    private String season;

    @Enumerated(EnumType.STRING)
    @Column(name = "competition_type")
    private CompetitionType competitionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "competition")
    private Competition competition;

    @Column
    private String jornada;

    @Column(name = "competition_group")
    private String group;

    @Column(name="unique_row_match_id", unique = false)
    private String uniqueRowMatchId;

}
