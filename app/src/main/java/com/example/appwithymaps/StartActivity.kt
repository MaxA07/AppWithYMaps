package com.example.appwithymaps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val button = findViewById<Button>(R.id.show_on_map)
        button.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        val title = findViewById<TextView>(R.id.text_on_map)
        val data = intent.extras?.getString(getString(R.string.key))
        findViewById<TextView>(R.id.point_on_map).text = data
        if (data != null) {
            title.visibility = View.GONE
        }
    }
}