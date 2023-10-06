package com.example.myintentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MoveWithBundleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_with_bundle)

        val receivedBundle = intent.getBundleExtra("myBundle")

        if (receivedBundle != null) {
            val value1 = receivedBundle.getString("key1")
            val value2 = receivedBundle.getInt("key2")
            val value3 = receivedBundle.getBoolean("key3")


            val textView = findViewById<TextView>(R.id.textView)
            textView.text = "Key1: $value1\nKey2: $value2\nKey3: $value3"
        }
    }
}