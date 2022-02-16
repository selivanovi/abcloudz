package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.spyfall.databinding.FragmentAboutBinding
import com.example.spyfall.ui.base.BaseViewModel
import com.example.spyfall.ui.base.DrawerFragment
import com.example.spyfall.ui.viewmodel.AboutViewModel

class AboutFragment :
    DrawerFragment<FragmentAboutBinding, BaseViewModel>(FragmentAboutBinding::inflate) {

    override val viewModel: AboutViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            back.setOnClickListener {
                viewModel.navigateUp()
            }
            buttonCool.setOnClickListener {
                viewModel.navigateUp()
            }
        }
    }
}
