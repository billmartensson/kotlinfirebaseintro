package se.magictechnology.myfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


@IgnoreExtraProperties
data class Todothing(
    var todotitle: String? = "",
    var done: Boolean? = false
)


class MainActivity : AppCompatActivity() {

    var todoadapter = Tododapter()

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoRecview.layoutManager = LinearLayoutManager(this)
        todoRecview.adapter = todoadapter

        database = Firebase.database.reference

        /*
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("ABC")



        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                Log.d("BILLDEBUG", "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("BILLDEBUG", "Failed to read value.", error.toException())
            }
        })
        */


        todoBtn.setOnClickListener {
            var thingtodo = Todothing(todoET.text.toString(), false)

            database.child("todo").push().setValue(thingtodo)

            loadTodo()
        }


        loadTodo()



    }


    fun loadTodo()
    {
        todoadapter.todolist.clear()

        val todoListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                for (todochild in dataSnapshot.children)
                {
                    val todo = todochild.getValue<Todothing>()

                    todoadapter.todolist.add(todo!!)

                    Log.i("BILLDEBUG", todo!!.todotitle)
                }

                todoadapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("BILLDEBUG", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        database.child("todo").addListenerForSingleValueEvent(todoListener)
    }


}