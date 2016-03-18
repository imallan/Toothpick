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
        val simpleName = activity.javaClass.simpleName
        var bindMethod = mMethodMap[simpleName]
        if (mMethodMap[simpleName] == null) {
            val clazz = Class.forName(activity.packageName + "." + simpleName + "\$\$ViewInjector")
            bindMethod = clazz.getDeclaredMethod("bindActivity", Activity::class.java)
            mMethodMap.put(simpleName, bindMethod)
        }
        bindMethod!!.invoke(null, activity)
    }

    fun bind(obj: Any, view: View) {
        val simpleName = obj.javaClass.simpleName
        var bindMethod = mMethodMap[simpleName]
        if (mMethodMapForView[simpleName] == null) {
            val clazz = Class.forName(obj.javaClass.canonicalName + "\$\$ViewInjector")
            bindMethod = clazz.getDeclaredMethod("bindView", Any::class.java, View::class.java)
            mMethodMapForView.put(simpleName, bindMethod)
        }
        bindMethod!!.invoke(null, obj, view)
    }

    @Deprecated("No need to use reflections anymore")
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
