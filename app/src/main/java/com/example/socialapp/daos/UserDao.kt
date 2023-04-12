package com.example.socialapp.daos

import android.util.Log
import com.example.socialapp.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UserDao {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun addUser(user: User?) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                usersCollection.document(user.uid).set(it)
            }
        }
        Log.d("2.ADDED users","ADDED")
    }

    fun
            getUserById(uId: String): Task<DocumentSnapshot> {
        return usersCollection.document(uId).get()
    }

}