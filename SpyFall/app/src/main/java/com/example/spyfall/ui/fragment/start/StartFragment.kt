package com.example.spyfall.ui.fragment.start

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.domain.entity.User
import com.example.spyfall.ui.fragment.BaseFragment
import com.example.spyfall.ui.fragment.prepare.PrepareFragment
import com.example.spyfall.ui.listener.CreateGameListener
import com.example.spyfall.ui.listener.JoinGameListener

class StartFragment : BaseFragment(R.layout.fragment_start), CreateGameListener, JoinGameListener {

    override val TAG: String
        get() = "StartGameFragment"

    private val user: User by lazy {
        requireArguments().getSerializable(KEY_USER)!! as User
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val childNavHost =
            childFragmentManager.findFragmentById(R.id.startGameContainerView) as NavHostFragment
        val childNavController = childNavHost.navController

        childNavController.navigate(R.id.joinGameView)

        view.findViewById<TextView>(R.id.nameTextView).text = user.name

        createButtons(view, childNavController)
    }

    private fun createButtons(view: View, childNavController: NavController) {
        val joinButton = view.findViewById<AppCompatButton>(R.id.buttonJoinGame)
        val createGameButton = view.findViewById<AppCompatButton>(R.id.buttonCreateGame)

        joinButton.isActivated = true

        joinButton.setOnClickListener {
            if (!joinButton.isActivated) {
                Log.d(TAG, "Click join button")
                childNavController.navigate(R.id.joinGameView)
                joinButton.isActivated = true
                createGameButton.isActivated = false
            }
        }

        createGameButton.setOnClickListener {
            if (!createGameButton.isActivated) {
                Log.d(TAG, "Click create button")
                childNavController.navigate(R.id.linkView)
                createGameButton.isActivated = true
                joinButton.isActivated = false
            }
        }
    }

    override fun createGame(gameId: String) {
        findNavController().navigate(R.id.action_startFragment_to_prepareFragment,
            PrepareFragment.getBundle(user, gameId)
        )
    }

    override fun join(gameId: String) {
        findNavController().navigate(R.id.action_startFragment_to_waitingGameFragment)
    }

    companion object {
        private const val TAG = "StartGameFragment"
        private const val KEY_USER = "key_user"

        fun getBundle(user: User): Bundle {
            return Bundle().apply {
                putSerializable(KEY_USER, user)
            }
        }
    }


}