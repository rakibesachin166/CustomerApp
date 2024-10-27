package com.dev.customerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.dev.customerapp.R

class RadioButtonsAdapter(
    private val itemList: List<String>,
) : RecyclerView.Adapter<RadioButtonsAdapter.RadioButtonViewHolder>() {

    private var selectedPosition = 0

    inner class RadioButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton: RadioButton = itemView.findViewById(R.id.radio_button)

    }
    public fun getSelectedPosition(): Int = selectedPosition


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioButtonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_radio_button, parent, false)
        return RadioButtonViewHolder(view)
    }

    override fun onBindViewHolder(holder: RadioButtonViewHolder, position: Int) {
        val item = itemList[position]
        holder.radioButton.text = item
        holder.radioButton.isChecked = position == selectedPosition
        holder.radioButton.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                selectedPosition = position
                notifyDataSetChanged()
            }
        })
    }

    override fun getItemCount(): Int = itemList.size
}
