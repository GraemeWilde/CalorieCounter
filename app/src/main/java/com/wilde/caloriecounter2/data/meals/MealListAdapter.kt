package com.wilde.caloriecounter2.data.meals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.databinding.MealListItemBinding

class MealListAdapter internal constructor(
    private val onClickCallback: ((mealAndComponentsAndFoods: MealAndComponentsAndFoods) -> Unit)? = null
) : ListAdapter<MealAndComponentsAndFoods, MealListAdapter.MealViewHolder>(MealDiffCallback()) {

    private class MealDiffCallback : DiffUtil.ItemCallback<MealAndComponentsAndFoods>() {
        override fun areItemsTheSame(oldItem: MealAndComponentsAndFoods, newItem: MealAndComponentsAndFoods): Boolean {
            return newItem.meal.id == oldItem.meal.id
        }

        override fun areContentsTheSame(oldItem: MealAndComponentsAndFoods, newItem: MealAndComponentsAndFoods): Boolean {
            return oldItem == newItem
        }
    }


    class MealViewHolder(itemView: View, private val binding: MealListItemBinding) : RecyclerView.ViewHolder(itemView) {
        private var mealAndComponentsAndFoods: MealAndComponentsAndFoods? = null

        //private val container: View = binding.root
        fun bind(mealAndComponentsAndFoods: MealAndComponentsAndFoods, onClick: ((mealAndComponentsAndFoods: MealAndComponentsAndFoods) -> Unit)? = null) {
            binding.mealNameTextView.text = mealAndComponentsAndFoods.meal.name

            if (onClick != null) {
                binding.root.setOnClickListener {
                    onClick(mealAndComponentsAndFoods)
                }
            } else {
                binding.root.setOnClickListener(null)
            }
        }

        companion object {
            fun create(parent: ViewGroup): MealViewHolder {
                val binding = MealListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                val view = binding.root
                return MealViewHolder(view, binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, onClickCallback)
    }

}
