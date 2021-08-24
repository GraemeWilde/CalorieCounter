package com.wilde.caloriecounter2

import android.app.ActionBar
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wilde.caloriecounter2.viewmodels.FoodListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodListFragment : Fragment() {

    companion object {
        fun newInstance() = FoodListFragment()
    }

    private val foodListViewModel: FoodListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.food_list_fragment, container, false)


        return layout
    }
}