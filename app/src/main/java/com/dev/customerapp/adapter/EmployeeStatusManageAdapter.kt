package com.dev.customerapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.customerapp.R
import com.dev.customerapp.SetEmployeeStatus
import com.dev.customerapp.activity.UserDetailsActivity
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.databinding.ItemManageEmployeeStatusBinding
import com.dev.customerapp.models.EmployeeModel
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.utils.loadImage
import de.hdodenhof.circleimageview.CircleImageView


class EmployeeStatusManageAdapter(
    private val itemList: List<EmployeeModel>,
    private val setEmployeeStatus: SetEmployeeStatus,
) : RecyclerView.Adapter<EmployeeStatusManageAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemManageEmployeeStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(employeeModel: EmployeeModel) {

            binding.employeeName.text = employeeModel.employeeName // Set user name
            binding.employeeLocation.text =
                employeeModel.employeeDistrict + " " + employeeModel.employeeBlock // Set user location // Set mobile number

            binding.acceptEmployee.setOnClickListener {
                setEmployeeStatus.setEmployeeStatus(employeeModel,1,adapterPosition)
            }

            binding.rejectEmployee.setOnClickListener {
                setEmployeeStatus.setEmployeeStatus(employeeModel,2,adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Use the ViewBinding to inflate the layout
        val binding = ItemManageEmployeeStatusBinding.inflate(
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
