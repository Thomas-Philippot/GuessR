package fr.eni.guessr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        LinearLayout layout = this.findViewById(R.id.result_activity);
        layout.setId(R.id.result_activity);

        TextView textView = new TextView(this);
        ListView listView = new ListView(this);
        layout.addView(textView);
        layout.addView(listView);

        AdapterGuess adapterGuess = new AdapterGuess(this, R.layout.row_style_guess, level.getGuesses());
        listView.setAdapter(adapterGuess);
        listView.setId(View.generateViewId());

        textView.setText(level.getLevel().getName());
        textView.setId(View.generateViewId());
        textView.setTextSize(40);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        params.setMargins(10,10,10,10);

    }

}
