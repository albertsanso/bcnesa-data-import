package org.ttamics.bcnesa_data_importer.jpa.mapper;

import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Practicioner;
import org.ttamics.bcnesa_data_importer.jpa.model.PracticionerJPA;

import java.util.function.Function;

@Component
public class PracticionerJPAToPracticionerMapper implements Function<PracticionerJPA, Practicioner> {
    @Override
    public Practicioner apply(PracticionerJPA practicionerJPA) {
        return Practicioner.createExisting(
                practicionerJPA.getId(),
                practicionerJPA.getFirstName(),
                practicionerJPA.getSecondName(),
                practicionerJPA.getFullName(),
                practicionerJPA.getBirthDate()
                );
    }
}
