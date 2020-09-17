package se.magictechnology.myfirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todo_item.view.*

class Tododapter() : RecyclerView.Adapter<TodoViewHolder>() {

    var todolist = mutableListOf<Todothing>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val vh = TodoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        return todolist.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.todoText.text = todolist[position].todotitle
    }

}

class TodoViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    var todoText = view.todoTV

}