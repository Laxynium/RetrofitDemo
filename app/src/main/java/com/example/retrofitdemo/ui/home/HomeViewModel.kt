package com.example.retrofitdemo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

//TODO add api calls
class HomeViewModel : ViewModel() {

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
}