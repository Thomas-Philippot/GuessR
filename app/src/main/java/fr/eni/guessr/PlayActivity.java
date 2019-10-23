package fr.eni.guessr;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import fr.eni.guessr.bounce.MyBounceInterpolator;
import fr.eni.guessr.model.Guess;
import fr.eni.guessr.view_model.GuessViewModel;
import pl.droidsonroids.gif.GifImageView;

public class PlayActivity extends AppCompatActivity {

    private Guess currentGuess;
    private GuessViewModel guessViewModel;

    private CardView cardView;
    private GifImageView loadingView;
    private ImageView guessImageView;
    private TextView guessRandomTextView;
    private EditText guessAnswerEditText;
    private FloatingActionButton submitButton;
    private FloatingActionButton skipButton;
    private Button btnSuccess;
    private Button btnFail;
    private Animation myAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        this.guessViewModel = ViewModelProviders.of(this).get(GuessViewModel.class);

        this.cardView = this.findViewById(R.id.play_card_view);
        this.loadingView = this.findViewById(R.id.play_gif_loading);
        this.guessImageView = this.findViewById(R.id.play_image);
        this.guessRandomTextView = this.findViewById(R.id.play_hint);
        this.guessAnswerEditText = this.findViewById(R.id.play_text);
        this.submitButton = this.findViewById(R.id.play_submit_button);
        this.skipButton = this.findViewById(R.id.play_pass_button);
        this.btnSuccess = this.findViewById(R.id.play_btn_success_animated);
        this.btnFail= this.findViewById(R.id.play_btn_fail_animated);
        this.myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        InputFilter[] editFilters = this.guessAnswerEditText.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        this.guessAnswerEditText.setFilters(newFilters);

        this.btnSuccess.setVisibility(View.INVISIBLE);
        this.btnFail.setVisibility(View.INVISIBLE);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        this.loading(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TOTO", "Start getCurrentGuess");
        this.guessViewModel.getCurrentGuess().observe(this, new Observer<List<Guess>>() {
            @Override
            public void onChanged(List<Guess> guesses) {
                if (guesses.size() < 1) {
                    finishAffinity();
                    Intent intent = new Intent(PlayActivity.this, GameOverActivity.class);
                    startActivity(intent);
                } else {
                    currentGuess = guesses.get(0);
                    PlayActivity.this.loading(true);
                    setViewValues();
                    guessAnswerEditText.setText("");
                    PlayActivity.this.loading(false);
                }
            }
        });
    }

    private void setViewValues() {
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(this.currentGuess.getImage());

        this.guessRandomTextView.setText(this.currentGuess.getHint());

        // Download directly from StorageReference using Glide
        GlideApp.with(this)
                .load(storageReference)
                .into(this.guessImageView);
    }

    public void passClicked(View view) {
        this.currentGuess.setStatus("PASSED");
        this.guessViewModel.update(currentGuess);
    }

    public void CheckClicked(View view) {
        EditText editText = this.findViewById(R.id.play_text);
        if (editText.getText().toString().equals(this.currentGuess.getAnswer())) {
            this.successOrFail(true);

            Log.d("TOTO", "same");

            this.currentGuess.setStatus("DONE");
            this.guessViewModel.update(currentGuess);
        } else {
            this.successOrFail(false);
        }
    }

    private void loading(boolean state) {
        if(state){
            this.loadingView.setVisibility(View.VISIBLE);

            this.cardView.setVisibility(View.INVISIBLE);
            this.guessImageView.setVisibility(View.INVISIBLE);
            this.guessRandomTextView.setVisibility(View.INVISIBLE);
            this.guessAnswerEditText.setVisibility(View.INVISIBLE);
            this.submitButton.setVisibility(View.INVISIBLE);
            this.skipButton.setVisibility(View.INVISIBLE);
        } else {
            this.loadingView.setVisibility(View.INVISIBLE);

            this.cardView.setVisibility(View.VISIBLE);
            this.guessImageView.setVisibility(View.VISIBLE);
            this.guessRandomTextView.setVisibility(View.VISIBLE);
            this.guessAnswerEditText.setVisibility(View.VISIBLE);
            this.submitButton.setVisibility(View.VISIBLE);
            this.skipButton.setVisibility(View.VISIBLE);
        }
    }

    private void successOrFail(boolean success){
        if(success){
            this.cardView.setVisibility(View.INVISIBLE);
            this.guessImageView.setVisibility(View.INVISIBLE);
            this.guessRandomTextView.setVisibility(View.INVISIBLE);
            this.guessAnswerEditText.setVisibility(View.INVISIBLE);
            this.submitButton.setVisibility(View.INVISIBLE);
            this.skipButton.setVisibility(View.INVISIBLE);

            this.btnSuccess.setVisibility(View.VISIBLE);
            this.btnSuccess.startAnimation(myAnim);
            this.btnSuccess.setVisibility(View.INVISIBLE);

            this.cardView.setVisibility(View.VISIBLE);
            this.guessImageView.setVisibility(View.VISIBLE);
            this.guessRandomTextView.setVisibility(View.VISIBLE);
            this.guessAnswerEditText.setVisibility(View.VISIBLE);
            this.submitButton.setVisibility(View.VISIBLE);
            this.skipButton.setVisibility(View.VISIBLE);
        } else {
            this.guessImageView.setVisibility(View.INVISIBLE);
            this.guessRandomTextView.setVisibility(View.INVISIBLE);
            this.guessAnswerEditText.setVisibility(View.INVISIBLE);
            this.submitButton.setVisibility(View.INVISIBLE);
            this.skipButton.setVisibility(View.INVISIBLE);

            this.btnFail.setVisibility(View.VISIBLE);
            this.btnFail.startAnimation(myAnim);
            this.btnFail.setVisibility(View.INVISIBLE);

            this.guessImageView.setVisibility(View.VISIBLE);
            this.guessRandomTextView.setVisibility(View.VISIBLE);
            this.guessAnswerEditText.setVisibility(View.VISIBLE);
            this.submitButton.setVisibility(View.VISIBLE);
            this.skipButton.setVisibility(View.VISIBLE);
        }
    }
}
