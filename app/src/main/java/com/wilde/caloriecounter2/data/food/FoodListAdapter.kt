package com.wilde.caloriecounter2.data.food

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wilde.caloriecounter2.R

class FoodListAdapter internal constructor(
    //private val onClickCallback: View.OnClickListener? = null,
    private val onClickCallback: ((product: Product) -> Unit)? = null
) : ListAdapter<Product, FoodListAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, onClickCallback)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var product: Product? = null

        private val container: ConstraintLayout = itemView.findViewById(R.id.foodItemContainer)
        private val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        private val productBrandTextView: TextView = itemView.findViewById(R.id.productBrandTextView)
        private val productQuantityTextView: TextView = itemView.findViewById(R.id.productQuantityTextView)

        fun bind(product: Product, onClick: ((product: Product) -> Unit)? = null) {
            //this.product = product

            productNameTextView.text = product.productName
            productBrandTextView.text = product.brands
            productQuantityTextView.text = product.quantity

            if (product.productName.isNullOrEmpty()) productNameTextView.visibility = View.GONE
            if (product.brands.isNullOrEmpty()) productBrandTextView.visibility = View.GONE
            if (product.quantity.isNullOrEmpty()) productQuantityTextView.visibility = View.GONE

            if (onClick != null) {
                container.setOnClickListener {
                    onClick(product)
                }
            } else {
                container.setOnClickListener(null)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ProductViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.food_list_item, parent, false)
                return ProductViewHolder(view)
            }
        }
    }
}

private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return if (newItem.id == oldItem.id && newItem.id != 0) {
            true
        } else {
            newItem.offId == oldItem.offId
        }
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}