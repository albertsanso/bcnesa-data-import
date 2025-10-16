package org.ttamics.bcnesa_data_importer.jpa.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.ClubMember;
import org.ttamics.bcnesa_data_importer.jpa.model.ClubMemberJPA;
import org.ttamics.bcnesa_data_importer.jpa.model.PracticionerJPA;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Component
public class ClubMemberToClubMemberJPAMapper implements Function<ClubMember, ClubMemberJPA> {

    private final ClubToClubJPAMapper clubToClubJPAMapper;

    private final PracticionerToPracticionerJPAMapper practicionerToPracticionerJPAMapper;

    @Autowired
    public ClubMemberToClubMemberJPAMapper(ClubToClubJPAMapper clubToClubJPAMapper, PracticionerToPracticionerJPAMapper practicionerToPracticionerJPAMapper) {
        this.clubToClubJPAMapper = clubToClubJPAMapper;
        this.practicionerToPracticionerJPAMapper = practicionerToPracticionerJPAMapper;
    }

    @Override
    public ClubMemberJPA apply(ClubMember clubMember) {

        ClubMemberJPA clubMemberJPA = new ClubMemberJPA();
        clubMemberJPA.setId(clubMember.getId());
        clubMemberJPA.setClub(clubToClubJPAMapper.apply(clubMember.getClub()));
        clubMemberJPA.setPracticioner(practicionerToPracticionerJPAMapper.apply(clubMember.getPracticioner()));

        List<String> sortedRanges = clubMember.getYearRanges().stream()
                .sorted(Comparator.comparing(r -> r.split("-")[0])) // sort by first year
                .toList();
        clubMemberJPA.setYearRanges(sortedRanges);
        return clubMemberJPA;
    }
}
