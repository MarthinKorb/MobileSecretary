package mobiletcc.curso.br.mobilesecretarytcc;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ClockActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Calendar c;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        timePicker = findViewById(R.id.TimePickerId);
        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });

      //  int hour = timePicker.getHour();
      //  int minute = timePicker.getMinute();

       // Toast.makeText(getApplicationContext(),hour + minute,Toast.LENGTH_SHORT).show();

       // Intent i = new Intent(getApplicationContext(), AddConsultaActivity.class);
      //  i.putExtra("editHora", hour);
      //  i.putExtra("editMinuto", minute);
       // startActivity(i);


    }
}
