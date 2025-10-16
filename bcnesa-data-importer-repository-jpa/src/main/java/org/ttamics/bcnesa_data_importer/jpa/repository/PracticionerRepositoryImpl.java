package org.ttamics.bcnesa_data_importer.jpa.repository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Practicioner;
import org.ttamics.bcnesa_data_importer.core.repository.PracticionerRepository;
import org.ttamics.bcnesa_data_importer.jpa.mapper.PracticionerJPAToPracticionerMapper;
import org.ttamics.bcnesa_data_importer.jpa.mapper.PracticionerToPracticionerJPAMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Component
public class PracticionerRepositoryImpl implements PracticionerRepository {
    private final PracticionerRepositoryHelper practicionerRepositoryHelper;
    private final PracticionerToPracticionerJPAMapper toJPAMapper;
    private final PracticionerJPAToPracticionerMapper fromJPAMapper;

    @Autowired
    public PracticionerRepositoryImpl(PracticionerRepositoryHelper practicionerRepositoryHelper, PracticionerToPracticionerJPAMapper toJPAMapper, PracticionerJPAToPracticionerMapper fromJPAMapper) {
        this.practicionerRepositoryHelper = practicionerRepositoryHelper;
        this.toJPAMapper = toJPAMapper;
        this.fromJPAMapper = fromJPAMapper;
    }

    @Override
    public Optional<Practicioner> findById(UUID id) {
        return practicionerRepositoryHelper.findById(id.toString()).map(fromJPAMapper);
    }

    @Override
    public Optional<Practicioner> findByFullName(String fullName) {
        return practicionerRepositoryHelper.findByFullName(fullName).map(fromJPAMapper);
    }

    @Override
    public List<Practicioner> findAll() {
        List<Practicioner> practicionerList = new ArrayList<>();
        practicionerRepositoryHelper.findAll().forEach(
                practicionerJPA ->
                        practicionerList.add(fromJPAMapper.apply(practicionerJPA)));
        return practicionerList;
    }

    @Override
    public void save(Practicioner practicioner) {
        practicionerRepositoryHelper.save(toJPAMapper.apply(practicioner));
    }
}
