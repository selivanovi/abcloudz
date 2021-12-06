package com.example.camera

import android.graphics.Bitmap
import android.graphics.Matrix

class Sticker(
    var bitmapOrg: Bitmap,
    var bitmap: Bitmap,
    var matrix: Matrix,
    var x: Float,
    var y: Float,
    var canMove: Boolean = false
)