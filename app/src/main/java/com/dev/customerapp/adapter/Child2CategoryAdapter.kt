package com.dev.customerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.customerapp.R
import com.dev.customerapp.models.Child1CategoryModel
import com.dev.customerapp.models.Child2CategoryModel


class Child2CategoryAdapter(
    private var categories: List<Child2CategoryModel>
) : RecyclerView.Adapter<Child2CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_child2_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryName: TextView = itemView.findViewById(R.id.categoryName)

        fun bind(category: Child2CategoryModel) {
            categoryName.text = category.child2CategoryName

            itemView.setOnClickListener {

            }
        }
    }
}
