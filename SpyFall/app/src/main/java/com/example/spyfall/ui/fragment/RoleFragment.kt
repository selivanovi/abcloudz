package com.example.spyfall.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spyfall.R
import com.example.spyfall.data.entity.PlayerStatus
import com.example.spyfall.ui.viewmodel.RoleState
import com.example.spyfall.ui.viewmodel.RoleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RoleFragment : Fragment(R.layout.fragment_role) {

    private val viewModel: RoleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationTextView = view.findViewById<AppCompatTextView>(R.id.locationTextView)
        val timeTextView = view.findViewById<AppCompatTextView>(R.id.timeTextView)
        val locationImageView = view.findViewById<AppCompatImageView>(R.id.locationImageView)
        val locationButton = view.findViewById<AppCompatButton>(R.id.locationButton)
        val voteButton = view.findViewById<AppCompatButton>(R.id.voteButton)


        viewModel.setRolesInGame()

        viewModel.roleStateChannel.onEach { state ->
            when (state) {
                is RoleState.SetRoleState -> {
                    locationTextView.text = resources.getString(state.role.string)
                    locationImageView.background = resources.getDrawable(state.role.drawable, null)
                }
                is RoleState.VoteSpyState -> {
                    //todo
                }
                is RoleState.VotePlayerState -> {
                    findNavController().navigate(R.id.voteFragment)
                }
            }
        }.launchIn(lifecycleScope)

        viewModel.timeChannel.onEach {
            if (it != null) {
                val minutes = it / 60
                val seconds = it % 60

                Log.d("RoleFragment", "$minutes:$seconds")

                timeTextView.text = "$minutes:$seconds"
            } else {

            }
        }.launchIn(lifecycleScope)

        voteButton.setOnClickListener {
            viewModel.setStatusForСurrentPlayerInGame(PlayerStatus.VOTE)
        }

        locationButton.setOnClickListener {
            viewModel.setStatusForСurrentPlayerInGame(PlayerStatus.LOCATION)
        }
    }
}