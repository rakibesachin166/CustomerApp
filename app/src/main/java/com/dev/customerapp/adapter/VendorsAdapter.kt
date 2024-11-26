package com.dev.customerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.customerapp.SetEmployeeStatus
import com.dev.customerapp.activity.FragmentActivity
import com.dev.customerapp.databinding.ItemCustomerListBinding
import com.dev.customerapp.databinding.ItemManageEmployeeStatusBinding
import com.dev.customerapp.databinding.ItemVendorListBinding
import com.dev.customerapp.models.VendorModel
import com.dev.customerapp.models.EmployeeModel

class VendorsAdapter(
    private val itemList: List<VendorModel>
) : RecyclerView.Adapter<VendorsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemVendorListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vendorModel: VendorModel) {

            binding.vendorName.text = vendorModel.vendorName // Set user name
            binding.vendorLocation.text =
                vendorModel.vendorAddress


            binding.root.setOnClickListener{
                val intent = Intent(binding.root.context, FragmentActivity::class.java)
                intent.putExtra("fragment_type", "vendorDetails")
                intent.putExtra("vendorId", vendorModel.vendorInt)
                binding.root.context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Use the ViewBinding to inflate the layout
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