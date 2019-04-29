package mobiletcc.curso.br.mobilesecretarytcc;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.UUID;

import mobiletcc.curso.br.mobilesecretarytcc.Modelo.Consulta;

public class ConsultasDisponiveisActivity extends AppCompatActivity {

    private ListView listView_Disponiveis;

    private FirebaseAuth auth;
    private FirebaseUser user;

    //FirebaseDatabase databaseDisp;
    private DatabaseReference referenceDisp;


    private List<Consulta> listaConsultaDisponiveis = new ArrayList<Consulta>();
    private ArrayAdapter<Consulta> arrayAdapterConsultaDisp;

    private Consulta consultaSelecionadaDisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas_disponiveis);

        listView_Disponiveis = findViewById(R.id.listView_DisponiveisId);

        inicializarDatabaseDisp();
        eventoDatabaseDisp();

        listView_Disponiveis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                consultaSelecionadaDisp =(Consulta)parent.getItemAtPosition(position);
                confirmarAgendamento();
                //alert("Para confirmar o horário, clique no ícone na barra superior direita!");
            }
        });
    }

    private void confirmarAgendamento() {
        AlertDialog.Builder alertas = new AlertDialog.Builder(ConsultasDisponiveisActivity.this);
        alertas.setTitle("Confirmar Agendamento");
        alertas.setIcon(R.mipmap.ic_aviso)
                .setMessage("Você deseja confirmar o agendamento para o dia: " + "\n\n" + consultaSelecionadaDisp.getDia() + " às "
                + consultaSelecionadaDisp.getHora()+ " horas?")
                .setCancelable(true)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                agendarConsulta();
            }
        });
        AlertDialog alertDialog  = alertas.create();
        alertDialog.show();
    }

    private void eventoDatabaseDisp() {

        Query query = referenceDisp.child("Consulta").orderByChild("timeStamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaConsultaDisponiveis.clear();
                for (DataSnapshot dtSnapshot:dataSnapshot.getChildren()){
                    Consulta cDisp = dtSnapshot.getValue(Consulta.class);
                    if (cDisp.getIdUser() == null){
                        listaConsultaDisponiveis.add(cDisp);
                    }
                }
                arrayAdapterConsultaDisp = new ArrayAdapter<Consulta>(ConsultasDisponiveisActivity.this,
                        android.R.layout.simple_list_item_1, listaConsultaDisponiveis);
                listView_Disponiveis.setAdapter(arrayAdapterConsultaDisp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarDatabaseDisp() {
        FirebaseApp.initializeApp(ConsultasDisponiveisActivity.this);
        referenceDisp = FirebaseDatabase.getInstance().getReference();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_disponiveis, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_disponiveis){
            agendarConsulta();
        }
        return true;
    }
*/
    private void agendarConsulta() {
            Consulta cDisp = new Consulta();
            cDisp.setIdConsulta(consultaSelecionadaDisp.getIdConsulta());
            cDisp.setIdUser(user.getUid());
            cDisp.setEmailUser(user.getEmail());
            cDisp.setDia(consultaSelecionadaDisp.getDia().trim());
            cDisp.setHora(consultaSelecionadaDisp.getHora().trim());
            cDisp.setTimeStamp(consultaSelecionadaDisp.getTimeStamp());
            referenceDisp.child("Consulta").child(cDisp.getIdConsulta()).setValue(cDisp);
            alert("Horário reservado com sucesso!");
    }

    private void alert(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }

}

