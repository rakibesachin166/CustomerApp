package com.dev.customerapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.customerapp.R
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.models.TopCategoryModel
import com.dev.customerapp.utils.loadImage
import de.hdodenhof.circleimageview.CircleImageView


class TopCategoryAdapter(
    private val categories: List<TopCategoryModel>
) : RecyclerView.Adapter<TopCategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_top_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Log.d("sachinPosition", holder.adapterPosition.toString())
        val category = categories[holder.adapterPosition]

        holder.categoryName.text = category.topCategoryName

        Log.d("sachinPosition", holder.adapterPosition.toString())

        holder.categoryImage.loadImage(ApiClient.BASE_URL + category.topCategoryImage)

        holder.itemView.setOnClickListener {

            category.isExpanded = !category.isExpanded
            holder.expandIcon.setImageResource(if (category.isExpanded) R.drawable.icon_expand_less else R.drawable.icon_expand_more)
            holder.child1CategoryRecyclerView.visibility =
                if (category.isExpanded) View.VISIBLE else View.GONE
        }
        Log.d("sachinPosition", holder.adapterPosition.toString())

        if (category.child1CategoryList!=null){
            if (category.child1CategoryList.isNotEmpty()) {
                Log.d("sachin", category.toString())
                holder.child1CategoryRecyclerView.adapter =
                    Child1CategoryAdapter(category.child1CategoryList)
            } else {
                Log.d("sachin", "Empty category")
                holder.child1CategoryRecyclerView.adapter = Child1CategoryAdapter(emptyList())
            }
        } else {
            Log.d("sachin", "Empty category")
            holder.child1CategoryRecyclerView.adapter = Child1CategoryAdapter(emptyList())
        }



    }

    override fun getItemCount(): Int = categories.size

    // ViewHolder class
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val expandIcon: ImageView = itemView.findViewById(R.id.expandIcon)
        val categoryImage: CircleImageView = itemView.findViewById(R.id.categoryImage)
        val child1CategoryRecyclerView: RecyclerView =
            itemView.findViewById(R.id.child1CategoryRecyclerView)

    }
}
