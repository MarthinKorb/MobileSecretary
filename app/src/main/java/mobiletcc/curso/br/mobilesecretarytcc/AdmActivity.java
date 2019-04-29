package mobiletcc.curso.br.mobilesecretarytcc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdmActivity extends AppCompatActivity {

    private Button btn_GerHor, btnVerCons, btnMensAdmin, btnHistAgendAdm, btnUsuarios;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm);

        inicializarComponentes();
        eventoClick();
       // verificaAdministrador();

    }

    private void verificaAdministrador() {
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user == null){
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        });
    }

    private void eventoClick() {
        btn_GerHor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddConsultaActivity.class));
            }
        });

        btnVerCons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VerConsAdminActivity.class));
            }
        });

        btnMensAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), MensagemActivity.class));
            }
        });

        btnHistAgendAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HistoricoConsultasActivity.class));
            }
        });

        btnUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListaUsersActivity.class));
            }
        });

    }

    private void inicializarComponentes() {
        btn_GerHor = findViewById(R.id.btn_GerHorariosId);
        btnVerCons = findViewById(R.id.btn_VerConsultasId);
        btnMensAdmin = findViewById(R.id.btn_MensagemAdmId);
        btnHistAgendAdm = findViewById(R.id.btnHistAgendAdmID);
        btnUsuarios = findViewById(R.id.btn_UsuariosID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout){
            Conexao.logOut();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }
}
