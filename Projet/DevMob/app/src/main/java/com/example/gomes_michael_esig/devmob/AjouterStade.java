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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class AjouterStade extends AppCompatActivity {

    private EditText texteAdresse;
    private Button add;
    private FirebaseFirestore db;
    private  String obj;
    private String editAdresse;

    //    Read
    ListView listview;
    List<String> adrList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_stade);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        db = FirebaseFirestore.getInstance();
        add = (Button) findViewById(R.id.btnAjouter);
        texteAdresse = (EditText) findViewById(R.id.txtAdresse);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (texteAdresse.getText().toString().isEmpty()) {
                    Toast.makeText(AjouterStade.this, "Le champs est vide", Toast.LENGTH_SHORT).show();
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

//        READ
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

//        carte
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Object o = listview.getItemAtPosition(position);
                editAdresse = (String) o;
                carte(arg1);
                Log.d("", "Stade :" + o);
            }
        });

//        Delete
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, final long id) {
                // TODO Auto-generated method stub
                final String obj = (String) arg0.getItemAtPosition(pos);
                Log.v("long clicked", "pos: " + obj);


//                AlertDialog.Builder builder = new AlertDialog.Builder(AjouterStade.this);
//                builder.setCancelable(true);
//                builder.setTitle("Suppression");
//                builder.setMessage(Html.fromHtml("Voulez-vous supprimer" + "<br>" +  "<b>" +arg0.getItemAtPosition(pos)));
//                builder.setPositiveButton("Valider",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                               String op = db.collection("Adresse").document(test).getPath().toString();
//                                Toast.makeText(AjouterStade.this, "Data deleted !" + op,
//                                        Toast.LENGTH_SHORT).show();
//
//
//                                db.collection("Adresse")
//                                        .document(test)
//                                        .delete()
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Toast.makeText(AjouterStade.this, "Data deleted !",
//                                                        Toast.LENGTH_SHORT).show();
//
//                                            }
//                                        });


//                                DocumentReference  docref = db.collection("Adresse").document(test);
//                                Map<String,Object> updates = new HashMap<>();
//                                updates.put(test, FieldValue.delete());
//                                docref.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(AjouterStade.this, "Data deleted !",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                });


//                                Toast.makeText(AjouterStade.this, "Data deleted !",
//                                                Toast.LENGTH_SHORT).show();

//                                        .child("Adresse").child("Adresse").removeValue();
//
//                                database.child("Adresse").child(test).getRef().removeValue();
//                                db.collection("Adresse").document("Adresse")
//                                        .delete().addOnSuccessListener(new OnSuccessListener < Void > () {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Toast.makeText(AjouterStade.this, "Data deleted !",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                                .document(test);
//                                noteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task task) {
//                                        if(task.isSuccessful()){
//                                            Toast.makeText(AjouterStade.this, "OK BG", Toast.LENGTH_SHORT).show();
//                                        }
//                                        else{
//                                            Toast.makeText(AjouterStade.this, "FAUX", Toast.LENGTH_SHORT).show();
//                                        }
//
//                                    }
//                                });




//                            }
//                        });
//
//
//
//                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//
//                Log.v("long clicked", "pos: " + pos);
//
//                return true;
//            }
               Update(AjouterStade.this, obj);
                return true;
//        });
    }});}


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


    private void Update(Context c, final String obj) {
        final EditText taskEditText = new EditText(c);
        taskEditText.setText(obj);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Ajouter une saison")
                .setMessage("Comment voulez-vous appeler votre saison ?")
                .setView(taskEditText)
                .setPositiveButton("Créer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String docid = db.collection("Adresse").document().getId().toString();
                        DocumentReference ref = db.document("Adresse/Adresse");
                        String task = String.valueOf(taskEditText.getText());
                        Map<String, Object> upStade = new HashMap<>();
                        upStade.put("Adresse", task);
//                        DocumentReference ref = db.document("Adresse/Adresse");
                        ref.set(upStade);

                        Toast.makeText(getApplicationContext(), docid, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

}


//Inspiré pour l'insert : https://www.youtube.com/watch?v=7hwlMKUgTQc
//Inspiré pour le read : https://www.youtube.com/watch?v=S6nLyzW6Jyo
//Carte inspiré du TP