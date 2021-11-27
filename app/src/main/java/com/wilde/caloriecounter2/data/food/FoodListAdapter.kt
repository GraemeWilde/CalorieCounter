package com.wilde.caloriecounter2.data.food

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.databinding.FoodListItemBinding

class FoodListAdapter internal constructor(
    //private val onClickCallback: View.OnClickListener? = null,
    private val onClickCallback: ((product: Product) -> Unit)? = null
) : ListAdapter<Product, FoodListAdapter.ProductViewHolder>(ProductDiffCallback()) {

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            // Are same item if they have the same id, and the id isn't 0, or if they have the same
            // offId
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


    class ProductViewHolder(itemView: View, binding: FoodListItemBinding) : RecyclerView.ViewHolder(itemView) {
        private var product: Product? = null

        private val container: View = binding.root
        private val productNameTextView: TextView = binding.productNameTextView
        private val productBrandTextView: TextView = binding.productBrandTextView
        private val productQuantityTextView: TextView = binding.productQuantityTextView

        fun bind(product: Product, onClick: ((product: Product) -> Unit)? = null) {
            //this.product = product

            productNameTextView.text = product.productName
            productBrandTextView.text = product.brands
            productQuantityTextView.text = product.packageSize

            if (product.productName.isNullOrEmpty()) productNameTextView.visibility = View.GONE
            else productNameTextView.visibility = View.VISIBLE
            if (product.brands.isNullOrEmpty()) productBrandTextView.visibility = View.GONE
            else productBrandTextView.visibility = View.VISIBLE
            if (product.packageSize.isNullOrEmpty()) productQuantityTextView.visibility = View.GONE
            else productQuantityTextView.visibility = View.VISIBLE

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
                /*val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.food_list_item, parent, false)*/

                val binding: FoodListItemBinding = FoodListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                val view: View = binding.root
                return ProductViewHolder(view, binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, onClickCallback)
    }
}