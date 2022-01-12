package com.example.camera

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import kotlinx.coroutines.*

class Filter(val color: Int, val mode: PorterDuff.Mode)


val filters = listOf<Filter>(
    Filter(Color.BLUE, PorterDuff.Mode.OVERLAY),
    Filter(Color.YELLOW, PorterDuff.Mode.OVERLAY),
    Filter(Color.GREEN, PorterDuff.Mode.OVERLAY)
)