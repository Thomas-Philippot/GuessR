package fr.eni.guessr.view_model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import fr.eni.guessr.model.Level;
import fr.eni.guessr.model.LevelWithGuesses;
import fr.eni.guessr.repository.LevelRepository;

public class LevelViewModel extends AndroidViewModel {

    private LevelRepository levelRepository;
    private LiveData<List<LevelWithGuesses>> levels;

    public LevelViewModel(Application application) {
        super(application);
        this.levelRepository = new LevelRepository(application);
        init();
    }

    private void init() {
        this.levels = levelRepository.findAll();
    }

    public LiveData<List<LevelWithGuesses>> findAll() {
        return levels;
    }

    public void insert(Level level) {
        this.levelRepository.insert(level);
    }

    public void update(Level level) {
        this.levelRepository.update(level);
    }
}
