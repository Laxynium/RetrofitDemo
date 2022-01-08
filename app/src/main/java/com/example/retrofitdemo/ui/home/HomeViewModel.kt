package com.example.retrofitdemo.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitdemo.RetroInstance
import com.example.retrofitdemo.adapter.TodoAdapter
import com.example.retrofitdemo.model.Todo
import com.example.retrofitdemo.model.TodoList
import com.example.retrofitdemo.service.TodosService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeViewModel: ViewModel() {

    private val todosService: TodosService = RetroInstance.getRetroInstance().create(TodosService::class.java)

    var recyclerListData: MutableLiveData<TodoList> = MutableLiveData()
    var recyclerViewAdapter : TodoAdapter = TodoAdapter()

    fun getAdapter(): TodoAdapter {
        return recyclerViewAdapter
    }

    fun setAdapterData(data: ArrayList<Todo>) {
        recyclerViewAdapter.setDataList(data)
        recyclerViewAdapter.notifyDataSetChanged()
    }

    fun getRecyclerListDataObserver(): MutableLiveData<TodoList> {
        return recyclerListData
    }

    fun makeAPICall() {
        val call = todosService.getAll(10,0)
        call.enqueue(object : Callback<List<Todo>>{
            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                recyclerListData.postValue(null)
            }

            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                if(response.isSuccessful){
                    recyclerListData.postValue(TodoList(ArrayList(response.body())))
                } else {
                    recyclerListData.postValue(null)
                }
            }
        })
    }
}
