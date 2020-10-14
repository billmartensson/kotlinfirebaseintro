package se.magictechnology.myfirebase

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    var todoadapter = Tododapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        todoadapter.parentActivity = this
        todoRecview.layoutManager = LinearLayoutManager(this)
        todoRecview.adapter = todoadapter

        viewModel.errorMessage.observe(this, Observer { errtxt ->
            Toast.makeText(this, errtxt, Toast.LENGTH_LONG).show()
        })

        viewModel.loading.observe(this, Observer {
            if(it == true)
            {
                loadingCL.visibility = View.VISIBLE
            } else {
                loadingCL.visibility = View.INVISIBLE
            }
        })

        viewModel.startBitmap.observe(this, Observer { loadedBitmap ->
            todoStartimageView.setImageBitmap(loadedBitmap)
        })
        viewModel.loadTheImage()

        viewModel.todolist.observe(this, Observer {
            todoadapter.notifyDataSetChanged()
        })

        todoStartimageView.setOnClickListener {
            viewModel.loadTheImage()
        }

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
            viewModel.addTodo(todoET.text.toString())
            todoET.setText("")
        }

        logoutBtn.setOnClickListener {
            viewModel.auth.signOut()

            val intent = Intent(this, LoginRegisterActivity::class.java)

            startActivity(intent)
        }




    }

    override fun onStart() {
        super.onStart()

        if(viewModel.auth.currentUser == null)
        {
            val intent = Intent(this, LoginRegisterActivity::class.java)

            startActivity(intent)
        } else {

            Log.i("BILLDEBUG", "USER ID IS " + viewModel.auth.currentUser!!.uid)

            viewModel.loadTodo()
        }



    }



}