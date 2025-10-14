package com.example.lab5ex101500671
//-- Name: Lucas Tavares Criscuolo Student ID: 101500671



import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.hardware.Sensor
import android.util.Log
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import kotlin.math.sqrt


class MainActivity : AppCompatActivity(),SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorTV: TextView
    private lateinit var shakeTV: TextView
    private lateinit var lightTV: TextView

    private var lightSensor: Sensor? = null
    private var accSensor : Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorTV = findViewById<TextView>(R.id.sensor_list)
        shakeTV = findViewById<TextView>(R.id.shake)
        shakeTV.setOnClickListener(){v->(v as TextView).setText("")}
        lightTV = findViewById<TextView>(R.id.light)

        getAllSensors()
        setupLightSensor()
        setupAccSensor()

    }

    private fun setupLightSensor() {
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor == null) {
            Log.e("SENSOR", "No Light Sensor!")
        } else {
            Log.d("SENSOR", "Light Sensor Present")
        }
    }

    private fun setupAccSensor() {
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accSensor == null) {
            Log.e("SENSOR", "No Acc Sensor!")
        } else {
            Log.d("SENSOR", "Acc Sensor Present")
        }
    }


    private fun getAllSensors() {
        val deviceSensors:List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        sensorTV.setText("")
        for (s in deviceSensors){
            sensorTV.append(s.toString()+ "\n\n")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("SENSOR", "Accuracy changed for: "+sensor?.name+"to "+accuracy)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!=null) {
            val v = event.values
            if(event.sensor?.type == Sensor.TYPE_LIGHT) {
                Log.d("SENSOR", "Sensor Changed: " + v[0])
                lightTV.setText(brightness(v[0]))

            }else if(event.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                Log.d("SENSOR", "Sensor reading: [" + v[0]+","+v[1]+","+v[2]+"]" )
                if (isShaken(v)){
                    shakeTV.setText("SHAKEN!!!!")
                }
            }
        }
    }

    private var lastAcc = 0f
    private var currentAcc = 0f
    private var acceleration = 0f
    private val SHAKEN = 10
    private fun isShaken(vals:FloatArray):Boolean{
        val x = vals[0]
        val y = vals[1]
        val z = vals[2]

        lastAcc = currentAcc
        currentAcc = sqrt((x*x+y*y+z*z).toDouble()).toFloat()
        val delta = Math.abs(lastAcc-currentAcc)
        acceleration = acceleration*0.9f + delta
        return acceleration>SHAKEN
    }

    private fun brightness(v:Float):String{
        return when(v.toInt()) {
            0 -> "Pitch black"
            in 1..10 -> "Dark"
            in 11..50 -> "Grey"
            in 51..5000 -> "Normal"
            in 5001..25000 -> "Incredibly bright"
            else -> "This light will blind you"
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL)
        Log.d("SENSOR", "Listener Registered")
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        Log.d("SENSOR", "Listener Unregistered")
    }
}