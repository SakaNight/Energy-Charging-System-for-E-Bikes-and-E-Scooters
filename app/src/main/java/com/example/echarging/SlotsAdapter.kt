package com.example.echarging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SlotsAdapter(private val onClick: (Slot) -> Unit) : ListAdapter<Slot, SlotsAdapter.SlotViewHolder>(
    SlotViewHolder.SlotDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slot, parent, false)
        return SlotViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val slot = getItem(position)
        holder.bind(slot)
    }

    class SlotViewHolder(itemView: View, val onClick: (Slot) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val textViewSlotId: TextView = itemView.findViewById(R.id.textViewSlotId)
        private val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)
        private var currentSlot: Slot? = null

        init {
            itemView.setOnClickListener {
                currentSlot?.let {
                    onClick(it)
                }
            }
        }

        fun bind(slot: Slot) {
            currentSlot = slot
            textViewSlotId.text = "Slot ID: ${slot.slot_id}"
            textViewStatus.text =
                if (slot.slot_status == "available") "Available" else "Unavailable"
            textViewStatus.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (slot.slot_status == "available") android.R.color.holo_green_dark else android.R.color.holo_red_dark
                )
            )
            println("Binding Slot: ${slot.slot_id}, Status: ${slot.slot_status}")
        }


        class SlotDiffCallback : DiffUtil.ItemCallback<Slot>() {
            override fun areItemsTheSame(oldItem: Slot, newItem: Slot): Boolean {
                return oldItem.slot_id == newItem.slot_id
            }

            override fun areContentsTheSame(oldItem: Slot, newItem: Slot): Boolean {
                return oldItem == newItem
            }
        }
    }
}