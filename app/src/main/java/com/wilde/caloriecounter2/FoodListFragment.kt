package com.wilde.caloriecounter2

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wilde.caloriecounter2.data.food.FoodListAdapter
import com.wilde.caloriecounter2.data.food.Product
import com.wilde.caloriecounter2.data.food.ProductAdapter
import com.wilde.caloriecounter2.viewmodels.FoodListViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val LOGGER_TAG = "Food List Fragment"

@AndroidEntryPoint
class FoodListFragment : Fragment() {

    companion object {
        fun newInstance() = FoodListFragment()
    }

    private val foodListViewModel: FoodListViewModel by viewModels()

    private val adapter = FoodListAdapter {
        Log.d("Product:", it.toString())
        val bundle = bundleOf("product" to it)
        //val bundle = Bundle().putSerializable("product", it)
        findNavController().navigate(R.id.foodFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout = inflater.inflate(R.layout.food_list_fragment, container, false)

        val search = layout.findViewById<EditText>(R.id.searchText)

        val ssb: SpannableStringBuilder = SpannableStringBuilder("   ")

        val draw: Drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.search_icon)!!

        val textSize = (search.textSize * 1.25).toInt()

        draw.setBounds(0, 0, textSize, textSize)

        ssb.setSpan(ImageSpan(draw), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        ssb.append(requireContext().getText(R.string.search))

        search.hint = ssb

        search.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val imm: InputMethodManager = layout.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager;
                    imm.hideSoftInputFromWindow(layout.windowToken, 0)

                    val foods = foodListViewModel.search(v.text.toString())

                    //val navController = findNavController()
                    //navController.navigate(R.id.foodFragment)
                    true
                }
                else -> false
            }
        }

        /*val search = layout.findViewById<SearchView>(R.id.food_search)

        search.setHint

        search.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(LOGGER_TAG, "Search text: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
            //.setOnQueryTextListener(SearchView.OnQueryTextListener)*/

        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val search = view.findViewById<SearchView>(R.id.food_search)

        //search.isIconified = false

        /*val onClickListener: (product: Product) -> Unit = {
            Log.d("Product:", it.toString())
        }*/

        val recyclerView = view.findViewById<RecyclerView>(R.id.food_list)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        foodListViewModel.foods.observe(viewLifecycleOwner) { foods ->
            adapter.submitList(foods)
        }
    }
}