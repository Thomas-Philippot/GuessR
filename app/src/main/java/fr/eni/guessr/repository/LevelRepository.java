package fr.eni.guessr.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;

import fr.eni.guessr.dao.LevelDao;
import fr.eni.guessr.dao.LevelDatabase;
import fr.eni.guessr.model.Level;
import fr.eni.guessr.model.LevelWithGuesses;

public class LevelRepository {

    private LevelDao levelDao;
    private LiveData<List<LevelWithGuesses>> levels;

    public LevelRepository(Application application) {
        LevelDatabase levelDatabase = LevelDatabase.getInstance(application);
        levelDao = levelDatabase.levelDao();
        levels = levelDao.findAll();
    }

    public void insert(Level level) {
        new AsyncInsertLevel(levelDao).execute(level);
    }

    public LiveData<List<LevelWithGuesses>> findAll() {
        return levels;
    }

    public void update(Level level) {
        new AsyncUpdateLevel(levelDao).execute(level);
    }

    private static class AsyncInsertLevel extends AsyncTask<Level, Void, Void> {
        private LevelDao levelDao;

        AsyncInsertLevel(LevelDao levelDao) {
            this.levelDao = levelDao;
        }

        @Override
        protected Void doInBackground(Level... level) {
            this.levelDao.insert(level[0]);
            return null;
        }
    }

    private static class AsyncUpdateLevel extends AsyncTask<Level, Void, Void> {
        private LevelDao levelDao;

        AsyncUpdateLevel(LevelDao levelDao) {
            this.levelDao = levelDao;
        }

        @Override
        protected Void doInBackground(Level... level) {
            this.levelDao.update(level[0]);
            return null;
        }
    }
}
