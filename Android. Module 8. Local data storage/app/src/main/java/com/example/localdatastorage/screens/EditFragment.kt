package com.example.localdatastorage.screens

import android.app.AlertDialog
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
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.viewmodels.EditViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
            createBattersAlertDialog()?.show()
        }
        binding.toppingTextView.setOnClickListener {
            createToppingsAlertDialog()?.show()
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

    private fun createToppingsAlertDialog(): AlertDialog? {
        val toppings = donutUI.topping
        val selectedItemList = mutableListOf(*toppings.toTypedArray())
        val stringList = toppings.map { it.type }
        val checkedItems: Array<Boolean> = Array(toppings.size) { true }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.multiple_choice_dialog_title)
            .setMultiChoiceItems(
                stringList.toTypedArray(), checkedItems.toBooleanArray()
            ) { _, i, b ->
                if (b) {
                    selectedItemList.add(toppings[i])
                } else {
                    selectedItemList.remove(toppings[i])
                }
            }.setPositiveButton(R.string.multiple_choice_dialog_positive_button) { _, _ ->
                val newDonutUI = donutUI.copy(topping = selectedItemList)
                editViewModel.saveDonut(newDonutUI)
            }
            .create()
    }

    private fun createBattersAlertDialog(): AlertDialog? {
        val toppings = donutUI.batter
        val selectedItemList = mutableListOf(*toppings.toTypedArray())
        val stringList = toppings.map { it.type }
        val checkedItems: Array<Boolean> = Array(toppings.size) { true }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.multiple_choice_dialog_title)
            .setMultiChoiceItems(
                stringList.toTypedArray(), checkedItems.toBooleanArray()
            ) { _, i, b ->
                if (b) {
                    selectedItemList.add(toppings[i])
                } else {
                    selectedItemList.remove(toppings[i])
                }
            }.setPositiveButton(R.string.multiple_choice_dialog_positive_button) { _, _ ->
                val newDonutUI = donutUI.copy(batter = selectedItemList)
                editViewModel.saveDonut(newDonutUI)
            }
            .create()
    }

    companion object {
        const val ID_ARG = "id_argument"
    }
}