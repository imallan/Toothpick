package com.imallan.toothpickkotlin

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.imallan.toothpick.OnClick
import com.imallan.toothpick.Toothpick
import com.imallan.toothpick.bind

class MainActivity : AppCompatActivity() {

    val mButton: Button by bind(R.id.button_press_me)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toothpick.bind(this)
    }

    @OnClick(R.id.button_press_me) fun showToast(view: View) {
        toastShort("Pressed ${view.id}")
    }
}

fun Context.toastShort(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
