package com.dev.customerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev.customerapp.activity.FragmentActivity
import com.dev.customerapp.databinding.ItemEmployeeListBinding
import com.dev.customerapp.models.EmployeeModel

class EmployeeAdapter(
    private val itemList: List<EmployeeModel>
) : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemEmployeeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(employeeModel: EmployeeModel) {

            binding.employeeName.text = employeeModel.employeeName // Set user name
            binding.employeeLocation.text =
                employeeModel.employeeAddress


            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, FragmentActivity::class.java)
                intent.putExtra("fragment_type", "employeeDetails")
                intent.putExtra("employeeId", employeeModel.employeeId)
                binding.root.context.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Use the ViewBinding to inflate the layout
        val binding = ItemEmployeeListBinding.inflate(
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