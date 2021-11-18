package com.example.localdatastorage.screens

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.localdatastorage.viewmodels.LoginViewModel
import com.example.localdatastorage.MainActivity
import com.example.localdatastorage.R
import com.example.localdatastorage.databinding.FragmentLoginBinding
import com.example.localdatastorage.dialogfragments.BiometricDialogFragment
import com.example.localdatastorage.model.room.DonutDataBase
import com.example.localdatastorage.model.room.DonutsRepository
import com.example.localdatastorage.utils.BiometricUtil
import com.example.localdatastorage.utils.DonutJsonParser


class LogInFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    private val masterKey by lazy {
        MasterKey.Builder(requireContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val sharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            requireContext(),
            "secret_shared_preferences",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val dataBase: DonutDataBase by lazy {
        Room.databaseBuilder(
            requireContext(),
            DonutDataBase::class.java, "database-donut"
        ).build()
    }

    private val repository: DonutsRepository by lazy {
        DonutsRepository(dataBase.donutsDao)
    }

    private val loginViewModels: LoginViewModel by viewModels {
        LoginViewModel.Factory(sharedPreferences, repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        dataBase
        (requireActivity() as MainActivity).supportActionBar?.hide()
        if (loginViewModels.isFirstLaunch) {
            showScreenLogIn()

        } else {
            showScreenSignUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).supportActionBar?.show()
        _binding = null
    }

    private fun showScreenSignUp() {
        with(binding) {
            fingerprintCheckBox.isVisible = false
            loginButton.isVisible = false
            signupButton.setOnClickListener {
                if (checkEmailAndPassword()) {
                    findNavController().navigate(R.id.action_logInFragment_to_listFragment)
                }
            }
        }
        if (loginViewModels.getStateOfFingerPrint()) {
            BiometricUtil.show(this, ::setSucceedAuthentication, ::setErrorAuthentication)
        }
    }

    private fun checkEmailAndPassword(): Boolean {
        val email = binding.emailTextField.text!!.toString()
        val password = binding.passwordTextField.text!!.toString()
        val savedEmail = loginViewModels.getEmail()
        val savedPassword = loginViewModels.getPassword()
        return email == savedEmail && password == savedPassword
    }

    private fun showScreenLogIn() {
        with(binding) {
            signupButton.isVisible = false
            fingerprintCheckBox.isChecked = true
            loginButton.setOnClickListener {
                if (saveDataInEncryptedSharedPreferences()) {
                    val donutJson = DonutJsonParser.pareJson(requireContext(), "JSON 1.txt")
                    loginViewModels.loadDataInDataBase(donutJson)
                    findNavController().navigate(R.id.action_logInFragment_to_listFragment)
                }
            }
        }
    }

    private fun saveDataInEncryptedSharedPreferences(): Boolean {
        val email = binding.emailTextField.text!!.toString()
        val password = binding.passwordTextField.text!!.toString()
        val stateOfFingerPrint = binding.fingerprintCheckBox.isChecked
        try {
            loginViewModels.putEmail(email)
            loginViewModels.putPassword(password)
            loginViewModels.putStateOfFingerPrint(stateOfFingerPrint)
        } catch (e: Exception) {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.alert_dialog_error)
                .setMessage(e.message!!.toInt())
                .setPositiveButton(R.string.alert_dialog_positive_button) { _, _ -> }
                .create()
                .show()
            return false
        }
        return true
    }

    private fun setSucceedAuthentication() {
        findNavController().navigate(R.id.action_logInFragment_to_listFragment)
    }

    private fun setErrorAuthentication() {
        val biometricDialog = BiometricDialogFragment()
        biometricDialog.show(parentFragmentManager, null)
    }

    companion object {
        const val TAG = "LoginFragment"
    }
}
