package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.viewmodel.BaseViewModel

class HowToPlayFragment : BaseFragment<BaseViewModel>(R.layout.fragment_how_to_play) {

    override val viewModel: BaseViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.back).setOnClickListener {
            findNavController().popBackStack()
        }
        view.findViewById<AppCompatButton>(R.id.buttonCool).setOnClickListener {
            findNavController().popBackStack()
        }
    }


}