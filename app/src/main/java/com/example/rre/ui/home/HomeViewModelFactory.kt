package com.example.rre.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rre.repositories.PublicacionRepository

class HomeViewModelFactory(
    private val publicacionRepository: PublicacionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(publicacionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
//El HomeViewModelFactory es el "intermediario" que le dice a Android:

//"Cuando necesites crear un HomeViewModel"
//"Úsa este PublicacionRepository que ya preparé"
//"Y créalo de esta manera específica"