package com.example.spyfall.ui.fragment.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.ui.fragment.BaseFragment
import com.example.spyfall.ui.fragment.result.LocationWonFragment
import com.example.spyfall.ui.fragment.vote.LocationVoteFragment
import com.example.spyfall.ui.viewmodel.CheckLocationViewModel
import com.example.spyfall.ui.viewmodel.CheckState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CallLocationFragment : BaseFragment(R.layout.fragment_call_location) {

    override val TAG: String
        get() = "CallLocationFragment"

    private val viewModel: CheckLocationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = requireArguments().getString(KEY_GAME_ID)!!

        viewModel.checkStateChannel.onEach { state ->
            if (state == CheckState.SpyLost) {
                findNavController().navigate(R.id.locationWonFragment, LocationWonFragment.getBundle(gameId))
            } else {
                findNavController().navigate(R.id.spyWonFragment, LocationVoteFragment.getBundle(gameId))
            }
        }.launchIn(lifecycleScope)

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