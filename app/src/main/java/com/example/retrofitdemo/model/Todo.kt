package com.example.retrofitdemo.model

data class Todo(val id: String?, val description: String, val deadline: String)

data class UpdateTodo(val description: String?, val deadline: String?)

data class TodoList(val items: ArrayList<Todo>)
