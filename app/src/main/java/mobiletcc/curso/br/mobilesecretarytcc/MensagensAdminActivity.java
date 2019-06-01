package mobiletcc.curso.br.mobilesecretarytcc;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.Random;
import java.util.UUID;

import mobiletcc.curso.br.mobilesecretarytcc.Modelo.Mensagem;
import mobiletcc.curso.br.mobilesecretarytcc.Modelo.RespostaAdmin;

public class MensagensAdminActivity extends AppCompatActivity {

    private ListView listView_MensAdmin;

    private DatabaseReference reference;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private List<Mensagem> listaMensagensAdmin = new ArrayList<Mensagem>();
    private ArrayAdapter<Mensagem> arrayAdapterMensagensAdmin;

    private Mensagem mensagemSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens_admin);

        auth = FirebaseAuth.getInstance();

        listView_MensAdmin = findViewById(R.id.listView_MensAdminID);

        inicializarDatabase();
        eventoDatabase();
        eventoClick();

    }

    private void eventoClick() {
        listView_MensAdmin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mensagemSelecionada = (Mensagem)parent.getItemAtPosition(position);
                responderMensagem();
            }
        });
    }

    private void responderMensagem() {
        /*
        final AlertDialog.Builder alertas = new AlertDialog.Builder(MensagensAdminActivity.this);
        alertas.setTitle("Responder Mensagem");
        alertas.setIcon(R.mipmap.ic_aviso)
                .setMessage("")
                .setCancelable(true)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                mensagemSelecionada.getEmailUser(),Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enviarResposta();
            }
        });
        AlertDialog alertDialog  = alertas.create();
        alertDialog.show();
        */

        final AlertDialog.Builder builder = new AlertDialog.Builder(MensagensAdminActivity.this);
        builder.setTitle("Responder à "+ mensagemSelecionada.getEmailUser());
        builder.setIcon(R.mipmap.ic_aviso);

        final EditText input = new EditText(MensagensAdminActivity.this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String respostaAdmin = input.getText().toString().trim();


                if (input.getText().toString().trim().equals("")) {
                    Toast.makeText(MensagensAdminActivity.this,
                            "Escreva uma mensagem!", Toast.LENGTH_SHORT).show();
                } else {
                    enviarResposta(respostaAdmin);
                }

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

}

    private void enviarResposta(String respostaAdmin) {
        alert("Resposta enviada à "+ mensagemSelecionada.getEmailUser());

        RespostaAdmin r = new RespostaAdmin();

        r.setIdMensagem(UUID.randomUUID().toString());
        r.setEmitente("uZAnSSHu9wRocrLEEy7pDhbPup22");
        r.setDestinatario(mensagemSelecionada.getEmailUser());
        r.setIdDestinatario(mensagemSelecionada.getIdUser());
        r.setMensagemResposta(respostaAdmin);
        r.setTimeStamp(System.currentTimeMillis());

        reference.child("RespostaAdmin").child(r.getIdMensagem()).setValue(r);

    }

    private void eventoDatabase() {

        Query query;

        query = reference.child("Mensagem").orderByChild("timeStamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaMensagensAdmin.clear();
                for (DataSnapshot dtSnapshot:dataSnapshot.getChildren()){
                    Mensagem m = dtSnapshot.getValue(Mensagem.class);

                    if (user.getUid().equals("uZAnSSHu9wRocrLEEy7pDhbPup22")){
                        listaMensagensAdmin.add(m);
                    }
                    arrayAdapterMensagensAdmin = new ArrayAdapter<Mensagem>(MensagensAdminActivity.this,
                            android.R.layout.simple_list_item_1, listaMensagensAdmin);
                    listView_MensAdmin.setAdapter(arrayAdapterMensagensAdmin);
                }
                if (listaMensagensAdmin.isEmpty()){
                    alert("Você não tem mensagens!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void alert(String s) {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
    }

    private void inicializarDatabase() {
        FirebaseApp.initializeApp(MensagensAdminActivity.this);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }
}
