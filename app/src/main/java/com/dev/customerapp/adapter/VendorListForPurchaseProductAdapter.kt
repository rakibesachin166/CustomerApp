package com.dev.customerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.customerapp.activity.FragmentActivity
import com.dev.customerapp.databinding.ItemVendorListBinding
import com.dev.customerapp.models.VendorModel

class VendorListForPurchaseProductAdapter(
    private val itemList: List<VendorModel>
) : RecyclerView.Adapter<VendorListForPurchaseProductAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemVendorListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vendorModel: VendorModel) {

            binding.vendorName.text = vendorModel.vendorName
            binding.vendorLocation.text =
                vendorModel.vendorAddress

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, FragmentActivity::class.java)
                intent.putExtra("vendorId", vendorModel.vendorId)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemVendorListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[holder.adapterPosition]
        holder.bind(item)
    }

    override fun getItemCount(): Int = itemList.size
}