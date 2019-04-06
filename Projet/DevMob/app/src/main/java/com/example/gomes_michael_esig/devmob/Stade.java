package com.example.gomes_michael_esig.devmob;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class Stade extends AppCompatActivity {
    private FirebaseFirestore db;
    ListView listview;
    List<String> adrList = new ArrayList<>();
    private String editAdresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stade);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//Garde clavier fermé au démarrage

        db = FirebaseFirestore.getInstance();

       //Remplissage de l'arraylist
        listview = (ListView) findViewById(R.id.lvStade);
        db.collection("Adresse").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                adrList.clear();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    adrList.add(snapshot.getString("Adresse"));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_selectable_list_item, adrList);
                adapter.notifyDataSetChanged();
                listview.setAdapter(adapter);
            }
        });

        //Recherche Manuelle stade
        ArrayAdapter<String> ad = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, adrList);
        final AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);
                editAdresse = selectedItem;
                carte(view);
            }
        });
        actv.setThreshold(1);//Commence à chercher depuis le premier caractère
        actv.setAdapter(ad);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.WHITE);
        actv.setTextSize(25);

        //Appel de la carte
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = listview.getItemAtPosition(position);
                editAdresse = (String) o;
                carte(arg1);
                Log.d("", "Stade :" + o);
            }
        });
    }

    public void carte(View view) {
        try {
            //n°1 Récupération du contenu saisi dans l'objet
            String vsAdresse = editAdresse;
            vsAdresse = vsAdresse.replace(' ', '+');
            //n°2 Emission de l'intention cartographique
            Intent geoIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=" + vsAdresse));
            startActivity(geoIntent);
        } catch (Exception e) {
            final AlertDialog.Builder adb = new AlertDialog.Builder(this);
            AlertDialog ad = adb.create();
            ad.setMessage("Echec de lancement de Maps");
            ad.show();
        }
    }
}
