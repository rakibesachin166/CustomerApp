package com.dev.customerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.customerapp.R
import com.dev.customerapp.api.ApiClient
import com.dev.customerapp.models.UserDataModel
import com.dev.customerapp.utils.loadImage
import de.hdodenhof.circleimageview.CircleImageView


class UsersAdapter(
    private val itemList: List<UserDataModel>,
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImageView: CircleImageView = itemView.findViewById(R.id.userImageView)
        val userName: AppCompatTextView = itemView.findViewById(R.id.userName)
        val mobileNumber: AppCompatTextView = itemView.findViewById(R.id.mobileNumber)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[holder.adapterPosition]
        holder.userName.text = item.userName
        holder.mobileNumber.text = item.userMobileNo
        holder.userImageView.loadImage(ApiClient.BASE_URL + item.userPhoto)
    }

    override fun getItemCount(): Int = itemList.size
}
