package org.ttamics.bcnesa_data_importer.jpa.repository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Competition;
import org.ttamics.bcnesa_data_importer.core.model.CompetitionType;
import org.ttamics.bcnesa_data_importer.core.model.LicenseType;
import org.ttamics.bcnesa_data_importer.core.model.SeasonPlayerResult;
import org.ttamics.bcnesa_data_importer.core.repository.SeasonPlayerResultRepository;
import org.ttamics.bcnesa_data_importer.jpa.mapper.SeasonPlayerResultJPAToSeasonPlayerResultMapper;
import org.ttamics.bcnesa_data_importer.jpa.mapper.SeasonPlayerResultToSeasonPlayerResultJPAMapper;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class SeasonPlayerResultRepositoryImpl implements SeasonPlayerResultRepository {

    private final SeasonPlayerResultRepositoryHelper helper;

    private final SeasonPlayerResultToSeasonPlayerResultJPAMapper toJPAMapper;

    private final SeasonPlayerResultJPAToSeasonPlayerResultMapper fromJPAMapper;

    @Autowired
    public SeasonPlayerResultRepositoryImpl(SeasonPlayerResultRepositoryHelper helper, SeasonPlayerResultToSeasonPlayerResultJPAMapper toJPAMapper, SeasonPlayerResultJPAToSeasonPlayerResultMapper fromJPAMapper) {
        this.helper = helper;
        this.toJPAMapper = toJPAMapper;
        this.fromJPAMapper = fromJPAMapper;
    }

    @Override
    public Optional<SeasonPlayerResult> findById(UUID id) {
        return helper.findById(id).map(fromJPAMapper);
    }

    @Override
    public Optional<SeasonPlayerResult> findFor(
            String season, CompetitionType competitionType, Competition competition, String jornada, String group, String teamName, LicenseType licenseType, String licenseId, String gameLetters) {
        return helper.findBySeasonAndCompetitionTypeAndCompetitionAndJornadaAndGroupAndSeasonPlayer_ClubMember_Club_NameAndSeasonPlayer_LicenseTypeAndSeasonPlayer_LicenseRefAndMatchLinkageId(
                season, competitionType, competition,  jornada, group, teamName, licenseType, licenseId, gameLetters)
                .map(fromJPAMapper);
    }

    @Override
    public void save(SeasonPlayerResult seasonPlayerResult) {
        helper.save(toJPAMapper.apply(seasonPlayerResult));
    }
}
