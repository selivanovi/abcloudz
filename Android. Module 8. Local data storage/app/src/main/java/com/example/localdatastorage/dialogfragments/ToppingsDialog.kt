package com.example.localdatastorage.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.localdatastorage.R
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.entities.Topping
import com.example.localdatastorage.viewmodels.EditViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ToppingsDialog : DialogFragment() {

    private var argument: DonutUI? = null

    private val viewModel: EditViewModel by viewModels {
        EditViewModel.Factory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val argumentsNotNull = requireArguments()
        val donutId = argumentsNotNull.getInt(ID_DONUT_ARG)
        runBlocking {
            argument = viewModel.getDonutUI(donutId).first()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val toppings = argument!!.topping
        val selectedItemList = mutableListOf(*toppings.toTypedArray())
        val stringList = toppings.map { it.type }
        val checkedItems: Array<Boolean> = Array(toppings.size) { true }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.multiple_choice_dialog_title)
            .setMultiChoiceItems(
                stringList.toTypedArray(), checkedItems.toBooleanArray()
            ) { _, i, b ->
                if (b) {
                    selectedItemList.add(toppings[i])
                } else {
                    selectedItemList.remove(toppings[i])
                }
            }.setPositiveButton(R.string.multiple_choice_dialog_positive_button) { _, _ ->
                val newDonutUI = argument!!.copy(topping = selectedItemList)
                viewModel.saveDonut(newDonutUI)
            }
            .create()
    }
    companion object {
        const val ID_DONUT_ARG = "idDonut"
    }
}