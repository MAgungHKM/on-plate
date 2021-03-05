package com.hkm.onplate.detail

import android.content.res.Configuration
import android.graphics.Outline
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hkm.onplate.R
import com.hkm.onplate.adapter.IngredientsAdapter
import com.hkm.onplate.adapter.InstructionsAdapter
import com.hkm.onplate.core.adapter.RecipeAdapter
import com.hkm.onplate.core.data.Resource
import com.hkm.onplate.core.utils.DataMapper
import com.hkm.onplate.databinding.FragmentDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel
import com.hkm.onplate.core.R as CoreR
import com.hkm.onplate.core.presentation.model.Recipe as PresentationRecipe

class DetailFragment : Fragment() {

    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var instructionsAdapter: InstructionsAdapter
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding as FragmentDetailBinding
    private val mDetailFragmentArgs: DetailFragmentArgs by navArgs()
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            ingredientsAdapter = IngredientsAdapter()
            instructionsAdapter = InstructionsAdapter()

            val recipeId = mDetailFragmentArgs.RecipeId
            detailViewModel.setRecipeId(recipeId)

            detailViewModel.getRecipeDetail().observe(viewLifecycleOwner, { recipe ->
                when (recipe) {
                    is Resource.Loading -> binding.progressBarDetail.visibility = View.VISIBLE
                    is Resource.Success -> {
                        val data = recipe.data
                        if (data != null) {
                            val state = detailViewModel.isFavorite()
                            setFavoriteState(state)

                            binding.progressBarDetail.visibility = View.GONE
                            populateView(DataMapper.mapRecipeDomainToItsPresentation(data))
                            setItemOnClickListener()
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBarDetail.visibility = View.GONE
                        Toast.makeText(context, "Uh oh...", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = mDetailFragmentArgs.RecipeTitle
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun populateView(recipe: PresentationRecipe) {
        with(binding) {
            topRound.clipToOutline = true
            val cornerRadius = 128F

            topRound.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    if (view !== null)
                        outline?.setRoundRect(0, 0, view.width, (view.height + cornerRadius).toInt(), cornerRadius)
                }
            }

            tvTitle.text = recipe.title
            tvCookingTime.text = recipe.readyTime
            tvDishType.text = recipe.dishType
            tvLike.text = recipe.likes
            tvScore.text = recipe.score
            tvServings.text = recipe.servings
            tvSource.text = recipe.source
            tvSummary.text = recipe.summary

            ingredientsAdapter.setData(recipe.ingredients)
            ingredientsAdapter.notifyDataSetChanged()

            instructionsAdapter.setData(recipe.instructions)
            instructionsAdapter.notifyDataSetChanged()

            with(lvIngredients) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)

                adapter = ingredientsAdapter
            }

            with(lvInstructions) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)

                adapter = instructionsAdapter
            }

            Glide.with(this@DetailFragment)
                    .load(recipe.imageUrl)
                    .apply(
                            RequestOptions
                                    .placeholderOf(CoreR.drawable.ic_placeholder)
                                    .error(CoreR.drawable.ic_placeholder)
                                    .override(RecipeAdapter.IMG_BANNER_WIDTH, RecipeAdapter.IMG_BANNER_HEIGHT)
                    )
                    .into(imgBanner)
            imgBanner.clipToOutline = true

            btnShare.setOnClickListener {
                val anim = AnimationUtils.loadAnimation(it.context, R.anim.button_click_anim)
                btnShare.startAnimation(anim).also {
                    setOnClickShare(recipe.title, recipe.sourceUrl)
                }
            }

            btnFavorite.setOnClickListener {
                val anim = AnimationUtils.loadAnimation(it.context, R.anim.button_click_anim)
                btnFavorite.startAnimation(anim)
                if (detailViewModel.setFavorite())
                    Toast.makeText(requireActivity(),
                            getString(R.string.text_add_favorite, recipe.title),
                            Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireActivity(),
                            getString(R.string.text_remove_favorite, recipe.title),
                            Toast.LENGTH_SHORT).show()
                setFavoriteState(detailViewModel.isFavorite())
            }
        }
    }

    private fun setFavoriteState(state: Boolean) {
        if (state)
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_white)
        else
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_void_white)
    }

    private fun setOnClickShare(title: String, sourceUrl: String) {
        val mimeType = "text/plain"
        ShareCompat.IntentBuilder.from(requireActivity()).apply {
            setType(mimeType)
            setChooserTitle(getString(R.string.share_title))
            setText(resources.getString(R.string.share_recipe, title, sourceUrl))
            startChooser()
        }
    }

    private fun setItemOnClickListener() {
        ingredientsAdapter.setOnClickListener(object : IngredientsAdapter.OnClickListener {
            override fun onClick(ingredient: String) {
                Toast.makeText(requireActivity(), ingredient, Toast.LENGTH_SHORT).show()
            }
        })

        instructionsAdapter.setOnClickListener(object : InstructionsAdapter.OnClickListener {
            override fun onClick(instruction: String) {
                Toast.makeText(requireActivity(), instruction, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.theme_menu, menu)
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES ->
                menu.getItem(0).setIcon(R.drawable.ic_moon_white)
            Configuration.UI_MODE_NIGHT_NO ->
                menu.getItem(0).setIcon(R.drawable.ic_sun_white)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            R.id.menu_theme -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    Configuration.UI_MODE_NIGHT_NO ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}