package mobiletcc.curso.br.mobilesecretarytcc;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

import mobiletcc.curso.br.mobilesecretarytcc.Modelo.Usuario;

public class ListaUsersActivity extends AppCompatActivity {

    private ListView listView_Users;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private DatabaseReference reference;

    private List<Usuario> listaUsuarios = new ArrayList<Usuario>();
    private ArrayAdapter<Usuario>arrayAdapterUsuarios;
    private Usuario usuarioSelecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_users);

        inicializarComponentes();
        inicializarDatabase();
        eventoDatabase();
        eventoClick();

    }

    private void eventoClick() {
        listView_Users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usuarioSelecionado = (Usuario)parent.getItemAtPosition(position);
                tornarAdministrador();
            }
        });
    }

    private void tornarAdministrador() {
        Usuario u = new Usuario();
        if (usuarioSelecionado.getTipo().equals("u")){
            AlertDialog.Builder alerta = new AlertDialog.Builder(ListaUsersActivity.this);
            alerta.setTitle("Tornar Administrador");
            alerta.setIcon(R.mipmap.ic_aviso)
                    .setMessage("Você deseja tornar o usuário "+ usuarioSelecionado.getNomeUsuario() + " um administrador?")
                    .setCancelable(true)
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Usuario u = new Usuario();
                    u.setUuid(usuarioSelecionado.getUuid());
                    u.setNomeUsuario(usuarioSelecionado.getNomeUsuario());
                    u.setTipo("a");
                    reference.child("Usuarios").child(usuarioSelecionado.getUuid()).setValue(u);
                }
            });
            AlertDialog alertDialog = alerta.create();
            alertDialog.show();
        }else if (usuarioSelecionado.getTipo().equals("a")){
            AlertDialog.Builder alerta = new AlertDialog.Builder(ListaUsersActivity.this);
            alerta.setTitle("Tornar Usuário");
            alerta.setIcon(R.mipmap.ic_aviso)
                    .setMessage("Você deseja tornar o administrador "+ usuarioSelecionado.getNomeUsuario() + " um usuário comum?")
                    .setCancelable(true)
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Usuario u = new Usuario();
                    u.setUuid(usuarioSelecionado.getUuid());
                    u.setNomeUsuario(usuarioSelecionado.getNomeUsuario());
                    u.setTipo("u");
                    reference.child("Usuarios").child(usuarioSelecionado.getUuid()).setValue(u);
                }
            });
            AlertDialog alertDialog = alerta.create();
            alertDialog.show();
        }

    }

    private void eventoDatabase() {
        Query query = reference.child("Usuarios").orderByChild("nomeUsuario");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUsuarios.clear();

                for (DataSnapshot dtSnapshot:dataSnapshot.getChildren()){
                    Usuario u = dtSnapshot.getValue(Usuario.class);
                    listaUsuarios.add(u);
                }
                arrayAdapterUsuarios = new ArrayAdapter<Usuario>(ListaUsersActivity.this,
                        android.R.layout.simple_list_item_1,listaUsuarios);
                listView_Users.setAdapter(arrayAdapterUsuarios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inicializarDatabase() {
        FirebaseApp.initializeApp(ListaUsersActivity.this);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    private void inicializarComponentes() {
        listView_Users = findViewById(R.id.listView_UsersID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }
}
