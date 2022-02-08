package com.example.spyfall.ui.fragment.location

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.GameStatus
import com.example.spyfall.ui.fragment.BaseFragment
import com.example.spyfall.ui.fragment.prepare.WaitingGameFragment
import com.example.spyfall.ui.fragment.result.LocationWonFragment
import com.example.spyfall.ui.fragment.result.SpyWonFragment
import com.example.spyfall.ui.state.CheckState
import com.example.spyfall.ui.viewmodel.CheckLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CheckLocationFragment : BaseFragment(R.layout.fragment_check_location) {

    override val TAG: String
        get() = "CheckLocationFragment"

    private val viewModel: CheckLocationViewModel by viewModels()

    private val gameId: String by lazy { requireArguments().getString(KEY_GAME_ID)!! }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = requireArguments().getString(KEY_GAME_ID)!!


        viewModel.checkStateChannel.onEach { state ->
            if (state == CheckState.SpyLost) {
                findNavController().navigate(R.id.locationWonFragment, LocationWonFragment.getBundle(gameId))
            } else {
                findNavController().navigate(R.id.spyWonFragment, SpyWonFragment.getBundle(gameId))
            }
        }.launchIn(lifecycleScope)

        view.findViewById<AppCompatButton>(R.id.buttonCorrectly).setOnClickListener {
            viewModel.setStatusForGame(gameId, GameStatus.SPY_WON)
        }
        view.findViewById<AppCompatButton>(R.id.buttonCorrectly).setOnClickListener {
            viewModel.setStatusForGame(gameId, GameStatus.LOCATION_WON)
        }

        viewModel.observeGame(gameId)
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