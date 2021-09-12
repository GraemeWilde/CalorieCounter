package com.wilde.caloriecounter2.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wilde.caloriecounter2.R
import com.wilde.caloriecounter2.data.food.entities.Nutriments
import com.wilde.caloriecounter2.data.food.entities.PerServing
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.databinding.FoodFragmentBinding
import com.wilde.caloriecounter2.viewmodels.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [FoodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FoodFragment : Fragment(), SavingDialogCallback {

    private val viewModel: FoodViewModel by viewModels()
    private val binding get() = _binding!!

    private var _binding: FoodFragmentBinding? = null
    private var product: Product? = null

    private val args: FoodFragmentArgs by navArgs()

    private var foodChanged: Boolean = false

    private val savingDialog: ProgressDialog by lazy {
        val pd = ProgressDialog(context)
        pd.setTitle("Saving")
        pd.setMessage("Saving Food")
        pd
    }

    private inner class SaveButton : View.OnClickListener {
        override fun onClick(v: View?) {
            Log.d("Food Fragment", "foodChanged: $foodChanged")

            var id = 0
            if (product != null) {
                if (!foodChanged) {
                    viewModel.save(product!!)
                    return
                }
                id = product!!.id
            }

            val perServing = PerServing(
                binding.servingSizeEditTextNumber.text.toString().toFloatOrNull(),
                binding.caloriesEditTextNumber.text.toString().toFloatOrNull(),
                binding.fatEditTextNumber.text.toString().toFloatOrNull(),
                binding.saturatedFatEditTextNumber.text.toString().toFloatOrNull(),
                binding.transFatEditTextNumber.text.toString().toFloatOrNull(),
                binding.cholesterolEditTextNumber.text.toString().toFloatOrNull(),
                binding.sodiumEditTextNumber.text.toString().toFloatOrNull(),
                binding.carbohydrateEditTextNumber.toString().toFloatOrNull(),
                binding.fibreEditTextNumber.text.toString().toFloatOrNull(),
                binding.sugarsEditTextNumber.text.toString().toFloatOrNull(),
                binding.proteinsEditTextNumber.text.toString().toFloatOrNull()
            )
            val per100g = if (perServing.servingSize != null) {
                val factor = perServing.servingSize / 100f
                PerServing(
                    100f,
                    perServing.calories?.let { it/factor },
                    perServing.fat?.let { it/factor },
                    perServing.saturatedFat?.let { it/factor },
                    perServing.transFat?.let { it/factor },
                    perServing.cholesterol?.let { it/factor },
                    perServing.sodium?.let { it/factor },
                    perServing.carbohydrates?.let { it/factor },
                    perServing.fibre?.let { it/factor },
                    perServing.sugars?.let { it/factor },
                    perServing.proteins?.let { it/factor }
                )
            } else null
            val nutriments = Nutriments(
                perServing,
                per100g
            )
            val prod = Product(
                id,
                binding.productBrandsEditTextNumber.text.toString(),
                binding.foodNameEditTextNumber.text.toString(),
                product?.offId ?: "",
                binding.productCodeEditTextNumber.text.toString(),
                binding.productQuantityEditTextNumber.text.toString(),
                nutriments
            )
            viewModel.save(prod)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Food Fragment", "onCreateView")
        product = arguments?.get("product")?.let {
            it as Product
        }

        foodChanged = false

        _binding = FoodFragmentBinding.inflate(inflater, container, false)

        //val layout = binding.root //inflater.inflate(R.layout.food_fragment, container, false)


        /*val saveButton = layout.findViewById<Button>(R.id.save_button)

        val foodName = layout.findViewById<EditText>(R.id.foodNameEditTextNumber)
        val productCode = layout.findViewById<EditText>(R.id.productCodeEditTextNumber)
        val productQuantity = layout.findViewById<EditText>(R.id.productQuantityEditTextNumber)
        val productBrands = layout.findViewById<EditText>(R.id.productBrandsEditTextNumber)

        val servingSize = layout.findViewById<EditText>(R.id.servingSizeEditTextNumber)
        val calories = layout.findViewById<EditText>(R.id.caloriesEditTextNumber)
        val fat = layout.findViewById<EditText>(R.id.fatEditTextNumber)
        val saturatedFat = layout.findViewById<EditText>(R.id.saturatedFatEditTextNumber)
        val transFat = layout.findViewById<EditText>(R.id.transFatEditTextNumber)
        val cholesterol = layout.findViewById<EditText>(R.id.cholesterolEditTextNumber)
        val sodium = layout.findViewById<EditText>(R.id.sodiumEditTextNumber)
        val carbohydrate = layout.findViewById<EditText>(R.id.carbohydrateEditTextNumber)
        val fibre = layout.findViewById<EditText>(R.id.fibreEditTextNumber)
        val sugars = layout.findViewById<EditText>(R.id.sugarsEditTextNumber)
        val proteins = layout.findViewById<EditText>(R.id.proteinsEditTextNumber)*/

        binding.saveButton.setOnClickListener(this.SaveButton())

        binding.foodNameEditTextNumber.setText(product?.productName)
        binding.productCodeEditTextNumber.setText(product?.productCode)
        binding.productQuantityEditTextNumber.setText(product?.quantity)
        binding.productBrandsEditTextNumber.setText(product?.brands)

        product?.nutriments?.perServing?.let {
            binding.servingSizeEditTextNumber.setText(it.servingSize?.toString() ?: "")
            binding.caloriesEditTextNumber.setText(it.calories?.toString() ?: "")
            binding.fatEditTextNumber.setText(it.fat?.toString() ?: "")
            binding.saturatedFatEditTextNumber.setText(it.saturatedFat?.toString() ?: "")
            binding.transFatEditTextNumber.setText(it.transFat?.toString()?: "")
            binding.cholesterolEditTextNumber.setText(it.cholesterol?.toString()?: "")
            binding.sodiumEditTextNumber.setText(it.sodium?.toString()?: "")
            binding.carbohydrateEditTextNumber.setText(it.carbohydrates?.toString()?: "")
            binding.fibreEditTextNumber.setText(it.fibre?.toString()?: "")
            binding.sugarsEditTextNumber.setText(it.sugars?.toString()?: "")
            binding.proteinsEditTextNumber.setText(it.proteins?.toString()?: "")
        }



        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                foodChanged = true
                Log.d("Food Fragment", "All Watcher")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.foodNameEditTextNumber.addTextChangedListener(textWatcher)
        binding.productCodeEditTextNumber.addTextChangedListener(textWatcher)
        binding.productQuantityEditTextNumber.addTextChangedListener(textWatcher)
        binding.productBrandsEditTextNumber.addTextChangedListener(textWatcher)

        binding.servingSizeEditTextNumber.addTextChangedListener(textWatcher)
        binding.caloriesEditTextNumber.addTextChangedListener(textWatcher)
        binding.fatEditTextNumber.addTextChangedListener(textWatcher)
        binding.saturatedFatEditTextNumber.addTextChangedListener(textWatcher)
        binding.transFatEditTextNumber.addTextChangedListener(textWatcher)
        binding.cholesterolEditTextNumber.addTextChangedListener(textWatcher)
        binding.sodiumEditTextNumber.addTextChangedListener(textWatcher)
        binding.carbohydrateEditTextNumber.addTextChangedListener(textWatcher)
        binding.fibreEditTextNumber.addTextChangedListener(textWatcher)
        binding.sugarsEditTextNumber.addTextChangedListener(textWatcher)
        binding.proteinsEditTextNumber.addTextChangedListener(textWatcher)

        //binding.editingGroup

        //binding.foodGridLayout

        viewModel.askOverwrite.observe(viewLifecycleOwner, {
            if (it == true) {
                SavingDialogFragment().show(childFragmentManager, "saving_dialog_fragment")
            }
        })

        viewModel.saving.observe(viewLifecycleOwner, {
            if (it == true) {
                savingDialog.show()
            } else {
                savingDialog.dismiss()
            }
        })

        viewModel.saveSuccessful = ::saveSuccessful

        return binding.root
    }

    private fun saveSuccessful() {
        findNavController().navigate(R.id.action_foodFragment_to_foodListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        val navDrawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)

        Log.d("Food Fragment", "onStart")

        navDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onStop() {
        super.onStop()

        val navDrawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)

        navDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun cancelResponse() {

    }

    override fun addNewResponse() {
        Log.d("Food Fragment", "Prod" + viewModel.savingFood.toString())
        viewModel.save(viewModel.savingFood, FoodViewModel.Overwrite.NewRecord)
    }

    override fun updateOldResponse() {
        Log.d("Food Fragment", "Prod" + viewModel.savingFood.toString())
        viewModel.save(viewModel.savingFood, FoodViewModel.Overwrite.Overwrite)
    }
}