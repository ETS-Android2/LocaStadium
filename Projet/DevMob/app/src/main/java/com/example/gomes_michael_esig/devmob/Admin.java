package com.example.gomes_michael_esig.devmob;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class Admin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //Variables
    private GoogleApiClient googleApiClient;
    ImageView photoLogin;
    TextView nameLogin;
    TextView emailLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Authentification
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Assignation
        photoLogin = (ImageView) findViewById(R.id.imgLog);
        nameLogin = (TextView) findViewById(R.id.nameLog);
        emailLogin = (TextView) findViewById(R.id.emailLog);
    }

    //Google Authentification
    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    //Google Authentification
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            nameLogin.setText(account.getDisplayName());
            emailLogin.setText(account.getEmail());
            Glide.with(this).load(account.getPhotoUrl()).into(photoLogin);

        } else {
            goMainScreen();
        }
    }

    //Google Authentification
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void logOut(View view) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goMainScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Impossible de fermer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //Lien avec autres layout
    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openAjouterStade(View view) {
        Intent i = new Intent(this, AjouterStade.class);
        startActivity(i);
    }

}

/*
Code inspié du tutoriel : https://www.youtube.com/watch?v=O3aemJ9eAAA
Aide : https://developers.google.com/identity/sign-in/android/sign-in
Meme problème : https://stackoverflow.com/questions/43015476/googlesigninresult-always-returning-not-success-staus
Résolution problème token null : https://stackoverflow.com/questions/34685928/token-null-sign-in-google-account
*/


