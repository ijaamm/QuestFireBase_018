package com.example.firebase.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.firebase.MahasiswaApp
import com.example.firebase.ui.home.viewmodel.HomeViewModel
import com.example.firebase.ui.home.viewmodel.InsertViewModel

fun CreationExtras.MahasiswaApp(): MahasiswaApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as MahasiswaApp)

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer { HomeViewModel(MahasiswaApp().container.repositoryMhs) }

        initializer { InsertViewModel(MahasiswaApp().container.repositoryMhs) }
    }
}