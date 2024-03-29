package fr.eni.guessr.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fr.eni.guessr.model.Level;
import fr.eni.guessr.model.LevelWithGuesses;

@Dao
public interface LevelDao {

    @Query("SELECT * FROM level")
    @Transaction
    LiveData<List<LevelWithGuesses>> findAll();

    @Insert
    void insert(Level level);

    @Update
    void update(Level level);

    @Query("DELETE FROM level")
    void deleteAll();
}
