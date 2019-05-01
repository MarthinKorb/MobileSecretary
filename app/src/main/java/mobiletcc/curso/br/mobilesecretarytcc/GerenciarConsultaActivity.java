package mobiletcc.curso.br.mobilesecretarytcc;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mobiletcc.curso.br.mobilesecretarytcc.Modelo.Consulta;

public class GerenciarConsultaActivity extends AppCompatActivity {

    private ListView listView_ConsultaUser;
    private Button btnVerHorDisp;

    private FirebaseAuth auth;
    private FirebaseUser user;

    //FirebaseDatabase databaseGerCon;
    private DatabaseReference referenceGerCon;

    private List<Consulta> listaConsultaDados = new ArrayList<Consulta>();
    private ArrayAdapter<Consulta> arrayAdapterGerConsulta;

    private Consulta consultaSelecionadaDados;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_consulta);

        listView_ConsultaUser = findViewById(R.id.listView_ConsultaUserId);
        btnVerHorDisp = findViewById(R.id.btnVerHorDispId);

        inicializarDatabase();
        eventoDatabase();
        //testaDataAgendamento();


        listView_ConsultaUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                consultaSelecionadaDados =(Consulta)parent.getItemAtPosition(position);
                confirmarCancelamento();
            }
        });

        btnVerHorDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ConsultasDisponiveisActivity.class));
            }
        });
    }

   /* @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void notificaUser() {

        String msg = "Não esqueça do seu agendamento no dia "+ consultaSelecionadaDados.getDia()
                + " às "+ consultaSelecionadaDados.getHora();
        Intent intent = new Intent(getApplicationContext(), SobreActivity.class);
        intent.putExtra("mensagem", msg);

        int id = Integer.parseInt(user.getUid());

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getBaseContext())
                .setContentTitle("Está quase na hora...")
                .setContentText(msg)
                .setSmallIcon(R.mipmap.ic_aviso)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void testaDataAgendamento() {
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);
        int hora = calendar.get(Calendar.HOUR_OF_DAY);

        if (referenceGerCon.child("Consulta").child("dia").equals(dia+"/"+(mes+1)+"/"+ano)){
            alert(dia + "/" + mes + "/" + ano);
        }
    }*/

    private void confirmarCancelamento() {
        AlertDialog.Builder alertas = new AlertDialog.Builder(GerenciarConsultaActivity.this);
        alertas.setTitle("Desmarcar Agendamento");
        alertas.setIcon(R.mipmap.ic_aviso)
                .setMessage("Você deseja desmarcar o seu agendamento do dia: " + "\n\n" + consultaSelecionadaDados.getDia() + " às "
                        + consultaSelecionadaDados.getHora()+ " horas?")
                .setCancelable(true)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                desmarcarConsulta();
            }
        });
        AlertDialog alertDialog  = alertas.create();
        alertDialog.show();
    }

    private void eventoDatabase() {

        Query query;

        query = referenceGerCon.child("Consulta").orderByChild("timeStamp");

       query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaConsultaDados.clear();

                for (DataSnapshot dtSnapshot:dataSnapshot.getChildren()){
                    Consulta c = dtSnapshot.getValue(Consulta.class);

                    if (user.getUid().equals(c.getIdUser())){
                        listaConsultaDados.add(c);
                    }
                }
                arrayAdapterGerConsulta = new ArrayAdapter<Consulta>(GerenciarConsultaActivity.this,
                        android.R.layout.simple_list_item_1, listaConsultaDados);
                listView_ConsultaUser.setAdapter(arrayAdapterGerConsulta);

                if (listaConsultaDados.isEmpty()){
                    alert("Você não tem agendamentos!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void inicializarDatabase() {
        FirebaseApp.initializeApp(GerenciarConsultaActivity.this);
        referenceGerCon = FirebaseDatabase.getInstance().getReference();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gerconsulta, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_gerconsulta){
                desmarcarConsulta();
            }
        return true;
    }
    */

    private void desmarcarConsulta() {
        Consulta c = new Consulta();
        c.setIdConsulta(consultaSelecionadaDados.getIdConsulta());
        c.setIdUser(user.getUid());
        c.setEmailUser(user.getEmail());
        c.setTimeStamp(System.currentTimeMillis());
       // referenceGerCon.child("Consulta").child(c.getIdConsulta()).child("timeStamp").setValue(c.getTimeStamp());
        referenceGerCon.child("Consulta").child(c.getIdConsulta()).child("emailUser").removeValue();
        referenceGerCon.child("Consulta").child(c.getIdConsulta()).child("idUser").removeValue().
                addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                alert("Consulta desmarcada com sucesso!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alert(e.toString());
            }
        });

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
