package com.hkm.onplate.core.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeUntilCallback(observer: Observer<T>, callback: LiveData<Boolean>?) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            callback?.observeOnce { stop ->
                if (stop != null && stop) removeObserver(this)
            }
        }
    })
}

fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}