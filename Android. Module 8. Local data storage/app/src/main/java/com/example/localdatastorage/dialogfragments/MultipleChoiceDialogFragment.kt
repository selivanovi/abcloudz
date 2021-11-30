package com.example.localdatastorage.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.localdatastorage.R
import com.example.localdatastorage.model.entities.ui.DonutUI

class MultipleChoiceDialogFragment : DialogFragment() {

    var list: List<String>? = null
    var onConfirmClickListener: ((List<String>) -> Unit)? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val listNotNull = list!!
        val clickListenerNotNull = onConfirmClickListener!!

        val selectedItemList = mutableListOf<String>(*listNotNull.toTypedArray())
        val checkedItems: Array<Boolean> = Array(listNotNull.size) { true }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.multiple_choice_dialog_title)
            .setMultiChoiceItems(
                listNotNull.toTypedArray(), checkedItems.toBooleanArray()
            ) { dialog, i, b ->
                if (b) {
                    selectedItemList.add(listNotNull[i])
                } else {
                    selectedItemList.remove(listNotNull[i])
                }
            }.setPositiveButton(R.string.multiple_choice_dialog_positive_button) { _, _ ->
                clickListenerNotNull.invoke(selectedItemList)
            }
            .create()
    }
}