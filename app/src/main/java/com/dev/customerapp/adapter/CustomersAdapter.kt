package com.dev.customerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.customerapp.SetEmployeeStatus
import com.dev.customerapp.activity.FragmentActivity
import com.dev.customerapp.databinding.ItemCustomerListBinding
import com.dev.customerapp.databinding.ItemManageEmployeeStatusBinding
import com.dev.customerapp.models.CustomerModel
import com.dev.customerapp.models.EmployeeModel
import com.dev.customerapp.utils.changeActivity

class CustomersAdapter (
    private val itemList: List<CustomerModel>
) : RecyclerView.Adapter<CustomersAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemCustomerListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customerModel: CustomerModel) {

            binding.customerName.text = customerModel.customerName // Set user name
            binding.customerLocation.text =
                customerModel.customerHouseNo +   " " + customerModel.customerAddress  + customerModel.customerLocallity

            binding.root.setOnClickListener{
                val intent = Intent(binding.root.context, FragmentActivity::class.java)
                intent.putExtra("fragment_type", "customerDetails")
                intent.putExtra("customerId", customerModel.customerId)
                binding.root.context.startActivity(intent)

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Use the ViewBinding to inflate the layout
        val binding = ItemCustomerListBinding.inflate(
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