package org.ttamics.bcnesa_data_importer.core.model;

import org.albertsanso.commons.model.Entity;

import java.util.UUID;

public class Team extends Entity {
    private final UUID id;
    private final String name;

    private Team(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Team createNew(String name) {
        return new Team(UUID.randomUUID(), name);
    }

    public static Team createExisting(UUID id, String name) {
        return new Team(id, name);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
