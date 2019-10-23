package fr.eni.guessr.view_model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import fr.eni.guessr.model.Guess;
import fr.eni.guessr.repository.GuessRepository;

public class GuessViewModel extends AndroidViewModel {

    private GuessRepository guessRepository;

    public GuessViewModel(Application application) {
        super(application);
        this.guessRepository = new GuessRepository(application);
    }

    public void insert(Guess guess) {
        this.guessRepository.insert(guess);
    }

    public LiveData<List<Guess>> getCurrentGuess() {
        return this.guessRepository.getCurrentGuess();
    }

    public void update(Guess guess) {
        this.guessRepository.update(guess);
    }
}
