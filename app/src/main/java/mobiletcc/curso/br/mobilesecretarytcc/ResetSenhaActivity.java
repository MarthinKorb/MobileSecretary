package mobiletcc.curso.br.mobilesecretarytcc;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetSenhaActivity extends AppCompatActivity {

    private EditText editResetEmail;
    private Button btnResetSenha;

    private FirebaseAuth auth;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_senha);

        inicializaComponentes();
        eventoClick();
    }

    private void eventoClick() {
        btnResetSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editResetEmail.getText().toString().trim();

                if (email.isEmpty()){
                    alert("O campo está vazio!");
                }else if(!email.contains("@") || !email.contains(".")){
                    alert("email inválido");
                }else{
                    carregando();
                    resetSenha(email);
                }
            }
        });
    }

    private void carregando() {
        dialog = new ProgressDialog(ResetSenhaActivity.this);
        dialog.setTitle("Enviando Mensagem...");
        dialog.setMessage("\n\nAguarde um instante...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    private void resetSenha(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(ResetSenhaActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            alert("Você receberá um email para redefinir sua senha!\nConfira sua caixa de mensagens de email!");
                            finish();
                            startActivity( new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alert(e.getMessage());
                dialog.dismiss();
                finish();
            }
        });
    }

    private void alert(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private void inicializaComponentes() {
        editResetEmail = findViewById(R.id.editEResetmailId);
        btnResetSenha = findViewById(R.id.btnResetSenhaId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }
}
