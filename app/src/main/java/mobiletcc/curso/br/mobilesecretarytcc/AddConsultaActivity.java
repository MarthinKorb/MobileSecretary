package mobiletcc.curso.br.mobilesecretarytcc;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import mobiletcc.curso.br.mobilesecretarytcc.Modelo.Consulta;

public class AddConsultaActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private EditText edtDia, edtHora;
    private ListView listView_dados;

    //FirebaseDatabase database;
    private DatabaseReference reference;

    private List<Consulta> listaConsulta = new ArrayList<Consulta>();
    private ArrayAdapter<Consulta> arrayAdapterConsulta;

    Consulta consultaSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consulta);

        edtDia = findViewById(R.id.editDiaId);
        edtHora = findViewById(R.id.editHoraId);
        listView_dados = findViewById(R.id.listView_dadosId);

        inicializarDatabase();
        eventoDatabase();

        edtDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDia.setText("");
                //startActivity(new Intent(getApplicationContext(), CalendarioActivity.class ));
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"DatePicker");
            }
        });

        edtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtHora.setText("");
                //startActivity(new Intent(getApplicationContext(),ClockActivity.class));
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"Time Picker");
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String data = bundle.getString("editDia");
            edtDia.setText(data);
        }

        Bundle b = getIntent().getExtras();
        if (b != null){
            String hora = b.getString("editHora"+" : "+"editMinuto");
            edtHora.setText(hora);
        }


        listView_dados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                consultaSelecionada =(Consulta)parent.getItemAtPosition(position);
                edtDia.setText(consultaSelecionada.getDia());
                edtHora.setText(consultaSelecionada.getHora());
            }
        });

    }//Fim onCreate

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        edtHora.setText(hourOfDay + ":" + minute);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        //String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        //edtDia.setText(currentDate);
        edtDia.setText(dayOfMonth + "/" + (month+1)+ "/" + year);

    }

    private void eventoDatabase() {

        Query query;

        query = reference.child("Consulta").orderByChild("timeStamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaConsulta.clear();
                for (DataSnapshot dtSnapshot:dataSnapshot.getChildren()){

                    Consulta c = dtSnapshot.getValue(Consulta.class);
                    if(c.getIdUser() == null){
                        listaConsulta.add(c);
                    }
                }
                arrayAdapterConsulta = new ArrayAdapter<Consulta>(AddConsultaActivity.this,
                        android.R.layout.simple_list_item_1, listaConsulta);
                listView_dados.setAdapter(arrayAdapterConsulta);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void alert(String s) {
        Toast.makeText(getApplicationContext(), s , Toast.LENGTH_SHORT).show();
    }

    private void inicializarDatabase() {
        FirebaseApp.initializeApp(AddConsultaActivity.this);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_novo){
            addConsulta();
        }else if (id == R.id.menu_atualizar){
            atualizarConsulta();
        }else if (id == R.id.menu_deletar){
            deletarConsulta();
        }
        return true;
    }

    private void addConsulta() {
            String dia = edtDia.getText().toString().trim();
            String hora = edtHora.getText().toString().trim();

            if (dia.isEmpty() || hora.isEmpty()){
                alert("Os campos de dia e hora devem ser preenchidos!");
            }else{
                Consulta c = new Consulta();
                c.setIdConsulta(UUID.randomUUID().toString());
                c.setDia(edtDia.getText().toString());
                c.setHora(edtHora.getText().toString());
                c.setTimeStamp(System.currentTimeMillis());
                reference.child("Consulta").child(c.getIdConsulta()).setValue(c);
                limparCampos();
                alert("Consulta disponibilizada com sucesso!");
            }
    }

    private void deletarConsulta() {
            Consulta c = new Consulta();
            c.setIdConsulta(consultaSelecionada.getIdConsulta());
            reference.child("Consulta").child(c.getIdConsulta()).removeValue();
            alert("Consulta excluída com sucesso!");
            limparCampos();

    }

    private void atualizarConsulta() {
        String dia = edtDia.getText().toString().trim();
        String hora = edtHora.getText().toString().trim();

        if (dia.isEmpty() || hora.isEmpty()){
            alert("Os campos de dia e hora devem ser preenchidos!");
        }else{
            Consulta con = new Consulta();
            con.setIdConsulta(consultaSelecionada.getIdConsulta());
            con.setDia(edtDia.getText().toString().trim());
            con.setHora(edtHora.getText().toString().trim());
            con.setTimeStamp(consultaSelecionada.getTimeStamp());
            reference.child("Consulta").child(con.getIdConsulta()).setValue(con);
            alert("Consulta atualizada com sucesso!");
            limparCampos();
        }

    }

    private void limparCampos() {
        edtDia.setText("");
        edtHora.setText("");
        edtDia.requestFocus();
    }
}
