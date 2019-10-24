package fr.eni.guessr.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import fr.eni.guessr.dao.GuessDao;
import fr.eni.guessr.dao.LevelDatabase;
import fr.eni.guessr.model.Guess;

public class GuessRepository {

    private GuessDao guessDao;
    private LiveData<List<Guess>> currentGuess;
    private LiveData<List<Guess>> guessesByLevelId;

    public GuessRepository(Application application) {
        LevelDatabase levelDatabase = LevelDatabase.getInstance(application);
        guessDao = levelDatabase.guessDao();
        this.currentGuess = guessDao.getCurrentGuess();
    }
    public void insert(Guess guess) {
        new AsyncInsertGuess(guessDao).execute(guess);
    }

    public void update(Guess guess) {
        new AsyncUpdateGuess(guessDao).execute(guess);
    }

    public LiveData<List<Guess>> getCurrentGuess() {
        return this.currentGuess;
    }

    private static class AsyncInsertGuess extends AsyncTask<Guess, Void, Void> {
        private GuessDao guessDao;

        AsyncInsertGuess(GuessDao guessDao) {
            this.guessDao = guessDao;
        }

        @Override
        protected Void doInBackground(Guess... guess) {
            this.guessDao.insert(guess[0]);
            return null;
        }
    }

    private static class AsyncUpdateGuess extends AsyncTask<Guess, Void, Void> {
        private GuessDao guessDao;

        AsyncUpdateGuess(GuessDao guessDao) {
             this.guessDao = guessDao;
        }

        @Override
        protected Void doInBackground(Guess... guesses) {
            this.guessDao.update(guesses[0]);
            return null;
        }
    }

}
