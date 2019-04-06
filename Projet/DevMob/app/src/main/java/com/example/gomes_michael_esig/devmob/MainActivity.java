package com.example.gomes_michael_esig.devmob;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    //Google Authentification
    private GoogleApiClient googleApiClient;
    private SignInButton signIn;
    public static final int CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Google Authentification
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signIn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, CODE);
            }
        });

    }
    //Google Authentification
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Google Authentification
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            Log.d("", "handleSignInResult:" + result.toString() + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            handleSignInResult(result);
        }
    }

    //Google Authentification
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            //Test d'accès
            Log.d("", "NOM:" + account.getDisplayName());
            if (account.getDisplayName().equals("elv-michael.gmsds@eduge.ch") || (account.getDisplayName().equals("MICHAEL ELV-MICHAEL.GMSDS"))) {
                goAdminScreen();
            } else {
                Toast.makeText(this, "Accès réservé à l'administrateur", Toast.LENGTH_SHORT).show();
                logOut();
                goMainScreen();
            }
        } else {
            Toast.makeText(this, "La session ne peut pas se lancer", Toast.LENGTH_SHORT).show();
        }
    }

    public void logOut() {
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

    /* Lien avec autres layout*/
    public void openStadeliste(View view) {
        Intent i = new Intent(this, Stade.class);
        startActivity(i);
    }

    public void openPhoto(View view) {
        Intent i = new Intent(this, Photo.class);
        startActivity(i);
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goAdminScreen() {
        Intent intent = new Intent(this, Admin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}


/*
Code inspiré du tutoriel : https://www.youtube.com/watch?v=O3aemJ9eAAA
Aide : https://developers.google.com/identity/sign-in/android/sign-in
Meme problème : https://stackoverflow.com/questions/43015476/googlesigninresult-always-returning-not-success-staus
*/
