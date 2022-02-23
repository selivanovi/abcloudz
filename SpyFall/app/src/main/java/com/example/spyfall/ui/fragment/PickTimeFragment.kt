package com.example.spyfall.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.spyfall.R
import com.example.spyfall.databinding.FragmentPickTimeBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.base.BaseViewModel
import com.example.spyfall.ui.listener.PickTimeFragmentListener
import com.example.spyfall.ui.viewmodel.PickTimeViewModel
import com.example.spyfall.utils.FragmentNotAttachedException
import com.example.spyfall.utils.times

class PickTimeFragment :
    BaseFragment<FragmentPickTimeBinding, BaseViewModel>(FragmentPickTimeBinding::inflate) {

    override val viewModel: PickTimeViewModel by viewModels()

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

    override fun setupView() {
        super.setupView()
        with(binding) {
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
    }

    override fun onDetach() {
        super.onDetach()
        pickTimeFragmentListener = null
    }

}
