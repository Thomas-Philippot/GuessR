package fr.eni.guessr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fr.eni.guessr.adapter.AdapterGuess;
import fr.eni.guessr.model.LevelWithGuesses;
import fr.eni.guessr.view_model.LevelViewModel;

public class ResultsActivity extends AppCompatActivity {

    private LevelViewModel levelViewModel;

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
                LinearLayoutCompat layout = findViewById(R.id.result_activity);
                layout.removeAllViews();
                for (LevelWithGuesses level : levelWithGuesses) {
                    Log.d("TOTO", level.toString());
                    setListToView(level);
                }

            }
        });
    }

    public void setListToView(LevelWithGuesses level) {
        LinearLayoutCompat layout = this.findViewById(R.id.result_activity);
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
        this.setListViewHeightBasedOnChildren(listView);

        //set the card view style
        cardView.setRadius(45);
        ViewGroup.MarginLayoutParams cardViewParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
        cardViewParams.setMargins(0,0,0,50);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
