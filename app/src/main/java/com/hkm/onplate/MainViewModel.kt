package com.hkm.onplate

import androidx.lifecycle.ViewModel
import com.hkm.onplate.core.domain.usecase.OnPlateUseCase

class MainViewModel constructor(private val mOnPlateUseCase: OnPlateUseCase) : ViewModel() {
    fun emptyRecipes() = mOnPlateUseCase.emptyRecipes()
    fun emptyAutoCompleteResults() = mOnPlateUseCase.emptyAutoCompleteResults()
}