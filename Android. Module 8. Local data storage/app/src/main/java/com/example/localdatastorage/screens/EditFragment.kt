package com.example.localdatastorage.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.localdatastorage.R
import com.example.localdatastorage.databinding.FragmentEditBinding
import com.example.localdatastorage.dialogfragments.BattersDialog
import com.example.localdatastorage.dialogfragments.ToppingsDialog
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.viewmodels.EditViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EditFragment : Fragment(R.layout.fragment_edit) {

    private var _binding: FragmentEditBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var donutUI: DonutUI

    private val editViewModel by viewModels<EditViewModel> { EditViewModel.Factory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val argumentsNotNull = arguments ?: error("Arguments not found")
        val idDonut = argumentsNotNull.getInt(ID_ARG)

        editViewModel.getDonutUI(idDonut).onEach {
            donutUI = it
            setContent(donutUI)
        }.launchIn(lifecycleScope)

        setClickListeners()
    }

    private fun setClickListeners() {
        binding.buttonOk.setOnClickListener {
            val newDonutUI = createNewDonutUI()
            editViewModel.saveDonut(newDonutUI)
            findNavController().navigateUp()
        }
        binding.batterTextView.setOnClickListener {
            val bundle = Bundle().apply { putInt(BattersDialog.ID_DONUT_ARG, donutUI.id) }
            BattersDialog().apply { arguments = bundle }.show(childFragmentManager, null)
        }
        binding.toppingTextView.setOnClickListener {
            val bundle = Bundle().apply { putInt(ToppingsDialog.ID_DONUT_ARG, donutUI.id) }
            ToppingsDialog().apply { arguments = bundle }.show(childFragmentManager, null)
        }
    }

    private fun createNewDonutUI(): DonutUI {
        val name = binding.nameTextField.text.toString()
        val ppu = binding.ppuTextField.text.toString()
        val type = binding.typeTextField.text.toString()
        val allergy = if (binding.allergyTextField.text.toString()
                .trim() == getString(R.string.allergy_null_edit_fragment)
        ) null else binding.allergyTextField.text.toString()

        return donutUI.copy(name = name, ppu = ppu, type = type, allergy = allergy)
    }

    private fun setContent(donutUI: DonutUI) {
        binding.nameTextField.setText(donutUI.name)
        binding.ppuTextField.setText(donutUI.ppu)
        binding.typeTextField.setText(donutUI.type)
        binding.allergyTextField.setText(
            donutUI.allergy ?: getString(R.string.allergy_null_edit_fragment)
        )
        binding.batterTextView.text = donutUI.getBattersString()
        binding.toppingTextView.text = donutUI.getToppingsString()
    }

    companion object {
        const val ID_ARG = "id_argument"
    }
}