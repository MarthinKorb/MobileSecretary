package mobiletcc.curso.br.mobilesecretarytcc;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

public class VerConsAdminActivity extends AppCompatActivity {

    private ListView listViewConsAdm;

    private FirebaseUser user;
    private FirebaseAuth auth;

    //FirebaseDatabase databaseAdm;
    private DatabaseReference referenceAdm;

    private List<Consulta> listaConsultaAdmin = new ArrayList<Consulta>();
    private ArrayAdapter<Consulta> arrayAdapterConsultaAdmin;

    private Consulta consultaSelecionadaAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_cons_admin);

        inicializarComponentes();
        eventoClick();
        inicializaDatabase();
        eventoDatabase();
    }

    private void eventoDatabase() {

        Query query;

        query = referenceAdm.child("Consulta").orderByChild("timeStamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaConsultaAdmin.clear();
                for (DataSnapshot dtSnapshot: dataSnapshot.getChildren()){
                    Consulta cAdmin = dtSnapshot.getValue(Consulta.class);

                    if (cAdmin.getIdUser() != null){
                        listaConsultaAdmin.add(cAdmin);
                    }
                }
                if (listaConsultaAdmin.isEmpty()){
                    alert("Nenhuma consulta marcada!");
                }
                arrayAdapterConsultaAdmin = new ArrayAdapter<Consulta>(VerConsAdminActivity.this,
                        android.R.layout.simple_list_item_1, listaConsultaAdmin);
                listViewConsAdm.setAdapter(arrayAdapterConsultaAdmin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializaDatabase() {
        FirebaseApp.initializeApp(VerConsAdminActivity.this);
        referenceAdm = FirebaseDatabase.getInstance().getReference();
    }

    private void eventoClick() {

        listViewConsAdm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                consultaSelecionadaAdmin = (Consulta)parent.getItemAtPosition(position);
                confirmarCancelamento();
                //terminarConsulta();
            }
        });
    }

    private void confirmarCancelamento() {
        AlertDialog.Builder alertas = new AlertDialog.Builder(VerConsAdminActivity.this);
        alertas.setTitle("Finalizar!");
        alertas.setIcon(R.mipmap.ic_aviso)
                .setMessage("Você deseja finalizar a atividade do dia: " + "\n\n" + consultaSelecionadaAdmin.getDia() + " às "
                        + consultaSelecionadaAdmin.getHora()+ " horas?")
                .setCancelable(true)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                terminarConsulta();
            }
        });
        AlertDialog alertDialog  = alertas.create();
        alertDialog.show();
    }

    private void terminarConsulta() {
        Consulta c = new Consulta();
        c.setIdConsulta(consultaSelecionadaAdmin.getIdConsulta());
        c.setIdUser(consultaSelecionadaAdmin.getIdUser());
        c.setEmailUser(consultaSelecionadaAdmin.getEmailUser());
        c.setHora(consultaSelecionadaAdmin.getHora());
        c.setDia(consultaSelecionadaAdmin.getDia());
        c.setTimeStamp(consultaSelecionadaAdmin.getTimeStamp());
        referenceAdm.child("LogConsultas").child(c.getIdConsulta()).setValue(c);
        referenceAdm.child("Consulta").child(consultaSelecionadaAdmin.getIdConsulta()).removeValue().
                addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                alert("Consulta finalizada com sucesso!");
            }
        });
    }

    private void inicializarComponentes() {
        listViewConsAdm = findViewById(R.id.listView_Cons_AdmId);
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
