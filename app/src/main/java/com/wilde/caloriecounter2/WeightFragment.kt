package com.wilde.caloriecounter2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wilde.caloriecounter2.data.weight.Weight
import com.wilde.caloriecounter2.viewmodels.WeightViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime


@AndroidEntryPoint
class WeightFragment : Fragment() {

    private val viewModel: WeightViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val binding = FragmentWeightBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_weight, container, false) //binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onClickListener = View.OnClickListener {
            if (it is TextView) {
                viewModel.insert(LocalDateTime.now(), it.text.toString().toFloat())
            }
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = WeightsAdapter(onClickListener)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        //viewModel.insert(LocalDateTime.now().minusMinutes(2), 173f)

        /*AppDatabase.getInstance(context).weightDAO().insertAll(
            WeightEntity(LocalDateTime.now(), 100f),
            WeightEntity(
                LocalDateTime.now().minusDays(2).minusMinutes(200),
                120f
            )
        )*/




        viewModel.weights.observe(viewLifecycleOwner) { weights ->
            weights.let {
                val items: List<Pair<Weight, View.OnClickListener?>> =
                    it.map { weight -> Pair(weight, onClickListener) }//mutableListOf()

                adapter.submitList(it)
            }
        }

        //recyclerView


        //viewModel.insert(LocalDateTime.now(), 149f)
    }


    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeightViewModel::class.java)
        // TODO: Use the ViewModel
    }// */

}