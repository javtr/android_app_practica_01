package com.example.counter_v02.Counter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.counter_v02.R

class Main : AppCompatActivity() {

    private lateinit var addClick: CardView
    private lateinit var addClickButton: ImageView
    private lateinit var counter0: TextView
    private lateinit var counter1: TextView
    private lateinit var counter2: TextView
    private lateinit var counter3: TextView
    private lateinit var counter4: TextView

    private var numeroInicial: Int = 0
    private var numeroFinal: Int = 0

    private var numeroInicial5dig: String = ""
    private var numeroFinal5dig: String = ""

    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponent()
        initListener()


    }

    private fun initComponent() {
        addClick = findViewById(R.id.addClick)
        addClickButton = findViewById(R.id.addClick_button)
        counter0 = findViewById(R.id.counter_0)
        counter1 = findViewById(R.id.counter_1)
        counter2 = findViewById(R.id.counter_2)
        counter3 = findViewById(R.id.counter_3)
        counter4 = findViewById(R.id.counter_4)

        mediaPlayer = MediaPlayer.create(this, R.raw.click)


    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        //boton add
        addClick.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    addButtonPressed()
                }

                MotionEvent.ACTION_UP -> {
                    addButtonLeaved()
                }
            }
            true
        }
    }

    private fun addButtonPressed() {
        addClickButton.setBackgroundResource(R.drawable.big_pressed)
    }

    private fun addButtonLeaved() {
        addClickButton.setBackgroundResource(R.drawable.big)
        updateClickScreen()
        vibrar()
        sonido()
    }


    private fun updateClickScreen() {
        // calcular la suma
        val numero1 = counter0.text.toString().toInt()
        val numero2 = counter1.text.toString().toInt()
        val numero3 = counter2.text.toString().toInt()
        val numero4 = counter3.text.toString().toInt()
        val numero5 = counter4.text.toString().toInt()

        // Combina los cinco números en un solo número de cinco dígitos
        numeroInicial = numero1 * 10000 + numero2 * 1000 + numero3 * 100 + numero4 * 10 + numero5
        numeroFinal = numeroInicial + 1;

        if(numeroInicial==99999){
            numeroFinal = 0
        }

        //convertir numero a 5 digitos
        numeroInicial5dig = numeroInicial.toString().padStart(5, '0')
        numeroFinal5dig = numeroFinal.toString().padStart(5, '0')

        Log.d("Salida", "var: $numeroInicial5dig")
        Log.d("Salida", "var: $numeroFinal5dig")

        checkDigits()

    }


    private fun checkDigits(){
        for (i in numeroInicial5dig.indices) {
            val caracter1 = numeroInicial5dig[i]
            val digito1 = caracter1.toString().toInt()

            val caracter2 = numeroFinal5dig[i]
            val digito2 = caracter2.toString().toInt()

            if (digito1!=digito2){
                animateNumber(digito2,i)

            }
        }
    }


    private fun animateNumber(EndDigit:Int,Index:Int) {

        val valorFinalDp = -70f
        val valorFinalPx = dpToPx(valorFinalDp)

        val valorInit2Dp = 70f
        val valorInit2Px = dpToPx(valorInit2Dp)

        val duracionAnimacion = 10L

        lateinit var counterIt: TextView

        when (Index) {
            0 -> counterIt = counter0
            1 -> counterIt = counter1
            2 -> counterIt = counter2
            3 -> counterIt = counter3
            4 -> counterIt = counter4
            else -> println("Otro día")
        }

        val animator = ValueAnimator.ofFloat(0f, valorFinalPx)
        animator.duration = duracionAnimacion

        animator.addUpdateListener { animation ->
            val valorAnimado = animation.animatedValue as Float
            counterIt.translationY = valorAnimado
        }

        // Listener para la primera animación
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                //counterIt.setText(EndDigit.toString())
                //startAnimateNumber(valorInit2Px, 0f, duracionAnimacion,counterIt)
            }
        })

        counterIt.setText(EndDigit.toString())
        animator.start()
    }


    private fun startAnimateNumber(startPosition: Float, endPosition: Float, duration: Long,view: TextView) {

        val animator = ValueAnimator.ofFloat(startPosition, endPosition)
        animator.duration = duration

        animator.addUpdateListener { animation ->
            val valorAnimado = animation.animatedValue as Float
            view.translationY = valorAnimado
        }

        animator.start()
    }

    fun dpToPx(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }

    private fun vibrar() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Verificar si la vibración está soportada en el dispositivo
        if (vibrator.hasVibrator()) {
            // Crear un efecto de vibración
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(50)
            }
        }    }

    private fun sonido(){
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }





}