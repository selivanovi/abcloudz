package com.example.spyfall.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.spyfall.R
import com.example.spyfall.ui.listener.PickTimeFragmentListener
import com.example.spyfall.utils.FragmentNotAttachedException
import com.example.spyfall.utils.times
import com.example.spyfall.utils.toSeconds

class PickTimeFragment : Fragment(R.layout.fragment_pick_time) {

    private var pickTimeFragmentListener: PickTimeFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if (parent is PickTimeFragmentListener) {
            pickTimeFragmentListener = parent
        } else {
            throw FragmentNotAttachedException("PickTimeFragment")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timePicker = view.findViewById<NumberPicker>(R.id.timePicker)
        val buttonPlay = view.findViewById<AppCompatButton>(R.id.buttonPlay)

        buttonPlay.setOnClickListener {
            val value = timePicker.value
            pickTimeFragmentListener?.setTime(times[value].inWholeSeconds)
        }

        val timesString = times.map {
            if (it.inWholeMinutes > 0) {
                it.inWholeMinutes.toString() + "\t" + resources.getString(R.string.measureOfTime)
            } else {
                resources.getString(R.string.noLimit)
            }
        }

        with(timePicker) {
            minValue = 0
            maxValue = timesString.size - 1
            wrapSelectorWheel = true
            displayedValues = timesString.toTypedArray()
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }
    }

    override fun onDetach() {
        super.onDetach()
        pickTimeFragmentListener = null
    }
}
