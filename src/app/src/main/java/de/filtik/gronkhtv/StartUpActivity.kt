package de.filtik.gronkhtv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class StartUpActivity : AppCompatActivity() {

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)

        startUp()
    }

    fun startUp() {
        handler = Handler()
        handler.postDelayed({
            val inte = Intent(this, MainActivity::class.java)
            startActivity(inte)
        }, 2000)
    }
}