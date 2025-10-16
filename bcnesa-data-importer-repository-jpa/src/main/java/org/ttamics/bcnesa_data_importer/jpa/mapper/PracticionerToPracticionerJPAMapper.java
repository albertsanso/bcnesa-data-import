package org.ttamics.bcnesa_data_importer.jpa.mapper;

import org.springframework.stereotype.Component;
import org.ttamics.bcnesa_data_importer.core.model.Practicioner;
import org.ttamics.bcnesa_data_importer.jpa.model.PracticionerJPA;

import java.util.function.Function;

@Component
public class PracticionerToPracticionerJPAMapper implements Function<Practicioner, PracticionerJPA> {
    @Override
    public PracticionerJPA apply(Practicioner practicioner) {
        PracticionerJPA practicionerJPA = new PracticionerJPA();
        practicionerJPA.setId(practicioner.getId());
        practicionerJPA.setFirstName(practicioner.getFirstName());
        practicionerJPA.setSecondName(practicioner.getSecondName());
        practicionerJPA.setFullName(practicioner.getFullName());
        practicionerJPA.setBirthDate(practicioner.getBirthDate());
        return practicionerJPA;
    }
}
