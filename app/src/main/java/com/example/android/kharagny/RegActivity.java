package com.example.android.kharagny;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class RegActivity extends AppCompatActivity
{
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 21;

    final Context mContext = this;

    Button emailLoginButt;
    EditText usernameEdtTxt;
    EditText passwordEdtTxt;
    Intent intent ;
    //shared preferences
    SharedPreferences lastLoginSharedPreference;
    SharedPreferences.Editor lastLoginSharedPreferenceEditor;
    //firebase auth
    FirebaseAuth firebaseAuth;
    //firebase realtime database
    DatabaseReference firebaseDatabase;
    DatabaseReference usersRef;
    //facebook
    View fbButt, gButt;
    CallbackManager fbCallbackManager;
    //google
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);



        fbButt = findViewById(R.id.sign_up_fb_butt);
        gButt = findViewById(R.id.sign_up_g_butt);

        //shared preferences
        lastLoginSharedPreference = getSharedPreferences("last_login", MODE_PRIVATE);
        lastLoginSharedPreferenceEditor = lastLoginSharedPreference.edit();

        //firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //firebase realtime database
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        usersRef = firebaseDatabase.child("users");

        //sign in with facebook

        fbCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                AccessToken fbAccessToken = loginResult.getAccessToken();
                AuthCredential fbAuthCredential = FacebookAuthProvider.getCredential(fbAccessToken.getToken());
                connectToFirebaseWithCredential(fbAuthCredential);

                final Profile[] fbProfile = {Profile.getCurrentProfile()};

                //if we cannot get the current profile, maybe something has changed with it
                if (fbProfile[0] == null)
                {
                    ProfileTracker profileTracker = new ProfileTracker()
                    {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile)
                        {
                            fbProfile[0] = currentProfile;
                            System.out.println("/// old profile: " + oldProfile.getName());
                            System.out.println("/// current profile: " + currentProfile.getName());
                        }
                    };
                }
                GraphRequest fbGraphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        System.out.println("/// GraphRequest response: " + response.toString());

                        try
                        {
                            System.out.println("/// GraphRequest email: " + object.getString("email"));
                            System.out.println("/// \n\nthe whole JSON object\n\n\n" + object.toString());
                        } catch (JSONException e)
                        {
                            System.out.println("/// error: " + e.toString());
                        }
                    }
                });
            }
            @Override
            public void onCancel()
            {
                Toast.makeText(mContext, "Login Canceled", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error)
            {
                popAnAlertDialogUp("Error", error.getMessage());
            }
        });

        //facebook sing up button
        fbButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //attempt to login to facebook and request only public profile info and email
                LoginManager.getInstance().logInWithReadPermissions(RegActivity.this, Arrays.asList("public_profile", "email"));
            }
        });

        //end: sign up with facebook



        //sign up with google
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id_auto_created_1))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //google sign in button
        gButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //create and show the sign in intent of google. the result will be shown in onActivityResult()
                Intent googleSignInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(googleSignInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
            }
        });
        //end: sign up with google



        //email sign up

        final EditText usernameEdtTxt = findViewById(R.id.editText2);
        final EditText passAgainEdtTxt = findViewById(R.id.editText);
        final EditText passEdtTxt = findViewById(R.id.emailText);
        final EditText emailEdtTxt = findViewById(R.id.editText3);
        Button signUpButt = findViewById(R.id.login_butt);

        firebaseAuth = FirebaseAuth.getInstance();

        signUpButt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String username = usernameEdtTxt.getText().toString();
                String pass = passEdtTxt.getText().toString();
                String passAgain = passAgainEdtTxt.getText().toString();
                String email = emailEdtTxt.getText().toString();

                if (username.equals("") || pass.equals("") || passAgain.equals("") || email.equals(""))
                {
                    popAnAlertDialogUp("Error", "Make sure to fill all of the fields");
                    return;
                }
                if (!pass.equals(passAgain))
                {
                    popAnAlertDialogUp("Error", "The password doesn't match the confirm password");
                    return;
                }

                //attempt to sign up with the given email and pass
                Task<AuthResult> signUpTask = firebaseAuth.createUserWithEmailAndPassword(email, pass);

                //on success
                signUpTask.addOnSuccessListener(new OnSuccessListener<AuthResult>()
                {
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
                        //send verification email
                        authResult.getUser().sendEmailVerification();
                        //add to database

                    }
                });

                //on failure
                signUpTask.addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        popAnAlertDialogUp("error", e.toString() + "\nTry again");
                    }
                });
            }
        });

        //end: email sign up
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (fbCallbackManager != null)
            fbCallbackManager.onActivityResult(requestCode, resultCode, data);

        //if this is true then the result is comming from the google sign in intent
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE)
        {
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try
            {
                GoogleSignInAccount googleSignInAccountResult = googleSignInAccountTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccountResult.getIdToken(), null);
                connectToFirebaseWithCredential(authCredential);

                //[debug] show data
                String s = "";
                s += "getIdToken: " + googleSignInAccountResult.getIdToken() + '\n';
                s += "getId: " + googleSignInAccountResult.getId() + '\n';
                s += "getDisplayName: " + googleSignInAccountResult.getDisplayName() + '\n';
                s += "getEmail: " + googleSignInAccountResult.getEmail();
                popAnAlertDialogUp("[debug] google account data", s);
            }
            catch (ApiException apiException)
            {
                popAnAlertDialogUp("error", "could not sign in to google\n" + apiException.toString());
            }
        }
    }


    private void connectToFirebaseWithCredential(AuthCredential credential)
    {
        Task<AuthResult> connectToFirebaseTask = firebaseAuth.signInWithCredential(credential);

        //on success
        connectToFirebaseTask.addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    updateDatabase(firebaseUser);
                    //go to the next screen (choose preferences)
                }
            }
        });
        //on failure
        connectToFirebaseTask.addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                popAnAlertDialogUp("[debug] error @ connectGoogleToFirebase -> connectToFirebaseTask.addOnFailureListener", e.toString());
            }
        });
    }

    private void updateDatabase(FirebaseUser firebaseUser)
    {
        User newUser = new User(firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhotoUrl().toString(), null);
        HashMap<String,Object> newUserToDatabase = new HashMap<>();
        newUserToDatabase.put(firebaseUser.getUid(), newUser);

        usersRef.updateChildren(newUserToDatabase).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                popAnAlertDialogUp("Error", e.toString() + "\nTry again later");
            }
        });

        goToHome(firebaseUser.getUid());
    }

    private void goToHome(String uid)
    {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.putExtra("uid", uid);
        startActivity(homeIntent);
    }

    //tools
    void popAnAlertDialogUp(Object title, Object message)
    {
        if (title == null)
            title = "";
        if (message == null)
            message = "";

        new AlertDialog.Builder(this)
                .setTitle(title.toString())
                .setMessage(message.toString())
                .show();
    }
}
