package com.hkm.onplate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hkm.onplate.R
import com.hkm.onplate.core.presentation.model.Recipe


class SearchAdapter constructor(private val mContext: Context, layout: Int, private val data: List<Recipe>) : ArrayAdapter<Recipe>(
        mContext,
        layout,
        data
) {
    companion object {
        const val IMG_SIZE = 100
    }

    private var tempItems = ArrayList<Recipe>(data)
    private var suggestions = ArrayList<Recipe>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_search_results, parent, false)
        }

        val recipe = data[position]

        val tvTitle = view?.findViewById<TextView>(R.id.tv_search_title)
        val imgSearch = view?.findViewById<ImageView>(R.id.img_search)

        tvTitle?.text = recipe.title

        if (recipe.title == context.getString(R.string.no_recipe))
            Glide.with(mContext)
                .load(loadEmpty())
                .apply(
                    RequestOptions
                        .placeholderOf(com.hkm.onplate.core.R.drawable.ic_placeholder)
                        .error(com.hkm.onplate.core.R.drawable.ic_placeholder)
                        .override(IMG_SIZE, IMG_SIZE)
                )
                .into(imgSearch as ImageView)
        else
            Glide.with(mContext)
                .load(recipe.imageUrl)
                .apply(
                        RequestOptions
                                .placeholderOf(com.hkm.onplate.core.R.drawable.ic_placeholder)
                                .error(com.hkm.onplate.core.R.drawable.ic_placeholder)
                                .override(IMG_SIZE, IMG_SIZE)
                )
                .into(imgSearch as ImageView)

        imgSearch.clipToOutline = true

        return view as View
    }

    private fun loadEmpty(): Int = context
        .resources
        .getIdentifier("ic_close_white", "drawable", context.packageName)

    override fun getFilter(): Filter {
        return titleFilter
    }

    private val titleFilter = object : Filter() {
        override fun convertResultToString(resultValue: Any): CharSequence {
            return (resultValue as Recipe).title
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            suggestions.clear()
            for (recipe in tempItems) {
//                if (constraint != null) {
                    suggestions.add(recipe)
//                }
            }
            val filterResults = FilterResults()
            filterResults.values = suggestions
            filterResults.count = suggestions.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val result = (results?.values as List<*>).filterIsInstance<Recipe>()
            if (result.isNotEmpty()) {
                for (recipe in result) {
                    add(recipe)
                    notifyDataSetChanged()
                }
            }
        }
    }
}