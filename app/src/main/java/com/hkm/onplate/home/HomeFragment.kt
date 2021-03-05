package com.hkm.onplate.home

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hkm.onplate.R
import com.hkm.onplate.adapter.SearchAdapter
import com.hkm.onplate.core.adapter.RecipeAdapter
import com.hkm.onplate.core.data.Resource
import com.hkm.onplate.core.utils.DataMapper
import com.hkm.onplate.core.utils.hideKeyboard
import com.hkm.onplate.databinding.FragmentHomeBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import com.hkm.onplate.core.presentation.model.Recipe as PresentationRecipe

@FlowPreview
@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {
    private lateinit var recipeAdapter: RecipeAdapter
    private var _binding: FragmentHomeBinding? = null
    private var isDelayed = false
    private val binding get() = _binding as FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            recipeAdapter = RecipeAdapter()

            homeViewModel.getRecipes().observe(viewLifecycleOwner, { recipes ->
                if (recipes != null) {
                    when (recipes) {
                        is Resource.Loading -> binding.progressBarRecipe.visibility = View.VISIBLE
                        is Resource.Success -> {
                            binding.progressBarRecipe.visibility = View.GONE
                            val data = recipes.data
                            if (data != null) {
                                recipeAdapter.setData(DataMapper.mapRecipeDomainsToItsPresentation(data))
                                setItemOnClickListener()
                                recipeAdapter.notifyDataSetChanged()
                            }
                        }
                        is Resource.Error -> {
                            binding.progressBarRecipe.visibility = View.GONE
                            Toast.makeText(context,
                                "Uh oh...",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

            with(binding.rvRecipe) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)

                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val layoutManager =
                            LinearLayoutManager::class.java.cast(recyclerView.layoutManager) as LinearLayoutManager
                        val totalItemCount = layoutManager.itemCount
                        val lastVisible = layoutManager.findLastVisibleItemPosition()

                        val endHasBeenReached = lastVisible + 1 >= totalItemCount
                        if (!isDelayed && totalItemCount > 0 && endHasBeenReached && binding.progressBarRecipe.isGone) {
                            milliSecondDelay(1000L)
                            binding.progressBarRecipe.visibility = View.VISIBLE
                            homeViewModel.loadMoreRecipes()
                        }
                    }
                })

                adapter = recipeAdapter
            }

            with(binding.edSearch) {
                addTextChangedListener({ _, _, _, _ -> }, { query, _, _, _ ->
                    run {
                        homeViewModel.emptyAutoCompleteResults()
                        val searchQuery = query.toString()
                        if (!isDelayed && searchQuery.trim()
                                .isNotEmpty() && searchQuery.length > 3
                        ) {
                            milliSecondDelay(1500L)
                            homeViewModel.search(searchQuery).observe(
                                viewLifecycleOwner,
                                { autocompleteResult ->
                                    if (autocompleteResult != null) {
                                        when (autocompleteResult) {
                                            is Resource.Loading -> binding.progressBarRecipe.visibility =
                                                View.VISIBLE
                                            is Resource.Success -> {
                                                binding.progressBarRecipe.visibility = View.GONE
                                                val data = autocompleteResult.data
                                                if (!data.isNullOrEmpty()) {
                                                    val adapter = SearchAdapter(
                                                        requireActivity(),
                                                        android.R.layout.select_dialog_item,
                                                        DataMapper.mapRecipeDomainsToItsPresentation(
                                                            data
                                                        )
                                                    )
                                                    adapter.notifyDataSetChanged()
                                                    setAdapter(adapter)
                                                    showDropDown()
                                                } else {
                                                    val placeholder =
                                                        ArrayList<PresentationRecipe>()
                                                    placeholder.add(
                                                        PresentationRecipe(
                                                            title = requireContext().getString(R.string.no_recipe)
                                                        )
                                                    )

                                                    val adapter = SearchAdapter(
                                                        requireActivity(),
                                                        android.R.layout.select_dialog_item,
                                                        placeholder
                                                    )
                                                    adapter.notifyDataSetChanged()
                                                    setAdapter(adapter)
                                                    showDropDown()
                                                }
                                            }
                                            is Resource.Error -> {
                                                binding.progressBarRecipe.visibility = View.GONE
                                                Toast.makeText(
                                                    context,
                                                    "Uh oh...",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                })
                        }
                    }
                }, {})
            }

            binding.edSearch.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                    run {
                        hideKeyboard()

                        val item = adapterView.getItemAtPosition(position) as PresentationRecipe

                        if (item.title != requireContext().getString(R.string.no_recipe)) {
                            homeViewModel.insertRecipe(
                                DataMapper.mapRecipePresentationToItsDomain(
                                    item
                                )
                            )

                            val toDetailFragment =
                                HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                                    item.title,
                                    item.recipeId,
                                )

                            view.findNavController().navigate(toDetailFragment)
                        }
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setItemOnClickListener() {
        recipeAdapter.setOnClickListener(object : RecipeAdapter.OnClickListener {
            override fun onClick(recipe: PresentationRecipe) {
                val toDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                        recipe.title,
                        recipe.recipeId,
                )

                view?.findNavController()?.navigate(toDetailFragment)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                val toFavoriteFragment =
                        HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
                view?.findNavController()?.navigate(toFavoriteFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun milliSecondDelay(timeMillis: Long) {
        isDelayed = true
        lifecycleScope.launch {
            delay(timeMillis)
            isDelayed = false
        }
    }

    override fun onDestroyView() {
        binding.rvRecipe.adapter = null
        _binding = null
        super.onDestroyView()
    }
}