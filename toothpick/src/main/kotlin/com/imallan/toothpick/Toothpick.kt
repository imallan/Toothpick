@file:Suppress("unused")

package com.imallan.toothpick

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.view.View

object Toothpick {

    fun bind(activity: Activity) {
        bind(activity) { activity.findViewById(it) }
    }

    fun bind(obj: Any, view: View) {
        bind(obj) { view.findViewById(it) }
    }

    private fun bind(obj: Any, func: (id: Int) -> View?) {
        val clazz = obj.javaClass
        for (method in clazz.declaredMethods) {
            if (method.isAnnotationPresent(OnClick::class.java)) {
                val annotation = method.getAnnotation(OnClick::class.java)
                val values = annotation.value
                for (id: Int in values) {
                    val view = func(id)
                    view?.setOnClickListener({
                        try {
                            method.invoke(obj, view)
                        } catch (e: IllegalArgumentException) {
                            method.invoke(obj)
                        }
                    })
                }
            }
        }
    }

}

fun <V : View> Activity.bind(@IdRes id: Int): Lazy<V> {
    return lazy {
        @Suppress("UNCHECKED_CAST")
        (findViewById(id)  as V)
    }
}

fun <V : View> Fragment.bind(@IdRes id: Int): Lazy<V> {
    return lazy {
        @Suppress("UNCHECKED_CAST")
        (view?.findViewById(id)  as V)
    }
}

fun <V : View> bind(@IdRes id: Int, view: View?): Lazy<V> {
    return lazy {
        @Suppress("UNCHECKED_CAST")
        (view?.findViewById(id)  as V)
    }
}
