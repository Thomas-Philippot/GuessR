package fr.eni.guessr;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import fr.eni.guessr.model.Guess;
import pl.droidsonroids.gif.GifImageView;

public class ProposalActivity extends AppCompatActivity {
    private ImageView imageView;
    private GifImageView loadingView;
    private EditText editText;
    private FloatingActionButton btnImgToPorpose;
    private FloatingActionButton btnValidatePorpose;

    private Uri capturedImageUri = null;
    private StorageReference storageRef;
    private boolean isAlreadyExist;
    private int lastGuessNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal);
        this.storageRef = FirebaseStorage.getInstance().getReference();

        this.imageView = this.findViewById(R.id.img_porpose);
        this.loadingView = this.findViewById(R.id.gif_loading);
        this.editText = this.findViewById(R.id.et_mot);
        this.btnImgToPorpose = this.findViewById(R.id.btn_img_to_porpose);
        this.btnValidatePorpose= this.findViewById(R.id.btn_validate_porpose);

        InputFilter[] editFilters = this.editText.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        this.editText.setFilters(newFilters);

        this.loadingView.setVisibility(View.INVISIBLE);
    }

    public void btnImgToPorposeCameraClicked(View view) {
        File file = new File(getExternalFilesDir("image"), "toto.jpg");
        if (file.exists()) {
            boolean delete = file.delete();
        }
        try {
            boolean newFile = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.capturedImageUri = Uri.fromFile(file);

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setMaxCropResultSize(6000, 6000)
                .setInitialCropWindowRectangle(new Rect(6000, 6000, 6000, 6000))
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CROP
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                this.capturedImageUri = result.getUri();
                imageView.setImageURI(this.capturedImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("Error", Objects.requireNonNull(error.getMessage()));
            }
        }
    }

    public void btnValidatePorposeClicked(View view) {
        this.loading(true);

        if (this.capturedImageUri == null) {
            this.loading(false);
            Toast.makeText(this, "Veuillez sélectionner une image", Toast.LENGTH_LONG).show();
        } else if ("".equals(String.valueOf(editText.getText()).trim())) {
            this.loading(false);
            Toast.makeText(this, "Veuillez entrer un mot décrivant l'image", Toast.LENGTH_LONG).show();
        } else {
            final String editTextString = String.valueOf(editText.getText());

            //PUT GUESS OBJECT INTO FIREBASE
            if(editTextString.length() < 4) {
                this.loading(false);
                Toast.makeText(this, "Le mot doit contenir au moins 4 lettres", Toast.LENGTH_LONG).show();
            } else {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final int levelToSearch = editTextString.length() - 3;
                final DatabaseReference myRef = database.getReference("levels").child("level" + levelToSearch);
                isAlreadyExist = false;
                lastGuessNumber = 0;

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshotLevel) {
                        if(!snapshotLevel.hasChild("id")){
                            Log.d("TOTO, LEVEL : ", "Creation de l'id");
                            myRef.child("id").setValue(levelToSearch);
                        }
                        for(DataSnapshot guessSnapshot : snapshotLevel.getChildren()){
                            if(!"id".equals(guessSnapshot.getKey())){
                                int toCompare = Integer.parseInt(Objects.requireNonNull(guessSnapshot.getKey()).substring(5));
                                if(toCompare > lastGuessNumber){
                                    lastGuessNumber = Integer.parseInt(Objects.requireNonNull(guessSnapshot.getKey()).substring(5));
                                }
                                Guess guess = guessSnapshot.getValue(Guess.class);
                                assert guess != null;
                                if (guess.getAnswer().equals(editTextString)){
                                    isAlreadyExist = true;
                                }
                            }
                        }
                        if(isAlreadyExist){
                            ProposalActivity.this.loading(false);
                            Toast.makeText(ProposalActivity.this, "Ce mot existe déjà, veuillez en choisir un autre", Toast.LENGTH_LONG).show();
                        } else {
                            myRef.child("guess" + (lastGuessNumber + 1)).child("answer").setValue(editTextString);
                            myRef.child("guess" + (lastGuessNumber + 1)).child("image").setValue("guess/" + editTextString);
                            //PUT IMAGE TO FIREBASE
                            StorageReference imgRef = storageRef.child("guess/" + editTextString);

                            imgRef.putFile(capturedImageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            ProposalActivity.this.loading(false);
                                            Toast.makeText(ProposalActivity.this, "Guess ajouté avec succès", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Log.e("Erreur", Objects.requireNonNull(exception.getMessage()));
                                            ProposalActivity.this.loading(false);
                                            Toast.makeText(ProposalActivity.this, "Erreur lors de l'ajout...", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Erreur", databaseError.getMessage());
                        Toast.makeText(ProposalActivity.this, "Erreur...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void loading(boolean state) {
        if(state){
            this.loadingView.setVisibility(View.VISIBLE);
            this.imageView.setVisibility(View.INVISIBLE);
            this.editText.setVisibility(View.INVISIBLE);
            this.btnImgToPorpose.setVisibility(View.INVISIBLE);
            this.btnValidatePorpose.setVisibility(View.INVISIBLE);
        } else {
            this.loadingView.setVisibility(View.INVISIBLE);
            this.imageView.setVisibility(View.VISIBLE);
            this.editText.setVisibility(View.VISIBLE);
            this.btnImgToPorpose.setVisibility(View.VISIBLE);
            this.btnValidatePorpose.setVisibility(View.VISIBLE);
        }
    }
}
