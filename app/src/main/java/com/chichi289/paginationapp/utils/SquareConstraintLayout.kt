package com.chichi289.paginationapp.utils

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class SquareConstraintLayout(
    context: Context,
    attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}