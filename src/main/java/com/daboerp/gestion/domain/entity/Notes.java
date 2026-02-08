package com.daboerp.gestion.domain.entity;

import java.util.Objects;
import java.util.UUID;

/**
 * Notes entity - structured notes with severity classification.
 * Composed by Guest.
 */
public class Notes {
    
    private final String id;
    private final String text;
    private final LevelNote level;
    
    private Notes(String id, String text, LevelNote level) {
        this.id = Objects.requireNonNull(id, "Notes ID cannot be null");
        this.text = Objects.requireNonNull(text, "Text cannot be null");
        this.level = Objects.requireNonNull(level, "Level cannot be null");
    }
    
    public static Notes create(String text, LevelNote level) {
        return new Notes(UUID.randomUUID().toString(), text, level);
    }
    
    public static Notes reconstitute(String id, String text, LevelNote level) {
        return new Notes(id, text, level);
    }
    
    public String getId() {
        return id;
    }
    
    public String getText() {
        return text;
    }
    
    public LevelNote getLevel() {
        return level;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notes notes = (Notes) o;
        return Objects.equals(id, notes.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
