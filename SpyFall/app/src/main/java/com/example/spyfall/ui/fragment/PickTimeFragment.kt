package com.example.spyfall.ui.fragment

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.spyfall.R
import com.example.spyfall.ui.listener.PickTimeFragmentListener
import com.example.spyfall.utils.times
import com.example.spyfall.utils.toSeconds


class PickTimeFragment : Fragment(R.layout.fragment_pick_time) {

    private var pickTimeFragmentListener: PickTimeFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if(parent is PickTimeFragmentListener) {
            pickTimeFragmentListener = parent
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timePicker = view.findViewById<NumberPicker>(R.id.timePicker)
        val buttonPlay = view.findViewById<AppCompatButton>(R.id.buttonPlay)

        buttonPlay.setOnClickListener {
            val value = timePicker.value
            pickTimeFragmentListener?.setTime(times[value].toSeconds())
        }


        val timesString = times.map {
            if (it > 0) {
                it.toString() + "\t" + resources.getString(R.string.measure_of_time)
            } else {
                resources.getString(R.string.no_limit)
            }
        }

        with(timePicker) {
            minValue = 0
            maxValue = timesString.size - 1
            wrapSelectorWheel = true
            displayedValues = timesString.toTypedArray()
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            removeDivider()
        }
    }

    override fun onDetach() {
        super.onDetach()
        pickTimeFragmentListener = null
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