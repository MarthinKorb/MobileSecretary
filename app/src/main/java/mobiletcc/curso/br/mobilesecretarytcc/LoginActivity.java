package mobiletcc.curso.br.mobilesecretarytcc;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.logging.Handler;

import mobiletcc.curso.br.mobilesecretarytcc.Modelo.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmailLogin, editSenhaLogin;
    private Button btnLogar, btnNovo;
    private TextView txtResetSenha;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        inicializaComponentes();
        inicializarDatabase();
        eventoClicks();
    }

    private void inicializarDatabase() {
        FirebaseApp.initializeApp(LoginActivity.this);
        reference = FirebaseDatabase.getInstance().getReference();
    }


    private void eventoClicks() {

        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmailLogin.getText().toString().trim();
                String senha = editSenhaLogin.getText().toString().trim();

                if (email.isEmpty() || senha.isEmpty()){
                    alert("Preencha todos os campos!");
                }else{
                    carregando();
                    login(email,senha);
                }
            }
        });

        txtResetSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetSenhaActivity.class ));
            }
        });
    }

    private void login(final String email, final String senha) {

           auth.signInWithEmailAndPassword(email,senha)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser user = auth.getCurrentUser();

                            if (email.equals("admin@admin.com")) {
                                if (senha.equals("123456")) {
                                    dialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), AdmActivity.class));
                                    alert("Bem Vindo Administrador!");
                                    finish();
                                } else {
                                    dialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    alert("Bem Vindo " + user.getEmail());
                                    finish();
                                }
                            } else {
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                alert("Bem Vindo " + user.getEmail());
                                finish();
                            }

                        }else{
                            dialog.dismiss();
                            alert("Erro ao fazer o login! Verifique seu email e/ou senha!");
                            editEmailLogin.setText("");
                            editSenhaLogin.setText("");
                        }
                    }
                });
    }

    private void alert(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    private void inicializaComponentes() {
        editEmailLogin = findViewById(R.id.edit_Email);
        editSenhaLogin = findViewById(R.id.edit_Senha);
        btnLogar = findViewById(R.id.btn_Logar);
        btnNovo = findViewById(R.id.btn_Novo);
        txtResetSenha = findViewById(R.id.txt_Reset_SenhaId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }

    private void carregando(){
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle("Fazendo Login");
        dialog.setMessage("\nAguarde um instante...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }
}
