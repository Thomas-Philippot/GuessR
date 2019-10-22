package fr.eni.guessr.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

public class LevelWithGuesses {

    @Embedded
    public Level level;

    @Relation(parentColumn = "id", entityColumn = "levelId", entity = Guess.class)
    public List<Guess> guesses;

    public LevelWithGuesses() {
        this.guesses = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "LevelWithGuesses{" +
                "level=" + level +
                ", guesses=" + guesses +
                '}';
    }
}
