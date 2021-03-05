package com.hkm.onplate.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hkm.onplate.core.R
import com.hkm.onplate.core.databinding.ItemRecipesBinding
import java.util.*
import com.hkm.onplate.core.presentation.model.Recipe as PresentationRecipe


class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    companion object {
        const val IMG_BANNER_WIDTH = 500
        const val IMG_BANNER_HEIGHT = 750
    }

    private lateinit var onClickListener: OnClickListener
    private var listData = ArrayList<PresentationRecipe>()

    fun setData(newListData: List<PresentationRecipe>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemBinding =
                ItemRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = listData[position]
        holder.bind(recipe)
    }

    inner class RecipeViewHolder(private val itemBinding: ItemRecipesBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(recipe: PresentationRecipe) {
            with(itemBinding) {
                tvRecipeName.text = recipe.title
                root.setOnClickListener { onClickListener.onClick(recipe) }
                Glide.with(root.context)
                        .load(recipe.imageUrl)
                        .apply(
                                RequestOptions
                                        .placeholderOf(R.drawable.ic_placeholder)
                                        .error(R.drawable.ic_placeholder)
                                        .override(IMG_BANNER_WIDTH, IMG_BANNER_HEIGHT)
                        )
                        .into(imgRecipe)
                imgRecipe.clipToOutline = true
            }
        }
    }

    interface OnClickListener {
        fun onClick(recipe: PresentationRecipe)
    }

    override fun getItemCount(): Int = listData.size
}