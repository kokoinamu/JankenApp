package com.example.shota_inamura.jankenapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.clear().apply()

        gu_btn.setOnClickListener{ onJankenBtnTapped(it) }
        choki_btn.setOnClickListener { onJankenBtnTapped(it) }
        pa_btn.setOnClickListener { onJankenBtnTapped(it) }
    }

    fun onJankenBtnTapped(view: View?) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("YOUR_HAND", view?.id)
        startActivity(intent)
    }
}
