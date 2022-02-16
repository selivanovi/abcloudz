package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.spyfall.databinding.FragmentHowToPlayBinding
import com.example.spyfall.ui.base.DrawerFragment
import com.example.spyfall.ui.base.BaseViewModel
import com.example.spyfall.ui.viewmodel.HowToPlayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HowToPlayFragment :
    DrawerFragment<FragmentHowToPlayBinding, BaseViewModel>(FragmentHowToPlayBinding::inflate) {

    override val viewModel: HowToPlayViewModel by viewModels()

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