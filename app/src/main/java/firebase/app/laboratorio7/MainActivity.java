package firebase.app.laboratorio7;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
        TextView gx, gy, gz, mx, my, mz, orientacion;
        Sensor giroscopio, mangnetometro;
        SensorManager sm;
        float prox=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gx = (TextView) findViewById(R.id.Gx);
        gy = (TextView) findViewById(R.id.Gy);
        gz = (TextView) findViewById(R.id.Gz);

        mx = (TextView) findViewById(R.id.Mx);
        my = (TextView) findViewById(R.id.My);
        mz = (TextView) findViewById(R.id.Mz);

        orientacion = (TextView) findViewById(R.id.orientacion);


        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mangnetometro = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        giroscopio = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener( this,mangnetometro,SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener( this,giroscopio,SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener( this,sm.getDefaultSensor(Sensor.TYPE_PROXIMITY),SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener( this,sm.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event){

        switch (event.sensor.getType()){
            case Sensor.TYPE_MAGNETIC_FIELD:
                mx.setText(String.valueOf(event.values[0]));
                my.setText(String.valueOf(event.values[1]));
                mz.setText(String.valueOf(event.values[2]));


                break;
            case Sensor.TYPE_ACCELEROMETER:
                gx.setText(String.valueOf(event.values[0]));
                gy.setText(String.valueOf(event.values[1]));
                gz.setText(String.valueOf(event.values[2]));
                if(event.values[0]<-9){
                    orientacion.setText("Orientación: Horizontal a la derecha ");
                }else if(event.values[0]>9){
                    orientacion.setText("Orientación: Horizontal a la izquierda ");
                }else if(event.values[1]<-9){
                    orientacion.setText("Orientación: Vertical invertido ");
                }else if(event.values[1]>9){
                    orientacion.setText("Orientación: Vertical derecho  ");
                }else if(event.values[2]<-9){
                    orientacion.setText("Orientación: Mirando hacia abajo  ");
                }else if(event.values[2]>9){
                    orientacion.setText("Orientación: Mirando hacia arriba");
                }
                break;
            case Sensor.TYPE_PROXIMITY:
                prox = event.values[0];
                break;
            case Sensor.TYPE_LIGHT:
                if(event.values[0] <15 && prox == 0){
                    Log.e("En el bolsillo","bolsillo");
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
