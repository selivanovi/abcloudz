package com.example.spyfall.ui.fragment.prepare

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.domain.entity.User
import com.example.spyfall.ui.fragment.BaseFragment
import com.example.spyfall.ui.fragment.RoleFragment
import com.example.spyfall.ui.listener.StartGameListener
import com.example.spyfall.ui.viewmodel.CreateGameViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrepareFragment : BaseFragment(R.layout.fragment_prepare), StartGameListener {

    override val TAG: String
        get() = "InvitePlayerFragment"

    private val viewModel: CreateGameViewModel by viewModels()

    private val gameId: String by lazy {
        requireArguments().getString(KEY_GAME_ID)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user: User = requireArguments().getSerializable(KEY_USER)!! as User


        view.findViewById<TextView>(R.id.nameTextView).text = user.name
        view.findViewById<TextView>(R.id.gameIdTextView).text =
            resources.getString(R.string.textWelcomeToTheGameWithId, gameId)

        val childNavHost =
            childFragmentManager.findFragmentById(R.id.createGameContainerView) as NavHostFragment
        val childNavController = childNavHost.navController

        view.findViewById<TextView>(R.id.nameTextView).text = user.name

        createButtons(view, childNavController)

        viewModel.createGame(gameId, user)
    }

    override fun startGame() {
        findNavController().navigate(
            R.id.action_invitePlayerFragment_to_roleFragment,
            RoleFragment.getBundle(gameId)
        )
    }

    private fun createButtons(view: View, childNavController: NavController) {
        val buttonPlayers = view.findViewById<MaterialButton>(R.id.buttonPlayers)
        val buttonCardDuration = view.findViewById<AppCompatButton>(R.id.buttonCardDuration)

        buttonPlayers.isActivated = true
        childNavController.navigate(R.id.invitePlayerView)

        buttonPlayers.setOnClickListener {
            if (!buttonPlayers.isActivated) {
                Log.d(TAG, "Click players button")
                childNavController.navigate(R.id.invitePlayerView)
                buttonPlayers.isActivated = true
                buttonCardDuration.isActivated = false
            }
        }

        buttonCardDuration.setOnClickListener {
            if (!buttonCardDuration.isActivated) {
                Log.d(TAG, "Click time button")
                childNavController.navigate(R.id.pickTimeView)
                buttonCardDuration.isActivated = true
                buttonPlayers.isActivated = false
            }
        }
    }

    companion object {

        private const val TAG = "InvitePLayerFragment"

        private const val KEY_GAME_ID = "key_game_id"
        private const val KEY_USER = "key_user"

        fun getBundle(user: User, gameId: String): Bundle {
            return Bundle().apply {
                putSerializable(KEY_USER, user)
                putString(KEY_GAME_ID, gameId)
            }
        }
    }

}