package com.example.localdatastorage.screens

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.localdatastorage.LoginViewModels
import com.example.localdatastorage.R
import com.example.localdatastorage.databinding.FragmentLoginBinding
import com.example.localdatastorage.dialogfragments.BiometricDialogFragment


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


    private val loginViewModels: LoginViewModels by viewModels {
        LoginViewModels.Factory(sharedPreferences)
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
        if (loginViewModels.isFirstLaunch) {
            showScreenLogIn()
        } else {
            showScreenSignUp()
            showBiometricCapability()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showScreenSignUp() {
        with(binding) {
            fingerprintCheckBox.isVisible = false
            loginButton.isVisible = false
        }
    }

    private fun showScreenLogIn() {
        with(binding) {
            signupButton.isVisible = false
            loginButton.setOnClickListener {
                val email = binding.emailTextField.text!!.toString()
                val password = binding.passwordTextField.text!!.toString()
                if (validateEmail(email = email) && validatePassword(password)) {
                    loginViewModels.putEmail(email)
                    loginViewModels.putPassword(password)
                }
                else {
                    AlertDialog.Builder(requireContext())
                        .setTitle(R.string.info_user_error)
                        .setMessage(R.string.info_user_message)
                }
                loginViewModels.putPassword("ilyaafafadfa")
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun showBiometricCapability() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(TAG, "App can authenticate using biometrics.")

                createBiometricPrompt()
                    .authenticate(
                        createPromptInfo(),
                    )
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e(LogInFragment.TAG, "No biometric features available on this device.")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e(LogInFragment.TAG, "Biometric features are currently unavailable.")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e(LogInFragment.TAG, "Biometric ")
                val biometricDialog = BiometricDialogFragment()
                biometricDialog.show(parentFragmentManager, null)
            }
        }
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(requireContext())

        val callback =
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.d(TAG, "$errorCode :: $errString")
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d(TAG, "Authentication failed for an unknown reason")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d(TAG, "Authentication was successful")
                    val email: String = loginViewModels.getEmail()
                    val password: String = loginViewModels.getPassword()
                    Log.d(TAG, "Email: $email\tPassword: $password")
                    binding.emailTextField.setText(email)
                    binding.passwordTextField.setText(password)
//                    findNavController().navigate(R.id.action_logInFragment_to_listFragment)
                }
            }

        return BiometricPrompt(this, executor, callback)
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {

        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.prompt_info_title))
            .setSubtitle(getString(R.string.prompt_info_subtitle))
            .setNegativeButtonText(getString(R.string.prompt_info_cancel))
            .build()
    }

    companion object {
        const val TAG = "LoginFragment"
    }
}
