package mobiletcc.curso.br.mobilesecretarytcc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import mobiletcc.curso.br.mobilesecretarytcc.Modelo.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText editEmail, editSenha, editNomeUser, editConfSenha;
    private Button btnRegistrar, btnFotoUser;
    private ImageView imageView_FotoUser;


    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private Uri mUriSelecionada;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializaComponentes();
        eventoClicks();
        inicializarDatabase();

    }

    private void inicializarDatabase() {
        FirebaseApp.initializeApp(CadastroActivity.this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void eventoClicks() {

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editNomeUser.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();
                String confSenha = editConfSenha.getText().toString().trim();

                if (nome == null || nome.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty()
                || confSenha==null || confSenha.isEmpty()){
                    alert("Todos os campos devem ser preenchidos!");
                }else {
                    if (senha.equals(confSenha)) {
                        carregando();
                        criarUser(email, senha);
                    }else{
                        alert("As senhas devem ser iguais!");
                        editSenha.setText("");
                        editConfSenha.setText("");
                        editSenha.requestFocus();
                    }
                }
            }
        });

        btnFotoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFotoUser();
            }
        });
    }

    private void carregando() {
        dialog = new ProgressDialog(CadastroActivity.this);
        dialog.setTitle("Cadastrando...");
        dialog.setMessage("\nAguarde um instante...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0){
             mUriSelecionada = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mUriSelecionada);
                imageView_FotoUser.setImageDrawable(new BitmapDrawable(bitmap));
                btnFotoUser.setAlpha(0);
            } catch (IOException e) {

            }
        }
    }

    private void selecionarFotoUser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void criarUser(final String email,String senha) {
        auth.createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            finish();
                            salvarUsuarioNoFirebase();
                            dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                                alert("Bem vindo "+ auth.getCurrentUser().getEmail());
                        }else{
                            dialog.dismiss();
                            alert(task.getException().toString());
                            editNomeUser.setText("");
                            editEmail.setText("");
                            editSenha.setText("");
                            editConfSenha.setText("");
                            editNomeUser.requestFocus();
                        }
                    }
                });
    }

     private void salvarUsuarioNoFirebase() {
        Usuario u = new Usuario();
        u.setUuid(auth.getUid());
        u.setEmail(editEmail.getText().toString().trim());
        u.setNomeUsuario(editNomeUser.getText().toString().trim());
        u.setTipo("u");
        databaseReference.child("Usuarios").child(u.getUuid()).setValue(u);

      /*  String fileName = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + fileName);
        ref.putFile(mUriSelecionada).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String uid = FirebaseAuth.getInstance().getUid();
                        String nomeUsuario = editNomeUser.getText().toString();
                        String urlFoto = uri.toString();
                        String tipo = "U";
                        Usuario usuario = new Usuario(uid, nomeUsuario, urlFoto, tipo);

                        FirebaseFirestore.getInstance().collection("Usuarios").add(usuario)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        alert("Dados salvos com Sucesso!");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                alert(e.getMessage());
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alert(e.getMessage());
            }
        });*/
    }


    private void alert(String msg){
        Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_SHORT).show();
    }

    private void inicializaComponentes() {
        imageView_FotoUser = findViewById(R.id.imageView_fotoUserId);
        editNomeUser = findViewById(R.id.edt_NomeUserId);
        editConfSenha = findViewById(R.id.editConfSenhaID);
        btnFotoUser = findViewById(R.id.btn_fotoUserId);
        editEmail = findViewById(R.id.editCadastroEmail);
        editSenha = findViewById(R.id.edit_CadastroSenha);
        btnRegistrar = findViewById(R.id.btn_CadastroRegistrar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }

}
