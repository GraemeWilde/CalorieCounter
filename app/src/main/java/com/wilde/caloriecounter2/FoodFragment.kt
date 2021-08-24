package com.wilde.caloriecounter2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.wilde.caloriecounter2.data.food.Product
import com.wilde.caloriecounter2.viewmodels.FoodViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [FoodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FoodFragment : Fragment() {

    private val viewModel: FoodViewModel by viewModels()
    private var product: Product? = null /*arguments?.get("product")?.let {
        //Log.d("Product: ", product.toString())
        it as Product
    }*/

    val args: FoodFragmentArgs by navArgs()

    private class saveButton : View.OnClickListener {
        override fun onClick(v: View?) {

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

        val layout = inflater.inflate(R.layout.food_fragment, container, false)

        val saveButton = layout.findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener(FoodFragment.saveButton())

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
        val proteins = layout.findViewById<EditText>(R.id.proteinsEditTextNumber)

        foodName.setText(product?.productName)
        productCode.setText(product?.productCode)
        productQuantity.setText(product?.quantity)
        productBrands.setText(product?.brands)

        product?.nutriments?.perServing?.let {
            servingSize.setText(it.servingSize?.toString() ?: "")
            calories.setText(it.calories?.toString() ?: "")
            fat.setText(it.fat?.toString() ?: "")
            saturatedFat.setText(it.saturatedFat?.toString() ?: "")
            transFat.setText(it.transFat?.toString()?: "")
            cholesterol.setText(it.cholesterol?.toString()?: "")
            sodium.setText(it.sodium?.toString()?: "")
            carbohydrate.setText(it.carbohydrates?.toString()?: "")
            fibre.setText(it.fibre?.toString()?: "")
            sugars.setText(it.sugars?.toString()?: "")
            proteins.setText(it.proteins?.toString()?: "")
        }

        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button: Button = view.findViewById(R.id.save_button)

        button.setOnClickListener(View.OnClickListener {

        })

        Log.d("Food Fragment", "onViewCreated")

        //requireView().findViewById<EditText>(R.id.foodNameEditTextNumber).edit

        /*requireView().findViewById<EditText>(R.id.foodNameEditTextNumber)
            .setText(product?.productName)
        requireView().findViewById<EditText>(R.id.productCodeEditTextNumber)
            .setText(product?.productCode)
        requireView().findViewById<EditText>(R.id.productQuantityEditTextNumber).setText(product?.quantity)
        requireView().findViewById<EditText>(R.id.productBrandsEditTextNumber).setText(product?.brands)
        product?.nutriments?.perServing?.let {
            requireView().findViewById<EditText>(R.id.servingSizeEditTextNumber).setText(it.servingSize?.toString() ?: "")
            requireView().findViewById<EditText>(R.id.caloriesEditTextNumber).setText(it.calories?.toString() ?: "")
            requireView().findViewById<EditText>(R.id.fatEditTextNumber).setText(it.fat?.toString() ?: "")
            requireView().findViewById<EditText>(R.id.saturatedFatEditTextNumber).setText(it.saturatedFat?.toString() ?: "")
            requireView().findViewById<EditText>(R.id.transFatEditTextNumber).setText(it.transFat?.toString()?: "")
            requireView().findViewById<EditText>(R.id.cholesterolEditTextNumber).setText(it.cholesterol?.toString()?: "")
            requireView().findViewById<EditText>(R.id.sodiumEditTextNumber).setText(it.sodium?.toString()?: "")
            requireView().findViewById<EditText>(R.id.carbohydrateEditTextNumber).setText(it.carbohydrates?.toString()?: "")
            requireView().findViewById<EditText>(R.id.fibreEditTextNumber).setText(it.fibre?.toString()?: "")
            requireView().findViewById<EditText>(R.id.sugarsEditTextNumber).setText(it.sugars?.toString()?: "")
            requireView().findViewById<EditText>(R.id.proteinsEditTextNumber).setText(it.proteins?.toString()?: "")
        }*/

        /*view.post(Runnable
        {
            val pw = view.findViewById<TextView>(R.id.proteinsTextView).width.toString()
            val sw = view.findViewById<TextView>(R.id.sugarsTextView).width.toString()

            view.findViewById<EditText>(R.id.proteinsEditTextNumber).setText(pw)
            view.findViewById<EditText>(R.id.sugarsEditTextNumber).setText(sw)
        })*/
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
}