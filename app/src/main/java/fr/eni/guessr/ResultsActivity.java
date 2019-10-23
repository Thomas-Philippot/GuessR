package fr.eni.guessr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

        //create all the element to display the level
        TextView textView = new TextView(this);
        ListView listView = new ListView(this);
        CardView cardView = new CardView(this);
        layout.addView(textView);
        cardView.addView(listView);
        layout.addView(cardView);

        //set the level name text style and content
        String levelName = level.getLevel().getName();
        textView.setText(levelName.substring(0, 1).toUpperCase() + levelName.substring(1, 5) + " " + levelName.substring(5));
        textView.setId(View.generateViewId());
        textView.setTextSize(40);
        ViewGroup.MarginLayoutParams textViewParams = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        textViewParams.setMargins(0,40,0,40);

        //set the list style and content
        AdapterGuess adapterGuess = new AdapterGuess(this, R.layout.row_style_guess, level.getGuesses());
        listView.setAdapter(adapterGuess);
        listView.setId(View.generateViewId());
        listView.setDivider(this.getResources().getDrawable(R.drawable.transparent, null));

        //set the card view style
        cardView.setRadius(45);
        cardView.setMinimumHeight(50);
        ViewGroup.MarginLayoutParams cardViewParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
        cardViewParams.setMargins(0,0,0,50);
    }

}
