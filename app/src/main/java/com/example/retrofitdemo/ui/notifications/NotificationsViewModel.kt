package com.example.retrofitdemo.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitdemo.RetroInstance
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response

class NotificationsViewModel(private val exerciseService: ExerciseService) : ViewModel() {
    fun generateCode(name: String): Observable<Response<Code>> {
        //TODO exercise : add appropriate method to exercise service and invoke it here
        return Observable.empty()
    }

    fun validate(code: Code): Observable<Response<ValidationSuccessResponse>> {
        //TODO exercise : add appropriate method to exercise service and invoke it here
        return Observable.empty()
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