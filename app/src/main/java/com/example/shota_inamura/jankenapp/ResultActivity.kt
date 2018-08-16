package com.example.shota_inamura.jankenapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val gu = 0
    val choki = 1
    val pa = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val id = intent.getIntExtra("YOUR_HAND", 0)

        val yourHand: Int
        when(id) {
            R.id.gu_btn -> {
                your_hand_txt.text = "グー"
                yourHand = gu
            }
            R.id.choki_btn -> {
                your_hand_txt.text = "チョキ"
                yourHand = choki
            }
            R.id.pa_btn -> {
                your_hand_txt.text = "パー"
                yourHand = pa
            }
            else -> yourHand = gu
        }

        val comHand = getHand()
        when(comHand) {
            gu -> com_hand_txt.text = "グー"
            choki -> com_hand_txt.text = "チョキ"
            pa -> com_hand_txt.text = "パー"
        }

        val result = (comHand - yourHand + 3) % 3
        when(result) {
            0 -> result_txt.text = "DRAW"
            1 -> result_txt.text = "YOU WIN"
            2 -> result_txt.text = "YOU LOSE"
        }

        printScore(result)

        back_btn.setOnClickListener {
            finish()
        }

        saveData(yourHand, comHand, result)
    }

    private fun saveData(yourHand: Int, comHand: Int, result: Int) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT", 0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        val yourScore = pref.getInt("YOUR_SCORE", 0)
        val lastComHand = pref.getInt("LAST_COM_HAND", 0)
        val lastResult = pref.getInt("RESULT", -1)

        val editor = pref.edit()
        editor.putInt("GAME_COUNT", gameCount + 1)
                .putInt("WINNING_STREAK_COUNT",
                        if (lastResult == 2 && result == 2) winningStreakCount + 1
                        else 0)
                .putInt("YOUR_SCORE",
                        if (result == 1) yourScore + 1
                        else yourScore)
                .putInt("LAST_YOUR_HAND", yourHand)
                .putInt("LAST_COM_HAND", comHand)
                .putInt("BEFORE_LAST_COM_HAND", lastComHand)
                .putInt("RESULT", result)
                .apply()
    }

    private fun getHand(): Int {
        var hand = (Math.random() * 3).toInt()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT", 0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        val lastYourHand = pref.getInt("LAST_YOUR_HAND", 0)
        val lastComHand = pref.getInt("LAST_COM_HAND", 0)
        val beforeLastComHand = pref.getInt("BEFORE_LAST_COM_HAND", 0)
        val result = pref.getInt("RESULT", -1)

        if (gameCount == 1) {
            if (result == 2) {
                while (lastComHand == hand) {
                    hand = (Math.random() * 3).toInt()
                }
            } else if (result == 1) {
                hand = (lastYourHand - 1 + 3) % 3
            }
        } else if (winningStreakCount > 0) {
            if (beforeLastComHand == lastComHand) {
                while (lastComHand == hand) {
                    hand = (Math.random() * 3).toInt()
                }
            }
        }
        return hand
    }

    private fun printScore(result: Int) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        var gameCount = pref.getInt("GAME_COUNT", 0)
        var yourScore = pref.getInt("YOUR_SCORE", 0)

        gameCount ++
        if (result == 1) {
            yourScore ++
        }

        your_score_txt.text = "あなたのスコア: $yourScore / $gameCount"
    }
}
