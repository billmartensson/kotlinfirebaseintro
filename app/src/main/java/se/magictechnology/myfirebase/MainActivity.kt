package se.magictechnology.myfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


@IgnoreExtraProperties
data class Todothing(
    var fbkey: String? = null,
    var todotitle: String? = "",
    var done: Boolean? = false
)


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    var todoadapter = Tododapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoRecview.layoutManager = LinearLayoutManager(this)
        todoRecview.adapter = todoadapter

        todoadapter.database = Firebase.database.reference
        todoadapter.auth = Firebase.auth
        auth = Firebase.auth

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
            //var thingtodo = Todothing(todoET.text.toString(), false)

            var thingtodo = Todothing(todotitle = todoET.text.toString(), done = false)

            todoadapter.database.child("todousers").child(auth.currentUser!!.uid).push().setValue(thingtodo)

            todoET.setText("")
            todoadapter.loadTodo()
        }

        logoutBtn.setOnClickListener {
            auth.signOut()

            val intent = Intent(this, LoginRegisterActivity::class.java)

            startActivity(intent)
        }




    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser == null)
        {
            val intent = Intent(this, LoginRegisterActivity::class.java)

            startActivity(intent)
        } else {

            Log.i("BILLDEBUG", "USER ID IS " + auth.currentUser!!.uid)

            todoadapter.loadTodo()
        }



    }



}