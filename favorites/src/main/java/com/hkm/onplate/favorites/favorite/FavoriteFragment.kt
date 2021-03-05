package com.hkm.onplate.favorites.favorite

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hkm.onplate.core.adapter.RecipeAdapter
import com.hkm.onplate.core.data.Resource
import com.hkm.onplate.core.presentation.model.Recipe
import com.hkm.onplate.core.utils.DataMapper
import com.hkm.onplate.favorites.R
import com.hkm.onplate.favorites.databinding.FragmentFavoriteBinding
import com.hkm.onplate.favorites.di.favoritesModule
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class FavoriteFragment : Fragment() {
    private lateinit var recipeAdapter: RecipeAdapter
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding as FragmentFavoriteBinding

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        loadKoinModules(favoritesModule)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            recipeAdapter = RecipeAdapter()

            favoriteViewModel.favorites.observe(viewLifecycleOwner, { favorites ->
                if (favorites != null) {
                    when (favorites) {
                        is Resource.Loading -> binding.progressBarRecipe.visibility = View.VISIBLE
                        is Resource.Success -> {
                            binding.progressBarRecipe.visibility = View.GONE
                            val data = favorites.data
                            if (data.isNullOrEmpty()) binding.tvNoRecipe.visibility = View.VISIBLE
                            else binding.tvNoRecipe.visibility = View.GONE
                            if (data != null) {
                                recipeAdapter.setData(DataMapper.mapRecipeDomainsToItsPresentation(data))
                                setItemOnClickListener()

                                for (dt in data) {
                                    favoriteViewModel.insertRecipe(dt)
                                }
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

                adapter = recipeAdapter
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.favorite_menu)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setItemOnClickListener() {
        recipeAdapter.setOnClickListener(object : RecipeAdapter.OnClickListener {
            override fun onClick(recipe: Recipe) {
                val toDetailFragment = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                        recipe.title,
                        recipe.recipeId,
                )

                view?.findNavController()?.navigate(toDetailFragment)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val toHomeFragment = FavoriteFragmentDirections.actionFavoriteFragmentToHomeFragment()
                view?.findNavController()?.navigate(toHomeFragment)
                true
            }
            R.id.menu_delete_all -> {
                val mAlertDialog: AlertDialog
                val mBuilder: AlertDialog.Builder =
                        AlertDialog.Builder(requireActivity(), R.style.MyPopupMenu)

                mBuilder.setTitle(getString(R.string.delete_title))
                mBuilder.setMessage(getString(R.string.delete_text))

                mBuilder.setPositiveButton(getString(R.string.delete_confirm_yes)) { _, _ ->
                    favoriteViewModel.deleteAllFavorite()
                }

                mBuilder.setNegativeButton(getString(R.string.delete_confirm_no)) { dialog, _ ->
                    dialog.cancel()
                }

                mAlertDialog = mBuilder.create()
                mAlertDialog.setCanceledOnTouchOutside(true)
                mAlertDialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        unloadKoinModules(favoritesModule)
    }
}