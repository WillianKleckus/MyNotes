package com.kleckus.mynotes.system

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kleckus.mynotes.R
import kotlinx.android.synthetic.main.note_or_book_item_layout.view.*

class Option(val description : String, val onClick : () -> Unit)

class OptionAdapter : RecyclerView.Adapter<OptionAdapter.VH>(){

    var dataset = mutableListOf<Option>()
    var onOptionChosen : () -> Unit = {}

    class VH(itemView : View) : RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.option_item_layout, parent,false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val currentOption = dataset[position]
        val itemView = holder.itemView
        itemView.description.text = currentOption.description
        itemView.setOnClickListener {
            currentOption.onClick()
            onOptionChosen()
        }
    }

    override fun getItemCount(): Int = dataset.size
}