package mobiletcc.curso.br.mobilesecretarytcc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import mobiletcc.curso.br.mobilesecretarytcc.Modelo.Mensagem;
import mobiletcc.curso.br.mobilesecretarytcc.Modelo.RespostaAdmin;

public class MensagemActivity extends AppCompatActivity {

    private ImageView imgSendMessage;
    private EditText editMensagem;
    private ListView listView_Mensagens;
    private ListView listView_Resp;

    private DatabaseReference reference;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private List<Mensagem> listaMensagens = new ArrayList<Mensagem>();
    private List<RespostaAdmin> listaResp = new ArrayList<RespostaAdmin>();
    private ArrayAdapter<Mensagem> arrayAdapterMensagens;
    private ArrayAdapter<RespostaAdmin> arrayAdapterResp;

    private Mensagem mensagemSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        auth = FirebaseAuth.getInstance();

        inicializarComponentes();
        inicilizarDatabase();
        eventoClick();
        eventoDatabase();
        apagarMensagem();
        tipToDeleteMessage();
    }

    private void tipToDeleteMessage() {
        listView_Mensagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alert("Mantenha a mensagem pressionada para apagar! ");
            }
        });
    }

    private void apagarMensagem() {
        listView_Mensagens.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mensagemSelecionada = (Mensagem)parent.getItemAtPosition(position);
                Mensagem m = new Mensagem();
                m.setId_mensagem(mensagemSelecionada.getId_mensagem());
                reference.child("Mensagem").child(m.getId_mensagem()).removeValue();
                arrayAdapterMensagens.clear();

                alert("Mensagem apagada!");

                return true;
            }
        });

    }

    private void alert(String s) {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
    }

    private void eventoDatabase() {
        Query query, query1;

        query = reference.child("Mensagem").orderByChild("timeStamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaMensagens.clear();
                for (DataSnapshot dtSnapshot:dataSnapshot.getChildren()){
                    Mensagem m = dtSnapshot.getValue(Mensagem.class);
                    if (user.getUid().equals(m.getIdUser()) /*|| user.getUid().equals("uZAnSSHu9wRocrLEEy7pDhbPup22")*/){
                        listaMensagens.add(m);
                    }
                    arrayAdapterMensagens = new ArrayAdapter<Mensagem>(MensagemActivity.this,
                            android.R.layout.simple_list_item_1, listaMensagens);
                    arrayAdapterResp = new ArrayAdapter<>(MensagemActivity.this,
                            android.R.layout.simple_list_item_1, listaResp);
                    listView_Mensagens.setAdapter(arrayAdapterMensagens);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alert(databaseError.getMessage());
            }
        });

        query1 = reference.child("RespostaAdmin").orderByChild("timesStamp");

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaResp.clear();
                for (DataSnapshot dtSnapshot:dataSnapshot.getChildren()){

                    RespostaAdmin r =dtSnapshot.getValue(RespostaAdmin.class);

                    if (!user.getUid().equals("uZAnSSHu9wRocrLEEy7pDhbPup22") ){
                        listaResp.add(r);
                    }
                    arrayAdapterResp = new ArrayAdapter<>(MensagemActivity.this,
                            android.R.layout.simple_list_item_1, listaResp);
                    listView_Resp.setAdapter(arrayAdapterResp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void eventoClick() {

        imgSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensagem = editMensagem.getText().toString().trim();

                if (mensagem.isEmpty()){
                    Toast.makeText(getApplicationContext(),"O campo de mensagem deve ser preenchido!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Mensagem m = new Mensagem();
                    m.setId_mensagem(UUID.randomUUID().toString());
                    m.setTexto_mensagem(editMensagem.getText().toString().trim());
                    m.setIdUser(auth.getCurrentUser().getUid());
                    m.setEmailUser(auth.getCurrentUser().getEmail());
                    m.setIdAdm("uZAnSSHu9wRocrLEEy7pDhbPup22");
                    m.setTimeStamp(System.currentTimeMillis());
                    reference.child("Mensagem").child(m.getId_mensagem()).setValue(m);
                    editMensagem.setText("");
                }
            }
        });

    }

    private void inicilizarDatabase() {
        FirebaseApp.initializeApp(MensagemActivity.this);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    private void inicializarComponentes() {
        imgSendMessage = findViewById(R.id.imgSendMessageID);
        editMensagem = findViewById(R.id.edit_mensagemUID);
        listView_Mensagens = findViewById(R.id.listView_mensagensID);
        listView_Resp = findViewById(R.id.listView_RespID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }
}
