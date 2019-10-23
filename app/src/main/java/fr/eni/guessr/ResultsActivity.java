package fr.eni.guessr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fr.eni.guessr.adapter.AdapterGuess;
import fr.eni.guessr.model.Guess;
import fr.eni.guessr.model.Level;
import fr.eni.guessr.model.LevelWithGuesses;
import fr.eni.guessr.view_model.LevelViewModel;

public class ResultsActivity extends AppCompatActivity {

    private LevelViewModel levelViewModel;
    private List<LevelWithGuesses> levels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        this.levelViewModel = ViewModelProviders.of(this).get(LevelViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.levelViewModel.findAll().observe(this, new Observer<List<LevelWithGuesses>>() {
            @Override
            public void onChanged(List<LevelWithGuesses> levelWithGuesses) {
                levels = levelWithGuesses;
                for (LevelWithGuesses level : levelWithGuesses) {
                    Log.d("TOTO", level.toString());
                    setListToView(level);
                }

            }
        });
    }

    public void setListToView(LevelWithGuesses level) {
        ConstraintSet constraintSet = new ConstraintSet();
        ConstraintLayout layout = this.findViewById(R.id.result_activity);
        TextView textView = new TextView(this);
        ListView listView = new ListView(this);
        layout.addView(listView);
        layout.addView(textView);

        AdapterGuess adapterGuess = new AdapterGuess(this, R.layout.row_style_guess, level.getGuesses());
        listView.setAdapter(adapterGuess);

        textView.setText(level.getLevel().getName());
        textView.setGravity(12);
    }

}
