package fr.eni.guessr.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.eni.guessr.model.Level;
import fr.eni.guessr.model.LevelWithGuesses;

@Dao
public interface LevelDao {

    @Query("SELECT * FROM level")
    LiveData<List<LevelWithGuesses>> findAll();

    @Insert
    void insert(Level level);
}
