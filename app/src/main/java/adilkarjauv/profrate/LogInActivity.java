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

public class LogInActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registrationButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText userEmail;
    private EditText userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            nextActivity();
        } else {
            setContentView(R.layout.activity_log_in);

            userEmail = (EditText) findViewById(R.id.userEmail);
            userPassword = (EditText) findViewById(R.id.userPassword);

            mAuth = FirebaseAuth.getInstance();

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d("LogInAcivity", "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                        // User is signed out
                        Log.d("LogInAcivity", "onAuthStateChanged:signed_out");
                    }
                    // ...
                }
            };

            loginButton = (Button) findViewById(R.id.loginButton);
            registrationButton = (Button) findViewById(R.id.registrationButton);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoginButton();
                }
            });
            registrationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRegistrationButton();
                }
            });
        }

    }

    private void onRegistrationButton() {
        Intent i = new Intent(this, RegistrationActivity.class);
        startActivity(i);
    }

    private void onLoginButton() {
        String email = userEmail.getText().toString().replaceAll("\\s+$", "");
        String password = userPassword.getText().toString().replaceAll("\\s+$", "");
        if (email.length() == 0) {
            Toast.makeText(LogInActivity.this, "Insert email", Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(LogInActivity.this, "Insert password", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LogInActivity.this, "Logging In, Please Wait!", Toast.LENGTH_SHORT).show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("LogInActivity", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w("LogInActivity", "signInWithEmail:failed", task.getException());
                                Toast.makeText(LogInActivity.this, "Login or password is incorrect",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                nextActivity();
                            }
                        }
                    });
        }
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

    private void nextActivity() {
        Intent i = new Intent(this, FeedActivity.class);
        startActivity(i);
        this.finish();
    }
}
