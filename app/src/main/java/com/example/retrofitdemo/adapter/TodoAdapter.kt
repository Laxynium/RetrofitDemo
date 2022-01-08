//package com.example.retrofitdemo.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.retrofitdemo.R
//import com.example.retrofitdemo.model.Todo
//import kotlinx.android.synthetic.main.todo_list_item.view.*
//
//
//class TodoAdapter(var todosList: ArrayList<Todo> = ArrayList()): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
//
//    fun setDataList(data :  ArrayList<Todo>) {
//        this.todosList = data
//    }
//
//    class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        val descriptionField: TextView = itemView.text_view_1
//        val deadlineField: TextView = itemView.text_view_2
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
//        val itemView = LayoutInflater
//            .from(parent.context)
//            .inflate(R.layout.todo_list_item, parent, false)
//
//        return TodoViewHolder(itemView)
//    }
//
//    override fun getItemCount() = todosList.size
//
//    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
//        val currentItem = todosList[position]
//
//        holder.descriptionField.text = currentItem.description
//        holder.deadlineField.text = currentItem.deadline
//    }
//
//}
