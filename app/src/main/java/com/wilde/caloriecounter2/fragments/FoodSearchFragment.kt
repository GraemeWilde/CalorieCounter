package com.wilde.caloriecounter2.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wilde.caloriecounter2.R
import com.wilde.caloriecounter2.data.food.FoodSearchAdapter
import com.wilde.caloriecounter2.databinding.FoodSearchFragmentBinding
import com.wilde.caloriecounter2.viewmodels.FoodSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val LOGGER_TAG = "Food List Fragment"

@AndroidEntryPoint
class FoodSearchFragment : Fragment() {

    companion object {
        fun newInstance() = FoodSearchFragment()
    }

    private var _binding: FoodSearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val foodSearchViewModel: FoodSearchViewModel by activityViewModels()

    private val adapter = FoodSearchAdapter {
        Log.d("Product:", it.toString())
        val bundle = bundleOf("product" to it)
        //val bundle = Bundle().putSerializable("product", it)
        findNavController().navigate(R.id.foodFragment, bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_search_menu, menu)
        /*menu.findItem(R.id.searchInternet).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
        (menu.findItem(R.id.searchInternet).actionView as SearchView).setIconifiedByDefault(false)*/
        super.onCreateOptionsMenu(menu, inflater)

        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchInternetView = menu.findItem(R.id.searchInternet).actionView as SearchView

        searchInternetView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))

        foodSearchViewModel.queryText.observe(viewLifecycleOwner, Observer {
            searchInternetView.setQuery(it, false)
            //searchInternetView.clearFocus()
            //searchInternetView.requestFocus()
        })

        /*val searchInternetItem = menu.findItem(R.id.searchInternet)
        val searchInternetView = searchInternetItem.actionView as SearchView

        searchInternetView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val imm: InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager;
                    imm.hideSoftInputFromWindow(searchInternetView.windowToken, 0)



                    val foods = query?.let {
                        foodSearchViewModel.search(query)
                    } ?: null

                    //val navController = findNavController()
                    //navController.navigate(R.id.foodFragment)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            }
        )*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*arguments?.get("search")?.let {
            val query = it as Intent

            val foods = query.getStringExtra(SearchManager.QUERY)?.let { query_text ->
                foodSearchViewModel.search(query_text)
            }

        }*/

        //val layout = inflater.inflate(R.layout.food_search_fragment, container, false)
        _binding = FoodSearchFragmentBinding.inflate(inflater, container, false)



        foodSearchViewModel.searching.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.progressBar.visibility = View.VISIBLE
                binding.searchScrollView.visibility = View.GONE

            } else {
                binding.progressBar.visibility = View.GONE
                binding.searchScrollView.visibility = View.VISIBLE
            }
        })


        /*val search = layout.findViewById<EditText>(R.id.searchText)

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

                    val foods = foodSearchViewModel.search(v.text.toString())

                    //val navController = findNavController()
                    //navController.navigate(R.id.foodFragment)
                    true
                }
                else -> false
            }
        }*/

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val search = view.findViewById<SearchView>(R.id.food_search)

        //search.isIconified = false

        /*val onClickListener: (product: Product) -> Unit = {
            Log.d("Product:", it.toString())
        }*/

        val recyclerView = binding.foodListRecyclerView//view.findViewById<RecyclerView>(R.id.food_list)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        foodSearchViewModel.foods.observe(viewLifecycleOwner) { foods ->
            //if (foods.isNotEmpty())
                adapter.submitList(foods)
            /*else
                adapter.currentList.remo*/
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}