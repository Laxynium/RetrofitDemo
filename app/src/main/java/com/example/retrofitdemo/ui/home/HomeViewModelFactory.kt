package com.example.retrofitdemo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitdemo.RetroInstance
import com.example.retrofitdemo.service.TodosService

class HomeViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                RetroInstance.getRetroInstance().create(TodosService::class.java)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}