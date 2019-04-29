package mobiletcc.curso.br.mobilesecretarytcc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarioActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button btn_Confirmar;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        display(date);

        inicializaComponentes();

       calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
           @Override
           public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
               String date = dayOfMonth + "/" + "0" + (month+1) + "/" + year;
               Intent i = new Intent(getApplicationContext(), AddConsultaActivity.class);
               i.putExtra("editDia", date);
               startActivity(i);
           }
       });

       eventoClick();
    }

    private void eventoClick() {
        btn_Confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(getApplicationContext(), AddConsultaActivity.class);
               // i.putExtra("editDia", date);
               // startActivity(i);
                //startActivity(new Intent(getApplicationContext(), AddConsultaActivity.class));
            }
        });
    }

    private void display(String text) {
       // Toast.makeText(CalendarioActivity.this, text, Toast.LENGTH_SHORT).show();
        //Bundle bundle = new Bundle();
        //bundle.putString("editDia", text);

    }

    private void inicializaComponentes() {
        calendarView = findViewById(R.id.calendarViewId);
        btn_Confirmar = findViewById(R.id.btn_ConfirmaId);
    }
}
