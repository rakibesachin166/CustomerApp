package com.dev.customerapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.customerapp.R
import com.dev.customerapp.activity.FragmentActivity
import com.dev.customerapp.models.Child1CategoryModel


class Child1CategoryAdapter(
    private var categories: List<Child1CategoryModel>
) : RecyclerView.Adapter<Child1CategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_child1_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[holder.adapterPosition]
        holder.categoryName.text = category.child1CategoryName
        holder.expandIcon.setOnClickListener {

            category.isExpanded = !category.isExpanded
            holder.expandIcon.setImageResource(if (category.isExpanded) R.drawable.icon_expand_less else R.drawable.icon_expand_more)
            holder.child2CategoryRecyclerView.visibility =
                if (category.isExpanded) View.VISIBLE else View.GONE


        }

        holder.itemView.setOnClickListener{
            val intent =  Intent(holder.itemView.context, FragmentActivity::class.java).apply {
                putExtra("fragment_type", "vendor_list_purchase_product")
                putExtra("topCategoryId", category.topCategoryId)
                putExtra("child1CategoryId", category.child1CategoryId)
                putExtra("child2CategoryId", 0)
            }
            holder.itemView.context.startActivity(intent)
        }
        if (category.child2CategoryList != null && category.child2CategoryList.isNotEmpty()) {

                Log.d("sachin", category.toString())
                holder.child2CategoryRecyclerView.adapter =
                    Child2CategoryAdapter(category.child2CategoryList)

        }
        else
        {
            Log.d("sachin", "Empty category")
            holder.child2CategoryRecyclerView.adapter = Child2CategoryAdapter(emptyList())
        }


    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)

        val expandIcon: ImageView = itemView.findViewById(R.id.expandIcon)

        val child2CategoryRecyclerView: RecyclerView =
            itemView.findViewById(R.id.child2CategoryRecyclerView)

    }


}
