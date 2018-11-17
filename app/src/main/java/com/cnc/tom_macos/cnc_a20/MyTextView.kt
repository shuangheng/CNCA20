package com.cnc.tom_macos.cnc_a20

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class MyTextView : TextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, style)


}