package se.magictechnology.myfirebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*

@IgnoreExtraProperties
data class Todothing(
    var fbkey: String? = null,
    var todotitle: String? = "",
    var done: Boolean? = false
)

class MainViewModel : ViewModel()
{
    var database: DatabaseReference = Firebase.database.reference
    var auth: FirebaseAuth = Firebase.auth

    var todolist = MutableLiveData<List<Todothing>>()

    val startBitmap = MutableLiveData<Bitmap>()

    val errorMessage = MutableLiveData<String>()

    val loading = MutableLiveData<Boolean>()

    fun loadTheImage()
    {
        val storage = Firebase.storage
        val storageRef = storage.reference

        val theStorageImageRef = storageRef.child("frog.jpg")

        val ONE_MEGABYTE: Long = 1024 * 1024
        loading.value = true
        theStorageImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { theBytes ->
            // Data for "images/island.jpg" is returned, use this as needed

            val theBitmap = BitmapFactory.decodeByteArray(theBytes, 0, theBytes.size)
            startBitmap.value = theBitmap
            loading.value = false
        }.addOnFailureListener {
            // Handle any errors
            errorMessage.value = "Kunde inte ladda bild"
            loading.value = false
        }
    }

    fun loadTodo()
    {
        val todoListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                var tempTodoList = mutableListOf<Todothing>()
                for (todochild in dataSnapshot.children)
                {
                    val todo = todochild.getValue<Todothing>()
                    todo!!.fbkey = todochild.key
                    tempTodoList.add(todo!!)

                    Log.i("BILLDEBUG", todochild.key)
                    Log.i("BILLDEBUG", todo!!.todotitle)
                }

                todolist.value = tempTodoList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("BILLDEBUG", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        database.child("todousers").child(auth.currentUser!!.uid).orderByChild("done").addListenerForSingleValueEvent(todoListener)
    }

    fun addTodo(title : String)
    {

        var thingtodo = Todothing(todotitle = title, done = false)

        database.child("todousers").child(auth.currentUser!!.uid).push().setValue(thingtodo)

        loadTodo()
    }

    fun changeDone(todonumber : Int)
    {
        if(todolist.value!![todonumber].done == true)
        {
            database.child("todousers").child(auth.currentUser!!.uid).child(todolist.value!![todonumber].fbkey!!).child("done").setValue(false)
        } else {
            database.child("todousers").child(auth.currentUser!!.uid).child(todolist.value!![todonumber].fbkey!!).child("done").setValue(true)
        }

        loadTodo()
    }

}