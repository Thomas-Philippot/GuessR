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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

public class ProposalActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText editText;
    private Uri capturedImageUri = null;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal);

        this.storageRef = FirebaseStorage.getInstance().getReference();
        this.imageView = this.findViewById(R.id.img_porpose);
        this.editText = this.findViewById(R.id.et_mot);

        InputFilter[] editFilters = this.editText.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        this.editText.setFilters(newFilters);
    }

    public void btnImgToPorposeCameraClicked(View view) {
        File file = new File(getExternalFilesDir("image"), "toto.jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        capturedImageUri = Uri.fromFile(file);

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setMaxCropResultSize(6000,6000)
                .setInitialCropWindowRectangle(new Rect(6000,6000,6000,6000))
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
            }
        }
    }

    public void btnValidatePorposeClicked(View view) {
        if(this.capturedImageUri == null){
            Toast.makeText(this, "Veuillez sélectionner une image", Toast.LENGTH_SHORT).show();
        } else if ("".equals(String.valueOf(editText.getText()).trim())){
            Toast.makeText(this, "Veuillez entrer un mot décrivant l'image", Toast.LENGTH_SHORT).show();
        } else {
            StorageReference imgRef = storageRef.child("guess/" + editText.getText());

            imgRef.putFile(this.capturedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.wtf("TOTO : ", String.valueOf(taskSnapshot.getUploadSessionUri()));
                            Toast.makeText(ProposalActivity.this, "Success Upload", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.wtf("TOTO : ", exception.getMessage());
                            Toast.makeText(ProposalActivity.this, "Failure Upload", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            Log.wtf("TOTO : ", "Progress " + (int) progress + "%...");
                        }
                    });
        }
    }
}
