package com.example.localdatastorage.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.localdatastorage.R
import com.example.localdatastorage.model.entities.ui.DonutUI

class MultipleChoiceDialogFragment(
    private val list: List<String>,
    var onConfirmClickListener: ((List<String>) -> Unit)? = null
) : DialogFragment() {

    private val selectedItemList = mutableListOf<String>()
    private val checkedItems: Array<Boolean>

    init {
        selectedItemList.addAll(list)
        checkedItems = Array(list.size) { true }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.multiple_choice_dialog_title)
            .setMultiChoiceItems(
                list.toTypedArray(), checkedItems.toBooleanArray()
            ) { dialog, i, b ->
                if (b) {
                    selectedItemList.add(list[i])
                } else {
                    selectedItemList.remove(list[i])
                }
            }.setPositiveButton(R.string.multiple_choice_dialog_positive_button) { _, _ ->
                onConfirmClickListener?.invoke(selectedItemList)
            }
            .create()
    }
}