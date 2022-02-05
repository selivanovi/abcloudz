package com.example.spyfall.ui.fragment.prepare

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.domain.entity.UserDomain
import com.example.spyfall.ui.fragment.prepare.sub.LobbyFragment
import com.example.spyfall.ui.listener.LobbyFragmentListener

class WaitingGameFragment : Fragment(R.layout.fragment_waiting_game), LobbyFragmentListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDomain: UserDomain = requireArguments().getSerializable(KEY_USER)!! as UserDomain
        val gameId: String = requireArguments().getString(KEY_GAME_ID)!!

        childFragmentManager.commit {
            add(R.id.waitingGameContainerView, LobbyFragment::class.java, LobbyFragment.getBundle(userDomain, gameId))
        }
    }

    override fun startGame() {
        findNavController().navigate(R.id.action_waitingGameFragment_to_roleFragment)
    }

    companion object {

        private const val KEY_GAME_ID = "key_game_id"
        private const val KEY_USER = "key_user"

        fun getBundle(userDomain: UserDomain, gameId: String): Bundle {
            return Bundle().apply {
                putSerializable(KEY_USER, userDomain)
                putString(KEY_GAME_ID, gameId)
            }
        }
    }
}