package com.example.retrofitdemo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import arrow.core.Either
import com.example.retrofitdemo.ApiError
import com.example.retrofitdemo.service.CreateTodo
import com.example.retrofitdemo.service.TodosService
import com.example.retrofitdemo.service.UpdateTodo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val todosService: TodosService) : ViewModel() {
    private val _todos =  MutableLiveData<List<Todo>>().apply {
        value = emptyList()
    }
    private val _error = MutableLiveData<String?>();
    val todos: LiveData<List<Todo>> = _todos.map { it.toList() }
    val error: LiveData<String?> = _error

    val todos2: LiveData<Either<ApiError, List<Todo>>> = this.todosService.getAllCustom(3,0)

    fun loadTodos() {
        val call = todosService.getAll(10, 0)
        val callback = object : Callback<List<Todo>> {
            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                if (response.isSuccessful) {
                    val todos = response.body()
                    _todos.value = todos
                    _error.value = null
                } else {
                    _error.value = "Loading todos failed";
                }
            }

            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                _error.value = "Loading todos failed";
            }

        }
        call.enqueue(callback)
    }

    fun loadTodosObs() {
        todosService.getAllObs(10, 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _todos.value = it
                _error.value = null
            }, {
                _error.value = "Loading todos failed"
            })
    }

    fun addTodo(todo: Todo) {
        val call = todosService.create(CreateTodo(todo.text, todo.completed))
        val callback = object : Callback<Todo> {
            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                if (response.isSuccessful) {
                    val createdTodo = response.body()!!
                    val newList = _todos.value?.toMutableList()
                    newList?.add(createdTodo)
                    _todos.value = newList?.toList()
                } else {
                    _error.value = "Adding todo failed";
                }
            }

            override fun onFailure(call: Call<Todo>, t: Throwable) {
                _error.value = "Adding todo failed";
            }

        }
        call.enqueue(callback)
    }

    fun updateTodo(todo: Todo) {
        val call = todosService.updateById(todo.id, UpdateTodo(todo.text, todo.completed))
        val callback = object : Callback<Todo> {
            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                if (response.isSuccessful) {
                    val updatedTodo = response.body()!!
                    val newList = _todos.value?.toMutableList()
                    val idx = newList!!.indexOfFirst { it.id == todo.id }
                    newList[idx] = updatedTodo
                    _todos.value = newList.toList()
                    _error.value = null
                } else {
                    _error.value = response.errorBody()?.string()?:"";
                }
            }

            override fun onFailure(call: Call<Todo>, t: Throwable) {
                _error.value = "Updating todo failed";
            }

        }
        call.enqueue(callback)
    }

    fun updateTodoObs(todo: Todo) {
        todosService.updateByIdObs(todo.id, UpdateTodo(todo.text, todo.completed))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    val todo = response.body()!!
                    val newList = _todos.value?.toMutableList()
                    val idx = newList!!.indexOfFirst { it.id == todo.id }
                    newList[idx] = todo
                    _todos.value = newList.toList()
                    _error.value = null
                } else {
                    val error = response.errorBody()?.string() ?: ""
                    _error.value = error
                }
            }, {
                _error.value = "Loading todos failed"
            })
    }

    fun removeTodo(todo: Todo) {
        val call = todosService.deleteById(todo.id)
        val callback = object : Callback<Response<Void>> {
            override fun onResponse(
                call: Call<Response<Void>>,
                response: Response<Response<Void>>
            ) {
                if (response.isSuccessful) {
                    val newList = _todos.value?.toMutableList()
                    newList?.removeAll { it.id == todo.id }
                    _todos.value = newList?.toList()
                } else {
                    _error.value = "Removed todo failed";
                }
            }

            override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                _error.value = "Removed todo failed";
            }
        }
        call.enqueue(callback)
    }
}

