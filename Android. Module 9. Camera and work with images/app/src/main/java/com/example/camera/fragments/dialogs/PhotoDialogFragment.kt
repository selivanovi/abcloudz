package com.example.camera.fragments.dialogs

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.camera.fragments.listeners.PhotoProviderFragmentListener
import com.example.camera.R

class PhotoDialogFragment : DialogFragment() {

    private var listener: PhotoProviderFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photo_dialog, container)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "$parentFragment")
        if(parentFragment is PhotoProviderFragmentListener) {
            listener = parentFragment as PhotoProviderFragmentListener
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.filesTextView).setOnClickListener {
            listener?.onClickFiles()
            Log.d(TAG, "onClickFiles")
            dismiss()
        }
        view.findViewById<TextView>(R.id.cameraTextView).setOnClickListener {
            listener?.onClickCamera()
            Log.d(TAG, "onClickCamera")
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        private const val TAG = "PhotoDialogFragment"
    }
}
