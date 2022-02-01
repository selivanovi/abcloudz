package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.subfragment.WaitingGameFragment
import com.example.spyfall.ui.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VoteFragment : Fragment(R.layout.fragment_vote) {

    private val viewModel: VoteViewModel by viewModels()

    private val gameId: String by lazy {
        requireArguments().getString(KEY_GAME_ID)!!
    }

    private val isSpy: Boolean by lazy {
        requireArguments().getBoolean(KEY_IS_SPY)
    }

    private val isFinished: Boolean by lazy {
        requireArguments().getBoolean(KEY_IS_FINISHED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (isSpy) {
            childFragmentManager.commit {
                replace(R.id.voteContainerView, WaitingGameFragment())
            }
        }

        viewModel.observeVotePlayersInGame(gameId = gameId, isFinished)
        viewModel.observeVotePlayersInGameNew(gameId, isFinished)
    }

    companion object {
        private const val KEY_GAME_ID = "key_game_id"
        private const val KEY_IS_SPY = "key_user_id"
        private const val KEY_IS_FINISHED = "key_is_host"

        fun getInstance(gameId: String, isSpy: Boolean, isFinished: Boolean): Bundle {
            return Bundle().apply {
                putString(KEY_GAME_ID, gameId)
                putBoolean(KEY_IS_SPY, isSpy)
                putBoolean(KEY_IS_FINISHED, isFinished)
            }
        }
    }
}