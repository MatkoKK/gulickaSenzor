package com.example.nitro.gulickasenzor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import javax.net.ssl.ManagerFactoryParameters;

/**
 * Created by Nitro on 27. 3. 2018.
 */

public class Platno extends View implements SensorEventListener {
    private SensorManager sManager; // manager sezorov
    private Sensor accelerometer;   // senzor akcelerometer
    private Point rozmery;          // rozmery plochy
    private Paint paint;
    private float xBall, yBall,xmax,ymax;     // poloha gulicky

    public Platno(Context context) {
        super(context); paint = new Paint();

        // ziskame pristup k menezerovi ...
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        // aby nam nastavil accelerometer ako senzor pre udaje z Accel.
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // rutina na zistenie rozmerov displeja
        WindowManager wm =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay(); // prečítame displej
        rozmery = new Point();
        display.getSize(rozmery); // nastavi rozmery do prem. typu Point
        // umiestnenie gulicky do stredu
        xmax=rozmery.x-20;
        ymax=rozmery.y-140;
        xBall = xmax/2;
        yBall = ymax/2;
        spustiSnimanie();

    }
    public void zastavSnimanie() { sManager.unregisterListener(this);}

    public void spustiSnimanie() {
        sManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void onSensorChanged(SensorEvent event) {
        // pri zmene senzora accelerometer – ine hodnoty nam ani nepridu
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            // precitame zrychlenie v smere x (moze byt kladne alebo zaporne)
            // a pripocitame ho k poslednej znamej hodnote nasho objektu
            xBall = xBall + event.values[0];
        yBall = yBall + event.values[1]; // detto

        // ak sme mimo aktivnej obrazovky, nepovolime odsun mimo
        if (xBall > xmax) xBall = xmax;
        if (xBall < 0) xBall = 0;
        if (yBall > ymax) yBall = ymax;
        if (yBall < 0) yBall = 0;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onDraw(Canvas canvas) {
        RectF oval = new RectF(xBall, yBall, xBall + 20, yBall +20); // defiunujeme ovál
        paint.setColor(Color.RED);
        canvas.drawOval(oval, paint);
        canvas.drawText(xBall + "," + yBall, 10, 10, paint); // vypíšeme súradnice
                invalidate(); // vyvoláme prekreslenie
    }



}






