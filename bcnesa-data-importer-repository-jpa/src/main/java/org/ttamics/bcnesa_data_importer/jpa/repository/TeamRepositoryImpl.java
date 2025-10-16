package org.ttamics.bcnesa_data_importer.jpa.repository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Team;
import org.ttamics.bcnesa_data_importer.core.repository.TeamRepository;
import org.ttamics.bcnesa_data_importer.jpa.mapper.TeamJPAToTeamMapper;
import org.ttamics.bcnesa_data_importer.jpa.mapper.TeamToTeamJPAMapper;

import java.util.Optional;

@Transactional
@Component
public class TeamRepositoryImpl implements TeamRepository {

    private final TeamRepositoryHelper teamRepositoryHelper;
    private final TeamJPAToTeamMapper teamJPAToTeamMapper;
    private final TeamToTeamJPAMapper teamToTeamJPAMapper;

    @Autowired
    public TeamRepositoryImpl(TeamRepositoryHelper teamRepositoryHelper, TeamJPAToTeamMapper teamJPAToTeamMapper, TeamToTeamJPAMapper teamToTeamJPAMapper) {
        this.teamRepositoryHelper = teamRepositoryHelper;
        this.teamJPAToTeamMapper = teamJPAToTeamMapper;
        this.teamToTeamJPAMapper = teamToTeamJPAMapper;
    }

    @Override
    public Team findById(String id) {
        return teamRepositoryHelper.findById(id)
                .map(teamJPAToTeamMapper)
                .orElseThrow(() -> new IllegalStateException("Team not found!"));
    }

    @Override
    public Optional<Team> findByName(String name) {
        return teamRepositoryHelper.findByName(name)
                .map(teamJPAToTeamMapper);
    }

    @Override
    public boolean existsById(String id) {
        return teamRepositoryHelper.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return teamRepositoryHelper.existsByName(name);
    }

    @Override
    public void save(Team team) {
        teamRepositoryHelper.save(teamToTeamJPAMapper.apply(team));
    }
}
