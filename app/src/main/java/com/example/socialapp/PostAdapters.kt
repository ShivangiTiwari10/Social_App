package com.example.socialapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.model.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapters(options: FirestoreRecyclerOptions<Post>, val listner: IPostAdapter) :
    FirestoreRecyclerAdapter<Post, PostAdapters.PostViewHolder>(options) {



    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.postTitle
        val userText: TextView = itemView.userName
        val createdAt: TextView = itemView.createdAt
        val likeCount: TextView = itemView.likeCount
        val userImage: ImageView = itemView.userImage
        val likeButton: ImageView = itemView.likeButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewHolder = PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
        viewHolder.likeButton.setOnClickListener {
            listner.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.postText.text = model.text
        holder.userText.text = model.createdBy.displayName
        Glide.with(holder.userImage.context).load(model.createdBy.imageUrl).circleCrop()
            .into(holder.userImage)
        holder.likeCount.text = model.likedBy.size.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)
        if (isLiked) {
            holder.likeButton.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.likeButton.context,
                    R.drawable.ic_licked
                )
            )
        } else {
            holder.likeButton.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.likeButton.context,
                    R.drawable.ic_unlicked
                )
            )
        }


    }


}

interface IPostAdapter {
    fun onLikeClicked(postId: String)
}