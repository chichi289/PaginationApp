@file:Suppress("unused")

package com.chichi289.paginationapp.extentions

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.chichi289.paginationapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Int?.nullSafe(defaultValue: Int = 0): Int {
    return this ?: defaultValue
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.visibleIf(isShown: Boolean) {
    if (isShown) {
        visible()
    } else {
        gone()
    }
}

fun ImageView.loadImage(
    value: String? = null,
    drawable: Int? = null,
    bitmap: Bitmap? = null,
    uri: Uri? = null,
    isCircle: Boolean = false,
    showLoading: Boolean = true,
    placeholderRes: Int = R.drawable.ic_no_image,
    roundRadius: Float? = null
) {
    val toBeLoad: Any = when {
        !value.isNullOrEmpty() -> value
        drawable != null -> drawable
        bitmap != null -> bitmap
        uri != null -> uri
        else -> ""
    }
    load(
        data = toBeLoad,
        builder = {
            crossfade(true)
            crossfade(500)
            if (showLoading) {
                val circularProgressDrawable = CircularProgressDrawable(this@loadImage.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()
                placeholder(circularProgressDrawable)
            } else {
                placeholder(placeholderRes)
            }
            if (isCircle) {
                transformations(CircleCropTransformation())
            }
            roundRadius?.let { radius ->
                transformations(RoundedCornersTransformation(radius))
            }
            error(R.drawable.ic_no_image)
        })
}

inline fun LifecycleOwner.whenResumed(
    crossinline block: suspend CoroutineScope.() -> Unit
) = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.RESUMED) {
        block()
    }
}

inline fun LifecycleOwner.whenCreated(
    crossinline block: suspend CoroutineScope.() -> Unit
) = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.CREATED) {
        block()
    }
}


inline fun LifecycleOwner.whenStarted(
    crossinline block: suspend CoroutineScope.() -> Unit
) = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        block()
    }
}
