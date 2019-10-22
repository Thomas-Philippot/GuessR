package fr.eni.guessr.view_model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import fr.eni.guessr.model.Guess;
import fr.eni.guessr.model.Level;
import fr.eni.guessr.model.LevelWithGuesses;
import fr.eni.guessr.repository.GuessRepository;
import fr.eni.guessr.repository.LevelRepository;

public class GuessViewModel extends AndroidViewModel {

    private GuessRepository levelRepository;

    public GuessViewModel(Application application) {
        super(application);
        this.levelRepository = new GuessRepository(application);
    }

    public void insert(Guess guess) {
        this.levelRepository.insert(guess);
    }
}
