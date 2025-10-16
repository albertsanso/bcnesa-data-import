package org.ttamics.bcnesa_data_importer.jpa.mapper;

import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Team;
import org.ttamics.bcnesa_data_importer.jpa.model.TeamJPA;

import java.util.UUID;
import java.util.function.Function;

@Component
public class TeamJPAToTeamMapper implements Function<TeamJPA, Team> {
    @Override
    public Team apply(TeamJPA teamJPA) {
        return Team.createExisting(UUID.fromString(teamJPA.getId()), teamJPA.getName());
    }
}
