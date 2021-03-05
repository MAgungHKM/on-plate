package com.hkm.onplate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkm.onplate.databinding.ItemInstructionsBinding
import java.util.*

class InstructionsAdapter : RecyclerView.Adapter<InstructionsAdapter.InstructionsViewHolder>() {

    private lateinit var onClickListener: OnClickListener
    private var listData = ArrayList<String>()

    fun setData(newListData: List<String>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionsViewHolder {
        val itemBinding =
                ItemInstructionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InstructionsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: InstructionsViewHolder, position: Int) {
        val recipe = listData[position]
        holder.bind(recipe)
    }

    inner class InstructionsViewHolder(private val itemBinding: ItemInstructionsBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(instruction: String) {
            with(itemBinding) {
                tvInstruction.text = instruction
                root.setOnClickListener { onClickListener.onClick(instruction) }
            }
        }
    }

    interface OnClickListener {
        fun onClick(instruction: String)
    }

    override fun getItemCount(): Int = listData.size
}