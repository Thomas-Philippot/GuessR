package fr.eni.guessr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import fr.eni.guessr.model.Guess;
import fr.eni.guessr.view_model.GuessViewModel;

public class PlayActivity extends AppCompatActivity {

    private Guess currentGuess;
    private GuessViewModel guessViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        this.guessViewModel = ViewModelProviders.of(this).get(GuessViewModel.class);
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
                    setViewValues();
                }
            }
        });
    }

    private void setViewValues() {
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(this.currentGuess.getImage());
        // ImageView in your Activity
        ImageView imageView = this.findViewById(R.id.play_image);
        TextView textView = this.findViewById(R.id.play_hint);

        textView.setText(this.currentGuess.getHint());

        // Download directly from StorageReference using Glide
        GlideApp.with(this)
                .load(storageReference)
                .into(imageView);
    }

    public void passClicked(View view) {
        this.currentGuess.setStatus("PASSED");
        this.guessViewModel.update(currentGuess);
    }

    public void CheckClicked(View view) {
        EditText editText = this.findViewById(R.id.play_text);
        if (editText.getText().toString().equals(this.currentGuess.getAnswer())) {
            Log.d("TOTO", "same");
            this.currentGuess.setStatus("DONE");
            this.guessViewModel.update(currentGuess);
        }
    }
}
