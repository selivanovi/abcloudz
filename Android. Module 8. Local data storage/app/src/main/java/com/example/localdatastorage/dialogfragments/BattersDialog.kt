package com.example.localdatastorage.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.localdatastorage.R
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.viewmodels.EditViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class BattersDialog : DialogFragment() {

    var argument: DonutUI? = null

    private val viewModel: EditViewModel by viewModels {
        EditViewModel.Factory(requireContext())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val batters = argument!!.batter
        val selectedItemList = mutableListOf(*batters.toTypedArray())
        val stringList = batters.map { it.type }
        val checkedItems: Array<Boolean> = Array(batters.size) { true }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.multiple_choice_dialog_title)
            .setMultiChoiceItems(
                stringList.toTypedArray(), checkedItems.toBooleanArray()
            ) { _, i, b ->
                if (b) {
                    selectedItemList.add(batters[i])
                } else {
                    selectedItemList.remove(batters[i])
                }
            }.setPositiveButton(R.string.multiple_choice_dialog_positive_button) { _, _ ->
                val newDonutUI = argument!!.copy(batter = selectedItemList)
                viewModel.saveDonut(newDonutUI)
            }
            .create()
    }
}