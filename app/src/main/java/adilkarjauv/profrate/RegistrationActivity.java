package adilkarjauv.profrate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userEmailRegistration;
    private EditText userPasswordRegistration;
    private EditText userPasswordRegistrationAgain;
    private Button registrationButtonRegistration;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userEmailRegistration = (EditText) findViewById(R.id.userEmailRegistration);
        userPasswordRegistration = (EditText) findViewById(R.id.userPasswordRegistration);
        userPasswordRegistrationAgain = (EditText) findViewById(R.id.userPasswordRegistrationAgain);
        registrationButtonRegistration = (Button) findViewById(R.id.registrationButtonRegistration);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("RegistrationActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("RegistrationActivity", "onAuthStateChanged:signed_out");
                }
            }
        };

        registrationButtonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegButton();
            }
        });
    }

    private void onRegButton() {

        String email = userEmailRegistration.getText().toString().replaceAll("\\s+$", "");
        String password = userPasswordRegistration.getText().toString().replaceAll("\\s+$", "");
        String password1 = userPasswordRegistrationAgain.getText().toString().replaceAll("\\s+$", "");

        if (email.length() == 0) {
            Toast.makeText(RegistrationActivity.this, "Insert email",
                    Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(RegistrationActivity.this, "Insert password",
                    Toast.LENGTH_SHORT).show();
        } else if (password1.length() == 0) {
            Toast.makeText(RegistrationActivity.this, "Insert password again",
                    Toast.LENGTH_SHORT).show();
        } else if (!password.equals(password1)) {
            Toast.makeText(RegistrationActivity.this, "Something is wrong, please check again!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegistrationActivity.this, "Logging In, Please Wait!", Toast.LENGTH_SHORT).show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("RegistrationActivity", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Something went wrong, please make sure you entered valid email address and password",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                nextActivity();
                            }
                        }
                    });
        }
    }

    private void nextActivity() {
        Intent i = new Intent(this, FeedActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
