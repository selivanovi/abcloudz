package com.example.spyfall.ui.fragment

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.spyfall.databinding.FragmentJoinGameBinding
import com.example.spyfall.ui.base.BaseFragment
import com.example.spyfall.ui.base.BaseViewModel
import com.example.spyfall.ui.listener.JoinGameFragmentListener
import com.example.spyfall.ui.viewmodel.JoinGameViewModel
import com.example.spyfall.utils.Constants
import com.example.spyfall.utils.FragmentNotAttachedException

class JoinGameFragment :
    BaseFragment<FragmentJoinGameBinding, BaseViewModel>(FragmentJoinGameBinding::inflate) {

    override val viewModel: JoinGameViewModel by viewModels()

    private var joinGameFragmentListener: JoinGameFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = requireParentFragment()
        if (parent is JoinGameFragmentListener) {
            joinGameFragmentListener = parent
        } else {
            throw FragmentNotAttachedException("JoinGameFragment")
        }
    }

    override fun setupView() {
        super.setupView()

        val data: Uri? = requireActivity().intent.data

        with(binding) {
            data?.let {
                val gameId = it.getQueryParameter(Constants.ID_PARAMETER)
                joinGameEditText.setText(gameId)
            }

            buttonJoinGame.setOnClickListener {
                val gameId = joinGameEditText.text.toString()
                if (gameId.length != Constants.LENGTH_ID) {
                    Toast.makeText(requireContext(), Constants.NOT_CORRECT_GAME_ID, Toast.LENGTH_LONG)
                        .show()
                } else {
                    joinGameFragmentListener?.joinToGame(gameId)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        joinGameFragmentListener = null
    }
}
