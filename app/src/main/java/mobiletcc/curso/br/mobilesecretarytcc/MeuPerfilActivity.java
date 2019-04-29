package mobiletcc.curso.br.mobilesecretarytcc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MeuPerfilActivity extends AppCompatActivity {

    private TextView textEmail, txtVerHistUser;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil);

        inicializaComponentes();
        eventoClick();
    }

    private void eventoClick() {

        txtVerHistUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HistoricoConsultasActivity.class));
            }
        });
    }

    private void inicializaComponentes() {
        textEmail = findViewById(R.id.text_EmailId);

        txtVerHistUser = findViewById(R.id.txtView_VerHistUserID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        //Verifica se há Informação de Usuário
        verificaUser();
    }

    private void verificaUser() {
        if (user == null){
            finish();
        }else{
            textEmail.setText("Email: "+ user.getEmail());

        }
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
}
