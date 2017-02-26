package adilkarjauv.profrate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AddProfessorActivity extends AppCompatActivity {

    private RoundedImageView addProfessorImageView;
    private EditText addProfessorNameEditText;
    private EditText addProfessorUniEditText;
    private Button addProfessorAddButton;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private String url = "Adil";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_professor);
        FeedActivity.bottomNavigation.setCurrentItem(0);

        addProfessorImageView = (RoundedImageView) findViewById(R.id.addProfessorImageView);
        addProfessorNameEditText = (EditText) findViewById(R.id.addProfessorNameEditText);
        addProfessorUniEditText = (EditText) findViewById(R.id.addProfessorUniEditText);
        addProfessorAddButton = (Button) findViewById(R.id.addProfessorAddButton);

        addProfessorImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addProfessorImageView.setCornerRadius((float) 100);
        addProfessorImageView.mutateBackground(false);
        addProfessorImageView.setOval(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        addProfessorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageView();
            }
        });

        addProfessorAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButton();
            }
        });
    }

    private void onImageView() {
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(imageReturnedIntent.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, imageReturnedIntent);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            addProfessorImageView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void onAddButton() {
        String professorName = addProfessorNameEditText.getText().toString();
        final String university = addProfessorUniEditText.getText().toString();
        if (professorName.length() == 0) {
            Toast toast = Toast.makeText(this, "Insert name", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (university.length() == 0) {
            Toast toast = Toast.makeText(this, "Insert the department", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            StorageReference reference = storage.getReferenceFromUrl("gs://profrate-93d9b.appspot.com");
            StorageReference child = reference.child(professorName + ".jpg");

            addProfessorImageView.setDrawingCacheEnabled(true);
            addProfessorImageView.buildDrawingCache();
            Bitmap bitmap = addProfessorImageView.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = child.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    url = downloadUrl.toString();
                    String professorName = addProfessorNameEditText.getText().toString();
                    mDatabase.child("professors").child(professorName).child("imageUrl").setValue(url);
                    Professor newProf = new Professor(professorName, university, url);
                    FeedActivity.Profs1.add(newProf);
                    WriteActivity.map.put(newProf.getName(), newProf);
                    addProfessorNameEditText.setText("");
                }
            });

            mDatabase.child("professors").child(professorName).child("name").setValue(professorName);
            mDatabase.child("professors").child(professorName).child("university").setValue(university);
            addProfessorUniEditText.setText("");
            addProfessorImageView.setImageBitmap(null);
            Toast.makeText(this, "It has been added successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
