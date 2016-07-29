package com.imallan.toothpickkotlin

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.imallan.toothpick.OnClick
import com.imallan.toothpick.Toothpick
import com.imallan.toothpick.bind

class MainActivity : AppCompatActivity() {

    val mButton: Button by bind(R.id.button_press_me)
    lateinit var mView: MyView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start = SystemClock.currentThreadTimeMillis()
        Toothpick.bind(this, window.decorView)
        Log.d("TIMEUSED", (SystemClock.currentThreadTimeMillis() - start).toString())

        mView = MyView(window.decorView)
    }

    @OnClick(R.id.button_press_me) fun showToast(view: View) {
        toastShort("Pressed ${view.id}")
    }

//    @OnClick(R.id.button_press_me_2) fun showToast2() {
//        toastShort("Pressed")
//    }

    inner class MyView {

        val mView: View

        constructor(view: View) {
            mView = view
            Toothpick.bind(this@MyView, view)
        }

        @OnClick(R.id.button_press_me_2)
        fun press() {
            toastShort("Pressed")
        }

    }
}

fun Context.toastShort(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
