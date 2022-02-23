package com.example.spyfall.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.spyfall.databinding.FragmentCreateLinkBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.listener.LinkFragmentListener
import com.example.spyfall.ui.viewmodel.LinkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinkFragment :
    BaseFragment<FragmentCreateLinkBinding, LinkViewModel>(FragmentCreateLinkBinding::inflate) {

    override val viewModel: LinkViewModel by viewModels()
    private var linkFragmentListener: LinkFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if (parent is LinkFragmentListener) {
            linkFragmentListener = parent
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = viewModel.generateGameId()

        with(binding) {
            textViewGameId.text = gameId

            buttonCool.setOnClickListener {
                linkFragmentListener?.createGame(gameId)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        linkFragmentListener = null
    }
}
