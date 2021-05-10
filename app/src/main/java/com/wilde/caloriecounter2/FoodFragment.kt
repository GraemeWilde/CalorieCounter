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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Food Fragment", "onCreateView")
        product = arguments?.get("product")?.let {
            it as Product
        }
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button: Button = view.findViewById(R.id.button_save)

        button.setOnClickListener(View.OnClickListener {

        })

        Log.d("Food Fragment", "onViewCreated")

        requireView().findViewById<EditText>(R.id.foodNameEditTextNumber)
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
        }

        view.post(Runnable
        {
            val pw = view.findViewById<TextView>(R.id.proteinsTextView).width.toString()
            val sw = view.findViewById<TextView>(R.id.sugarsTextView).width.toString()

            view.findViewById<EditText>(R.id.proteinsEditTextNumber).setText(pw)
            view.findViewById<EditText>(R.id.sugarsEditTextNumber).setText(sw)
        })
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