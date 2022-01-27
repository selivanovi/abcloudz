package com.example.spyfall.data.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.spyfall.R


enum class Role(@StringRes val string: Int, @DrawableRes val drawable: Int) {

    SPY(R.string.spy, R.drawable.image_spy),
    BEACH(R.string.beach, R.drawable.image_beach),
    SUPERMARKET(R.string.supermarket, R.drawable.image_supermarket),
    LIGHTHOUSE(R.string.lighthouse, R.drawable.image_lighthouse),
    OFFICE(R.string.office, R.drawable.image_supermarket),
    RAILWAY_CARRIAGE(R.string.railway_carriage, R.drawable.image_supermarket)
}