package com.example.spyfall.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.spyfall.R


class QuiteDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_quite_dialog, container, false).apply {
            findViewById<ImageView>(R.id.closeImage).setOnClickListener {
                dismiss()
            }
        }

    override fun onStart() {
        super.onStart()
        val dialogColor = ColorDrawable(Color.TRANSPARENT)

        dialog?.window?.setBackgroundDrawable(dialogColor)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }
}