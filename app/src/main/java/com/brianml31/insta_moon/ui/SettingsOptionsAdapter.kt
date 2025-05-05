package com.brianml31.insta_moon.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brianml31.instamoon.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsOptionsAdapter(
    private val options: MutableList<SettingOption>
) : RecyclerView.Adapter<SettingsOptionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_setting_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size

    fun getUpdatedOptions(): List<SettingOption> = options

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTextView: TextView = itemView.findViewById(R.id.item_text)
        private val switchMaterial: SwitchMaterial = itemView.findViewById(R.id.item_switch)

        fun bind(option: SettingOption) {
            textTextView.text = option.text
            switchMaterial.isChecked = option.isChecked
            switchMaterial.setOnCheckedChangeListener { _, isChecked ->
                option.isChecked = isChecked
            }
            // Switch'e basmadan satıra basınca da toggle etmesi için:
            itemView.setOnClickListener {
                switchMaterial.toggle()
            }
        }
    }
}
