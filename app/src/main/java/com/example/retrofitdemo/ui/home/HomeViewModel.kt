package com.example.retrofitdemo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.retrofitdemo.RetroInstance
import com.example.retrofitdemo.service.TodosService

//TODO add api calls
class HomeViewModel : ViewModel() {

    private val todosService: TodosService = RetroInstance.getRetroInstance().create(TodosService::class.java)

    private val _todos = MutableLiveData<MutableList<Todo>>().apply {
        value = arrayListOf(
            Todo(1, "TODO 1", false)
        )
    }
    val todos: LiveData<List<Todo>> = _todos.map { it.toList() }

    fun addTodo(todo: Todo) {
        val newId: Int = (_todos.value?.toList()?.maxByOrNull { it.id }?.id ?: 0) + 1
        _todos.value = _todos.value.apply {
            this?.add(todo.copy(id = newId))
        }
    }

    fun updateTodo(todo: Todo) {
        val idx = _todos.value!!.indexOfFirst { it.id == todo.id }
        _todos.value = _todos.value.apply {
            this!![idx] = todo
        }

    }

    fun removeTodo(todo: Todo) {
        _todos.value = _todos.value.apply {
            this?.removeAll { it.id == todo.id }
        }
    }

//    fun makeAPICall() {
//        val call = todosService.getAll(10,0)
//        call.enqueue(object : Callback<List<Todo>> {
//            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
//                 recyclerListData.postValue(null)
//            }
//
//            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
//                if(response.isSuccessful){
//                    // TODO GG
//                    recyclerListData.postValue(TodoList(ArrayList(response.body())))
//                } else {
//                    recyclerListData.postValue(null)
//                }
//            }
//        })
//    }
}
