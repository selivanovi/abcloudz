package com.example.spyfall.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.spyfall.R

class InvitePlayerScreen : Fragment(R.layout.fragment_invite_player) {

    companion object {

        private const val KEY_GAME_ID = "key_game_id"

        fun getInstance(gameId: String): Bundle {
            return Bundle().apply { putString(KEY_GAME_ID, gameId) }
        }
    }
}