package com.example.camera

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class PhotoDialogFragment : DialogFragment() {

    private var listener: PhotoFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photo_dialog, container)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(requireActivity() is PhotoFragmentListener)
            listener = requireActivity() as PhotoFragmentListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.filesTextView).setOnClickListener {
            listener?.onClickFiles()
            dismiss()
        }
        view.findViewById<TextView>(R.id.cameraTextView).setOnClickListener {
            listener?.onClickCamera()
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
