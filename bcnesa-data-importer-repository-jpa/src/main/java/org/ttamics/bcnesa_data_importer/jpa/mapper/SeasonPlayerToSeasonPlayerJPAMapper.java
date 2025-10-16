package org.ttamics.bcnesa_data_importer.jpa.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.SeasonPlayer;
import org.ttamics.bcnesa_data_importer.jpa.model.SeasonPlayerJPA;

import java.util.function.Function;

@Component
public class SeasonPlayerToSeasonPlayerJPAMapper implements Function<SeasonPlayer, SeasonPlayerJPA> {
    private final ClubMemberToClubMemberJPAMapper clubMemberToClubMemberJPAMapper;

    @Autowired
    public SeasonPlayerToSeasonPlayerJPAMapper(ClubMemberToClubMemberJPAMapper clubMemberToClubMemberJPAMapper) {
        this.clubMemberToClubMemberJPAMapper = clubMemberToClubMemberJPAMapper;
    }

    @Override
    public SeasonPlayerJPA apply(SeasonPlayer seasonPlayer) {
        SeasonPlayerJPA seasonPlayerJPA = new SeasonPlayerJPA();
        seasonPlayerJPA.setId(seasonPlayer.getId());
        seasonPlayerJPA.setClubMember(clubMemberToClubMemberJPAMapper.apply(seasonPlayer.getClubMember()));
        seasonPlayerJPA.setLicenseType(seasonPlayer.getLicense().getLicenseType());
        seasonPlayerJPA.setLicenseRef(seasonPlayer.getLicense().getLicenseId());
        seasonPlayerJPA.setYearRange(seasonPlayer.getYearRange());
        return seasonPlayerJPA;
    }
}
