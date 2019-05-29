package mobiletcc.curso.br.mobilesecretarytcc;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import mobiletcc.curso.br.mobilesecretarytcc.Modelo.Consulta;

public class HistoricoConsultasActivity extends AppCompatActivity {

    private ListView listViewHistCons;

    private FirebaseUser user;
    private FirebaseAuth auth;

    private DatabaseReference reference;

    private List<Consulta> listaHistCons = new ArrayList<Consulta>();
    private ArrayAdapter<Consulta> arrayAdapterlistaHistCons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_consultas);

        inicializarComponentes();
        inicializarDatabase();
        eventoDatabase();
    }

    private void eventoDatabase() {
        Query query;

        query = reference.child("LogConsultas").orderByChild("timeStamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaHistCons.clear();
                for (DataSnapshot dtSnapShot:dataSnapshot.getChildren()){
                    Consulta histCons = dtSnapShot.getValue(Consulta.class);

                    if (user.getUid().equals("uZAnSSHu9wRocrLEEy7pDhbPup22")){
                        listaHistCons.add(histCons);
                    }else{
                        if (user.getUid().equals(histCons.getIdUser())){
                            listaHistCons.add(histCons);
                        }
                    }
                }
                arrayAdapterlistaHistCons = new ArrayAdapter<Consulta>(HistoricoConsultasActivity.this,
                        android.R.layout.simple_list_item_1, listaHistCons);
                listViewHistCons.setAdapter(arrayAdapterlistaHistCons);

                if (listaHistCons.isEmpty()){
                    alert("Você não tem histórico de agendamentos!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alert(databaseError.getMessage());
            }
        });
    }

    private void inicializarDatabase() {
        FirebaseApp.initializeApp(HistoricoConsultasActivity.this);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    private void inicializarComponentes() {
        listViewHistCons = findViewById(R.id.listView_HistConsID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }

    private void alert(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
