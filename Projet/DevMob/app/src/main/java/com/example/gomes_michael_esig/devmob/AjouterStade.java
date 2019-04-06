package com.example.gomes_michael_esig.devmob;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class AjouterStade extends AppCompatActivity {

    //Variables
    private EditText texteAdresse;
    private Button add;
    private FirebaseFirestore db;
    private String obj;
    private String editAdresse;
    ListView listview;
    List<String> adrList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_stade);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Assignation aux variables
        db = FirebaseFirestore.getInstance();
        add = (Button) findViewById(R.id.btnAjouter);
        texteAdresse = (EditText) findViewById(R.id.txtAdresse);

        //Insertion d'un stade
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (texteAdresse.getText().toString().isEmpty()) {
                    Toast.makeText(AjouterStade.this, "Le champ est vide", Toast.LENGTH_SHORT).show();
                    return;
                }
                String adresse = texteAdresse.getText().toString();
                Map<String, String> userMap = new HashMap<>();
                userMap.put("Adresse", adresse);
                db.collection("Adresse").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AjouterStade.this, "L'adresse a été inséré", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage();
                        Toast.makeText(AjouterStade.this, "Erreur " + error, Toast.LENGTH_SHORT).show();
                    }
                });
                texteAdresse.setText("");
            }

        });



//       Affichage des données
        listview = (ListView) findViewById(R.id.listStade);
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

//       Ouverture de la maps
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = listview.getItemAtPosition(position);
                editAdresse = (String) o;
                carte(arg1);
                Log.d("", "Stade :" + o);
            }
        });

//       Modification
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, final long id) {
                // TODO Auto-generated method stub
                final String obj = (String) arg0.getItemAtPosition(pos);
                Log.v("long clicked", "pos: " + obj);
                Update(AjouterStade.this, obj);
                return true;
            }
        });
    }


    private void Update(Context c, final String obj) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setText(obj);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Modification")
                .setMessage("Modifier le stade :")
                .setIcon(R.drawable.logo)
                .setView(taskEditText)
                .setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Adresse").whereEqualTo("Adresse", obj).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w("", "listen:error", e);
                                    return;
                                }
                                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                                    String docid = dc.getDocument().getId();
                                    DocumentReference ref = db.document("Adresse/" + docid);
                                    String task = String.valueOf(taskEditText.getText());
                                    Map<String, Object> upStade = new HashMap<>();
                                    upStade.put("Adresse", task);
                                    ref.set(upStade);
                                    Toast.makeText(getApplicationContext(), "La modidfication a été enregistrée ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                })
                .setNeutralButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("Adresse").whereEqualTo("Adresse", obj).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w("", "listen:error", e);
                                    return;
                                }
                                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                                    String docid = dc.getDocument().getId();

                                    DocumentReference ref = db.document("Adresse/" + docid);
                                    ref.delete();
                                    Toast.makeText(getApplicationContext(), obj + " a été supprimé", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Annuler", null)
                .create();
        dialog.show();
    }


    public void carte(View view) {
        try {

            String vsAdresse = editAdresse;
            vsAdresse = vsAdresse.replace(' ', '+');
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

/*
Inspiré pour l'insert : https://www.youtube.com/watch?v=7hwlMKUgTQc
Inspiré pour le read : https://www.youtube.com/watch?v=S6nLyzW6Jyo
Carte inspiré du TP
J'ai posé la question sur un forum pour l'accès à l'id: https://stackoverflow.com/questions/55246790/get-document-identifier-firestore
*/
