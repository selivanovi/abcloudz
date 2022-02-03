package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.ui.viewmodel.ResultState
import com.example.spyfall.ui.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LocationWonFragment : BaseFragment(R.layout.fragment_location_won) {

    override val TAG: String
        get() = "LocationWonFragment"

    private val viewModel: ResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = requireArguments().getString(KEY_GAME_ID)!!

        viewModel.roleChannel.onEach { role ->
            role?.let {
                view.findViewById<AppCompatImageView>(R.id.locationImageView).setImageResource(it.drawable)
                view.findViewById<AppCompatTextView>(R.id.locationTextView).setText(it.string)
            }
        }.launchIn(lifecycleScope)

        viewModel.resultStateChannel.onEach { state ->
            if (state is ResultState.Exit) {
                findNavController().navigate(R.id.action_spyWonFragment_to_startGameFragment)
            }
            else {
                findNavController().navigate(R.id.roleFragment, )
            }
        }.launchIn(lifecycleScope)

        view.findViewById<AppCompatButton>(R.id.nextCardButton).setOnClickListener {
            viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.Continue)
        }
        view.findViewById<AppCompatButton>(R.id.mainMenuButton).setOnClickListener {
            viewModel.setStatusForCurrentPlayerInGame(gameId, PlayerStatus.EXIT)
        }

        viewModel.observeStatusOfCurrentPlayer(gameId)
        viewModel.observeStatusOfPlayers(gameId)
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