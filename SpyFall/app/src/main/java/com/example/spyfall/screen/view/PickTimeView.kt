package com.example.spyfall.screen.view

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.example.spyfall.R
import com.example.spyfall.utils.times

class PickTimeView : Fragment(R.layout.fragment_pick_time) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createTimePicker(view)
    }

    private fun createTimePicker(view: View) {
        val timePicker = view.findViewById<NumberPicker>(R.id.timePicker)
        timePicker
        timePicker.minValue = 0
        timePicker.maxValue = times.size - 1
        timePicker.wrapSelectorWheel = true
        timePicker.displayedValues  = times.map { it.toString() }.toTypedArray()
        timePicker.setDividerColor(Color.BLUE)
    }

}


fun NumberPicker.removeDivider() {
    val pickerFields = NumberPicker::class.java.declaredFields
    for (pf in pickerFields) {
        if (pf.name == "mSelectionDivider") {
            pf.isAccessible = true
            try {
                val colorDrawable = ColorDrawable(Color.TRANSPARENT)
                pf[this] = colorDrawable
            } catch (e: java.lang.IllegalArgumentException) {
                // log exception here
            } catch (e: Resources.NotFoundException) {
                // log exception here
            } catch (e: IllegalAccessException) {
                // log exception here
            }
            break
        }
    }
}

fun NumberPicker.setItemCount(count: Int) {
    val pickerFields = NumberPicker::class.java.declaredFields
    for (pf in pickerFields) {
        if (pf.name == "mSelectorIndices") {
            pf.isAccessible = true
            try {
                val array = IntArray(count)
                pf[this] = array
            } catch (e: java.lang.IllegalArgumentException) {
                // log exception here
            } catch (e: Resources.NotFoundException) {
                // log exception here
            } catch (e: IllegalAccessException) {
                // log exception here
            }
            break
        }
    }
}

fun NumberPicker.setDividerColor(color: Int) {
    val pickerFields = NumberPicker::class.java.declaredFields
    for (pf in pickerFields) {
        if (pf.name == "mSolidColor") {
            pf.isAccessible = true
            try {
                val colorDrawable = ColorDrawable(color)
                pf[this] = colorDrawable
            } catch (e: java.lang.IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            break
        }
    }
}