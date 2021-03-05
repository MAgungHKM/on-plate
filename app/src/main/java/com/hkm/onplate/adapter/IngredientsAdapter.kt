package com.hkm.onplate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hkm.onplate.databinding.ItemIngredientsBinding
import java.util.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val itemBinding =
                ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        val recipe = listData[position]
        holder.bind(recipe)
    }

    inner class IngredientsViewHolder(private val itemBinding: ItemIngredientsBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(ingredient: String) {
            with(itemBinding) {
                tvIngredient.text = ingredient
                root.setOnClickListener { onClickListener.onClick(ingredient) }
            }
        }
    }

    interface OnClickListener {
        fun onClick(ingredient: String)
    }

    override fun getItemCount(): Int = listData.size
}