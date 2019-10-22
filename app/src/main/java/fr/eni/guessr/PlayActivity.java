package fr.eni.guessr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import fr.eni.guessr.model.Guess;

public class PlayActivity extends AppCompatActivity {

    private Guess currentGuess;
    private String currentGuessString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference myRef = MainActivity.firebaseDatabase.getReference("levels").child("level1").child("guess1");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentGuess = dataSnapshot.getValue(Guess.class);
                setViewValues();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TOTO", "loadPost:onCancelled", databaseError.toException());
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
}
