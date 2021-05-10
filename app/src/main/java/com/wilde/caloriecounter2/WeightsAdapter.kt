package com.wilde.caloriecounter2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wilde.caloriecounter2.data.weight.Weight

class WeightsAdapter internal constructor(
    private val onClickCallback: View.OnClickListener? = null
) : ListAdapter<Weight, WeightsAdapter.WeightViewHolder>(WeightsDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        return WeightViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.weight.toString(), onClickCallback)
    }

    class WeightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?, onClick: View.OnClickListener? = null) {
            wordItemView.text = text
            onClick?.let {
                wordItemView.setOnClickListener(it)
            }
        }



        companion object {
            fun create(parent: ViewGroup): WeightViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.weight_list_item, parent, false)
                return  WeightViewHolder(view)
            }
        }
    }
}

private class WeightsDiffCallback : DiffUtil.ItemCallback<Weight>() {
    override fun areItemsTheSame(oldItem: Weight, newItem: Weight): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: Weight, newItem: Weight): Boolean {
        return oldItem == newItem
    }

}