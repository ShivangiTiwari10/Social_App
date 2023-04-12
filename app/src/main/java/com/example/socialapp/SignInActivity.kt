package com.example.socialapp


import android.app.Person
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.daos.PostDao
import com.example.socialapp.databinding.ActivitySignInBinding
import com.example.socialapp.model.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignInActivity : AppCompatActivity(), IPostAdapter {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var postDao: PostDao
    private lateinit var adapter: PostAdapters
    private lateinit var user: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)

        setContentView(binding.root)
        user = Firebase.auth

        fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
        val adContentDialoag = AlertDialog.Builder(this)

            .setMessage("Do You want to Log out")
            .setIcon(R.drawable.ic_add_logout)

            .setPositiveButton("yess") { _, _ ->
                user.signOut()
                startActivity(Intent(this, MainActivity::class.java))

                Toast.makeText(this, "YOU loggedOut", Toast.LENGTH_LONG).show()
            }

            .setNegativeButton("No") { _, _ ->
                Toast.makeText(this, "YOU did not loggedOut", Toast.LENGTH_LONG).show()
            }.create()

        btnLogOut.setOnClickListener {

            adContentDialoag.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    private fun deletePerson(person: Person) =
        CoroutineScope(Dispatchers.IO).launch {

        }


    private fun setUpRecyclerView() {
        postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapters(recyclerViewOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        Log.d("Recycler", "How it is working")


    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
//        Log.d("START", "${startActivity(intent)}")
    }


    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }

}
