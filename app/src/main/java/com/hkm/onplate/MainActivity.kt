package com.hkm.onplate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hkm.onplate.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.emptyAutoCompleteResults()
    }
}