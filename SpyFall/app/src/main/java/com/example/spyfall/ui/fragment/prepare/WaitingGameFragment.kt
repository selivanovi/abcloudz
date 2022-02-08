package com.example.spyfall.ui.fragment.prepare

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.BaseFragment
import com.example.spyfall.ui.fragment.prepare.sub.LobbyFragment
import com.example.spyfall.ui.listener.LobbyFragmentListener
import com.example.spyfall.ui.viewmodel.WaitingGameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WaitingGameFragment : BaseFragment(R.layout.fragment_waiting_game), LobbyFragmentListener {

    private val viewModel: WaitingGameViewModel by viewModels()

    override val TAG: String
        get() = "WaitingGameFragment"

    private val gameId: String by lazy { requireArguments().getString(KEY_GAME_ID)!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        view.findViewById<TextView>(R.id.nameTextView).text = viewModel.currentUser.name

        childFragmentManager.commit {
            add(
                R.id.waitingGameContainerView,
                LobbyFragment::class.java,
                LobbyFragment.getBundle(gameId)
            )
        }
    }

    override fun onBackPressed() {
        viewModel.clearGame(gameId)
    }

    override fun startGame() {
        findNavController().navigate(R.id.action_waitingGameFragment_to_roleFragment)
    }

    override fun exit() {
        findNavController().popBackStack(R.id.startFragment, false)
    }

    companion object {

        private const val KEY_GAME_ID = "key_game_id"

        fun getBundle(gameId: String): Bundle {
            return Bundle().apply {
                putString(KEY_GAME_ID, gameId)
            }
        }
    }
}