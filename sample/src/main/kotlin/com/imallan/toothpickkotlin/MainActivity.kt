package com.imallan.toothpickkotlin

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.imallan.toothpick.OnClickView
import com.imallan.toothpick.Toothpick
import com.imallan.toothpick.bind

class MainActivity : AppCompatActivity() {

    val mButton: Button by bind(R.id.button_press_me)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start = SystemClock.currentThreadTimeMillis()
        for (i in 0..10000) {
            Toothpick.bind(this)
        }
        Log.d("TIMEUSED", (SystemClock.currentThreadTimeMillis() - start).toString())
    }

    @OnClickView(R.id.button_press_me) fun showToast(view: View) {
        toastShort("Pressed ${view.id}")
    }

    @OnClickView(R.id.button_press_me_2) fun showThost() {
        toastShort("Pressed")
    }
}

fun Context.toastShort(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
