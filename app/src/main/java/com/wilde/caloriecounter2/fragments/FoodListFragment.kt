package com.wilde.caloriecounter2.fragments

import android.os.Bundle
import android.view.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilde.caloriecounter2.composables.screens.FoodList
import com.wilde.caloriecounter2.composables.screens.Food
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.viewmodels.FoodViewModel2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodListFragment : Fragment() {

    /*private var _binding: FoodListFragmentBinding? = null
    private val binding get() = _binding!!

    private val foodListViewModel: FoodListViewModel by viewModels()

    private val adapter = FoodListAdapter { product ->
        Log.d("Product:", product.toString())
        val bundle = bundleOf("product" to product)
        //val bundle = Bundle().putSerializable("product", it)
        findNavController().navigate(R.id.foodFragment, bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.food_list_menu, menu)

        val searchInternetMenuItem = menu.findItem(R.id.searchInternet)
        val searchFoodListMenuItem = menu.findItem(R.id.searchFoodList)

        val expandListener = object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                menu.setGroupVisible(R.id.foodListMenuGroup, false)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                activity?.invalidateOptionsMenu()
                return true
            }
        }

        searchInternetMenuItem.setOnActionExpandListener(expandListener)
        searchFoodListMenuItem.setOnActionExpandListener(expandListener)

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchInternetView = menu.findItem(R.id.searchInternet).actionView as SearchView

        searchInternetView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val layout = inflater.inflate(R.layout.food_list_fragment, container, false)
        /*_binding = FoodListFragmentBinding.inflate(inflater, container, false)

        binding.foodListRecyclerView.adapter = adapter
        binding.foodListRecyclerView.layoutManager = LinearLayoutManager(this.context)

        foodListViewModel.foods.observe(viewLifecycleOwner) { foods ->
            adapter.submitList(foods)
        }

        return binding.root*/

        return ComposeView(requireContext()).apply {
            setContent {
                //var foodList by remember { mutableStateOf(true) }
                var product by remember { mutableStateOf<Product?>(null) }

                if (product == null) {
                    FoodList() {
                        //findNavController().navigate(R.id.foodFragment, bundleOf("product" to it))
                        product = it
                    }
                } else {
                    val foodViewModel: FoodViewModel2 = viewModel()

                    foodViewModel.openProduct(product!!)

                    Food(foodViewModel)
                }
            }
        }
    }
}