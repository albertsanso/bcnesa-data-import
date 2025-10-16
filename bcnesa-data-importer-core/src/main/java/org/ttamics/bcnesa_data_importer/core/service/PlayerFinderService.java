package org.ttamics.bcnesa_data_importer.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ttamics.bcnesa_data_importer.core.repository.SeasonPlayerRepository;

@Service
public class PlayerFinderService {
    private final SeasonPlayerRepository seasonPlayerRepository;

    @Autowired
    public PlayerFinderService(SeasonPlayerRepository seasonPlayerRepository) {
        this.seasonPlayerRepository = seasonPlayerRepository;
    }
}
