package com.example.localdatastorage.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.localdatastorage.R
import com.example.localdatastorage.databinding.FragmentEditBinding
import com.example.localdatastorage.dialogfragments.MultipleChoiceDialogFragment
import com.example.localdatastorage.model.entities.ui.DonutUI
import com.example.localdatastorage.model.room.DonutDataBase
import com.example.localdatastorage.model.room.DonutsRepository
import com.example.localdatastorage.model.room.entities.Batter
import com.example.localdatastorage.model.room.entities.Topping
import com.example.localdatastorage.utils.retryIn
import com.example.localdatastorage.viewmodels.EditViewModel
import kotlinx.coroutines.flow.onEach

class EditFragment : Fragment(R.layout.fragment_edit) {

    private var _binding: FragmentEditBinding? = null
    private val binding
        get() = _binding!!

    private val dataBase by lazy {
        DonutDataBase.getInstance(requireContext())
    }

    private val repository by lazy {
        DonutsRepository(dataBase)
    }

    private val subscriptionDonutUI by lazy {
        editViewModel.channelDonutUI
            .onEach {
                setContent(it)
            }
            .retryIn(lifecycleScope)
    }

    private val editViewModel by viewModels<EditViewModel> { EditViewModel.Factory(repository) }

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

        subscriptionDonutUI
        editViewModel.getDonutUI(idDonut)
    }


    private fun setContent(donutUI: DonutUI) {
        binding.nameTextField.setText(donutUI.name)
        binding.ppuTextField.setText(donutUI.ppu.toString())
        binding.typeTextField.setText(donutUI.type)
        binding.batterTextView.text = donutUI.getBattersString()
        binding.toppingTextView.text = donutUI.getToppingsString()
        binding.allergyTextField.setText(
            donutUI.allergy ?: getString(R.string.allergy_null_edit_fragment)
        )

        binding.buttonOk.setOnClickListener {
            val donutUI: DonutUI = createNewDonutUI(donutUI)
            editViewModel.saveDonut(donutUI)
            findNavController().navigateUp()
        }
        binding.batterTextView.setOnClickListener {
            val listBatter = donutUI.batter.map { it.type }
            Log.d("EditFragment", "$listBatter")
            MultipleChoiceDialogFragment(listBatter, ::setBatterTextView)
                .show(childFragmentManager, null)
        }
        binding.toppingTextView.setOnClickListener {
            val listTopping = donutUI.topping.map { it.type }
            Log.d("EditFragment", "$listTopping")
            MultipleChoiceDialogFragment(listTopping, ::setToppingTextView)
                .show(childFragmentManager, null)
        }
    }

    private fun createNewDonutUI(donutUI: DonutUI): DonutUI {
        val id = donutUI.id
        val name = binding.nameTextField.text.toString()
        val ppu = binding.ppuTextField.text.toString()
        val type = binding.typeTextField.text.toString()
        val allergy = if (binding.allergyTextField.text.toString()
                .trim() == getString(R.string.allergy_null_edit_fragment)
        ) null else binding.allergyTextField.text.toString()
        val batterString = binding.batterTextView.text.toString()
        val toppingString = binding.toppingTextView.text.toString()
        val batter = getBatters(donutUI, batterString)
        val topping = getToppings(donutUI, toppingString)
        return DonutUI(id, name, ppu, type, batter, topping, allergy)
    }

    private fun getBatters(donutUI: DonutUI, batterString: String): List<Batter> {
        Log.d("EditFragment", "Batter string: $batterString")
        val list = batterString.split(",").map { it.trim() }
        Log.d("EditFragment", "$list")
        val batter = mutableListOf<Batter>()
        donutUI.batter.forEach {
            if (it.type in list)
                batter.add(it)
        }
        Log.d("EditFragment", "$batter")
        return batter
    }

    private fun getToppings(donutUI: DonutUI, toppingString: String): List<Topping> {
        Log.d("EditFragment", "Topping string: $toppingString")
        val list = toppingString.split(",").map { it.trim() }
        Log.d("EditFragment", "$list")
        val topping = mutableListOf<Topping>()
        donutUI.topping.forEach {
            if (it.type in list)
                topping.add(it)
        }
        Log.d("EditFragment", "$topping")
        return topping
    }

    private fun setBatterTextView(list: List<String>) {
        binding.batterTextView.text = list.toString().removePrefix("[").removeSuffix("]")
    }

    private fun setToppingTextView(list: List<String>) {
        binding.toppingTextView.text = list.toString().removePrefix("[").removeSuffix("]")
    }

    companion object {
        const val ID_ARG = "id_argument"
    }
}