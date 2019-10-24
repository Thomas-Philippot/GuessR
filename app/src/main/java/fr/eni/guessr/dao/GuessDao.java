package fr.eni.guessr.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import fr.eni.guessr.model.Guess;

@Dao
public interface GuessDao {

    @Query("SELECT * FROM guess;")
    @Transaction
    LiveData<List<Guess>> findAll();

    @Query("SELECT * FROM guess WHERE status = 'TODO' OR status = 'PASSED' ORDER by levelId ASC, status DESC;")
    LiveData<List<Guess>> getCurrentGuess();

    @Query("SELECT * FROM guess WHERE levelId = :id")
    LiveData<List<Guess>> getGuessByLevelId(int id);

    @Insert
    void insert(Guess guess);

    @Update
    void update(Guess guess);

    @Query("DELETE FROM guess;")
    void deleteAll();
}
