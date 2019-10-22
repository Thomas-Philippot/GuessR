package fr.eni.guessr.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import fr.eni.guessr.dao.GuessDao;
import fr.eni.guessr.dao.LevelDao;
import fr.eni.guessr.dao.LevelDatabase;
import fr.eni.guessr.model.Guess;
import fr.eni.guessr.model.Level;

public class GuessRepository {

    private GuessDao guessDao;

    public GuessRepository(Application application) {
        LevelDatabase levelDatabase = LevelDatabase.getInstance(application);
        guessDao = levelDatabase.guessDao();
    }
    public void insert(Guess guess) {
        new AsyncInsertGuess(guessDao).execute(guess);
    }

    private static class AsyncInsertGuess extends AsyncTask<Guess, Void, Void> {
        private GuessDao guessDao;

        AsyncInsertGuess(GuessDao guessDao) {
            this.guessDao = guessDao;
        }

        @Override
        protected Void doInBackground(Guess... guess) {
            Log.d("TOTO", "Level do inBackground : " + guess[0].toString());
            this.guessDao.insert(guess[0]);
            return null;
        }
    }
}
