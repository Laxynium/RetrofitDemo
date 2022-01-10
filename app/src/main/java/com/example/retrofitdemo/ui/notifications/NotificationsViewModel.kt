package com.example.retrofitdemo.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitdemo.RetroInstance
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response

class NotificationsViewModel(private val exerciseService: ExerciseService) : ViewModel() {
    fun generateCode(name: String): Observable<Response<Code>> {
        return exerciseService.generateCode(name);
    }

    fun validate(code: Code): Observable<Response<ValidationSuccessResponse>> {
        return exerciseService.validate(code.name, code.code);
    }
}

class NotificationsViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(
                RetroInstance.getRetroInstance().create(ExerciseService::class.java)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}