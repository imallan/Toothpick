@file:Suppress("unused")

package com.imallan.toothpick

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.view.View
import java.lang.reflect.Method
import java.util.*

object Toothpick {

    private val mMethodMap = LinkedHashMap<String, Method>();
    private val mMethodMapForView = LinkedHashMap<String, Method>();

    fun bind(activity: Activity) {
        val injectorName = "${activity.javaClass.canonicalName}\$\$ViewInjector"
        var bindMethod = mMethodMap[injectorName]
        if (bindMethod == null) {
            val clazz = Class.forName(injectorName)
            bindMethod = clazz.getDeclaredMethod("bindActivity", Any::class.java, Activity::class.java)
            mMethodMap.put(injectorName, bindMethod)
        }
        bindMethod!!.invoke(null, activity, activity)
    }

    fun bind(obj: Any, view: View) {
        val injectorName = "${obj.javaClass.name}\$\$ViewInjector"
        var bindMethod = mMethodMapForView[injectorName]
        if (bindMethod == null) {
            val clazz = Class.forName(injectorName)
            bindMethod = clazz.getDeclaredMethod("bindView", Any::class.java, View::class.java)
            mMethodMapForView.put(injectorName, bindMethod)
        }
        bindMethod!!.invoke(null, obj, view)
    }

}

fun <V : View> Activity.bind(@IdRes id: Int): Lazy<V> {
    return lazy {
        @Suppress("UNCHECKED_CAST")
        (findViewById(id) as V)
    }
}

fun <V : View> Fragment.bind(@IdRes id: Int): Lazy<V> {
    return lazy {
        @Suppress("UNCHECKED_CAST")
        (view?.findViewById(id) as V)
    }
}

fun <V : View> bind(@IdRes id: Int, view: View?): Lazy<V> {
    return lazy {
        @Suppress("UNCHECKED_CAST")
        (view?.findViewById(id) as V)
    }
}
