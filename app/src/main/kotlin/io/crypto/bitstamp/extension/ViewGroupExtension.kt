package io.crypto.bitstamp.extension

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View = LayoutInflater.from(this.context).inflate(layoutId, this, false)
