package com.brianml31.insta_moon.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brianml31.insta_moon.R // R dosyasını import etmeyi unutma

class MenuOptionsAdapter(
    private val options: List<MenuOption>,
    private val listener: (MenuOption) -> Unit
) : RecyclerView.Adapter<MenuOptionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option, listener)
    }

    override fun getItemCount(): Int = options.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconTextView: TextView = itemView.findViewById(R.id.item_icon)
        private val textTextView: TextView = itemView.findViewById(R.id.item_text)

        fun bind(option: MenuOption, listener: (MenuOption) -> Unit) {
            iconTextView.text = option.icon
            textTextView.text = option.text
            itemView.setOnClickListener { listener(option) }
        }
    }
}
