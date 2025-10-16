package org.ttamics.bcnesa_data_importer.jpa.mapper;

import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Team;
import org.ttamics.bcnesa_data_importer.jpa.model.TeamJPA;

import java.util.function.Function;

@Component
public class TeamToTeamJPAMapper implements Function<Team, TeamJPA> {
    @Override
    public TeamJPA apply(Team team) {
        //return new TeamJPA(team.getId().toString(), team.getName());
        return null;
    }
}
