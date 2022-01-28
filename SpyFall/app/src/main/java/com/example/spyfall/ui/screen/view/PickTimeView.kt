package com.example.spyfall.ui.screen.view

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.spyfall.R
import com.example.spyfall.ui.viewmodel.CreateGameViewModel
import com.example.spyfall.ui.viewmodel.PlayersViewModel
import com.example.spyfall.utils.times
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickTimeView : Fragment(R.layout.fragment_pick_time) {

    private val parentViewModel: CreateGameViewModel by viewModels(ownerProducer = { requireParentFragment().requireParentFragment() })
    private val viewModel: TimeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timePicker = view.findViewById<NumberPicker>(R.id.timePicker)
        val buttonPlay = view.findViewById<AppCompatButton>(R.id.buttonPlay)

        buttonPlay.setOnClickListener {
            val value = timePicker.value
            parentViewModel.gameId?.let { gameId -> viewModel.setTimeForGame(gameId, times[value]) }
        }
        timePicker
        timePicker.minValue = 0
        timePicker.maxValue = times.size - 1
        timePicker.wrapSelectorWheel = true
        timePicker.displayedValues  = times.map { it.toString() + resources.getString(R.string.measure_of_time) }.toTypedArray()
        timePicker.removeDivider()

        timePicker.value
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
