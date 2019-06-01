package mobiletcc.curso.br.mobilesecretarytcc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import mobiletcc.curso.br.mobilesecretarytcc.Modelo.MensagemUser;
import mobiletcc.curso.br.mobilesecretarytcc.Modelo.RespostaAdmin;

public class ListaMensagensUsersActivity extends AppCompatActivity {

    private ListView listViewMensUsers;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private List<MensagemUser> listaMensagemUser = new ArrayList<MensagemUser>();
    private ArrayAdapter<MensagemUser> arrayAdapterMensagemUser;

    private MensagemUser userSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mensagens_users);

        auth = FirebaseAuth.getInstance();

        listViewMensUsers = findViewById(R.id.listView_MensUsersID);

        inicializarDatabase();
        carregarLista();
        eventoClick();
    }

    private void eventoClick() {
        listViewMensUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSelecionado  = (MensagemUser) parent.getItemAtPosition(position);
               //startActivity(new Intent(getApplicationContext(), MensagemUser.class));
                Toast.makeText(getApplicationContext(), userSelecionado.getEmailUser(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarLista() {
        Query query;

        query = reference.child("MensagemUser").orderByChild("timeStamp");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaMensagemUser.clear();
                for (DataSnapshot dtSnapshot:dataSnapshot.getChildren()){
                    MensagemUser m = dtSnapshot.getValue(MensagemUser.class);
                    if (auth.getCurrentUser().getUid().equals("uZAnSSHu9wRocrLEEy7pDhbPup22")) {


                        listaMensagemUser.add(m);
                    }

                    arrayAdapterMensagemUser = new ArrayAdapter<MensagemUser>(ListaMensagensUsersActivity.this,
                            android.R.layout.simple_list_item_1, listaMensagemUser);
                    listViewMensUsers.setAdapter(arrayAdapterMensagemUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarDatabase() {
        FirebaseApp.initializeApp(ListaMensagensUsersActivity.this);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }
}
