package se.magictechnology.myfirebase

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.todo_item.view.*

class Tododapter() : RecyclerView.Adapter<TodoViewHolder>() {

    lateinit var parentActivity : MainActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val vh = TodoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false))
        return vh
    }

    override fun getItemCount(): Int {
        parentActivity.viewModel.todolist.value?.let {
            return it.size
        }

        return 0
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.todoText.text = parentActivity.viewModel.todolist.value!![position].todotitle

        if(parentActivity.viewModel.todolist.value!![position].done == true)
        {
            holder.todoDone.setBackgroundColor(Color.GREEN)
        } else {
            holder.todoDone.setBackgroundColor(Color.RED)
        }

        holder.itemView.setOnClickListener {
            parentActivity.viewModel.changeDone(position)
        }
    }





}

class TodoViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    var todoText = view.todoTV
    var todoDone = view.todoDoneCL

}