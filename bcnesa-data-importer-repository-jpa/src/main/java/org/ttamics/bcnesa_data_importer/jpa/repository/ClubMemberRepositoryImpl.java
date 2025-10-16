package org.ttamics.bcnesa_data_importer.jpa.repository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.ClubMember;
import org.ttamics.bcnesa_data_importer.core.repository.ClubMemberRepository;
import org.ttamics.bcnesa_data_importer.jpa.mapper.ClubMemberJPAToClubMemberMapper;
import org.ttamics.bcnesa_data_importer.jpa.mapper.ClubMemberToClubMemberJPAMapper;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class ClubMemberRepositoryImpl implements ClubMemberRepository {

    private final ClubMemberRepositoryHelper clubMemberRepositoryHelper;

    private final ClubMemberJPAToClubMemberMapper clubMemberJPAToClubMemberMapper;

    private final ClubMemberToClubMemberJPAMapper clubMemberToClubMemberJPAMapper;

    @Autowired
    public ClubMemberRepositoryImpl(ClubMemberRepositoryHelper clubMemberRepositoryHelper, ClubMemberJPAToClubMemberMapper clubMemberJPAToClubMemberMapper, ClubMemberToClubMemberJPAMapper clubMemberToClubMemberJPAMapper) {
        this.clubMemberRepositoryHelper = clubMemberRepositoryHelper;
        this.clubMemberJPAToClubMemberMapper = clubMemberJPAToClubMemberMapper;
        this.clubMemberToClubMemberJPAMapper = clubMemberToClubMemberJPAMapper;
    }

    @Override
    public Optional<ClubMember> findByPracticionerIdAndClubId(UUID practicionerId, UUID clubId) {
        return clubMemberRepositoryHelper.findByPracticionerIdAndClubId(practicionerId, clubId).map(clubMemberJPAToClubMemberMapper);
    }

    @Override
    public void save(ClubMember clubMember) {
        clubMemberRepositoryHelper.save(clubMemberToClubMemberJPAMapper.apply(clubMember));
    }
}
