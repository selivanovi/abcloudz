package com.example.spyfall.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.spyfall.ui.navigation.NavEvent
import com.example.spyfall.utils.Inflate
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(
    val inflate: Inflate<VB>
) : Fragment() {

    private var _binding: VB? = null

    protected val binding
        get() = _binding!!

    abstract val viewModel: VM

    open fun onBackPressed() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListeners()
        setupObserver()
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.coroutineContext.cancelChildren()
        viewModel.clear()
    }


    protected open fun setupView() {
    }

    protected open fun setupListeners() {
    }

    protected open fun setupObserver() {
        observeError()
        observeNavigation()
    }

    private fun observeError() {
        viewModel.errorChannel.onEach { throwable ->
            Log.e("BaseFragment", throwable.message.toString())
            handleError(throwable)
        }.launchIn(lifecycleScope)
    }

    open fun handleError(throwable: Throwable) {

    }

    private fun observeNavigation() {
        viewModel.navigationChannel.onEach { event ->
            handleNavigation(event)
        }.launchIn(lifecycleScope)
    }

    private fun handleNavigation(event: NavEvent) {
        when (event) {
            is NavEvent.To -> findNavController().navigate(event.navDirections)
            NavEvent.Back -> findNavController().popBackStack()
            NavEvent.Up -> findNavController().navigateUp()
        }
    }
}
