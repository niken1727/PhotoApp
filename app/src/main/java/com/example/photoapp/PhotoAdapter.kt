package com.example.photoapp

import android.content.Context
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photoapp.databinding.ItemPhotoBinding


class InternalStoragePhotoAdapter(
    val context: Context
) : ListAdapter<TriangleGallery, InternalStoragePhotoAdapter.PhotoViewHolder>(Companion) {

    inner class PhotoViewHolder(val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<TriangleGallery>() {
        override fun areItemsTheSame(oldItem: TriangleGallery, newItem: TriangleGallery): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TriangleGallery, newItem: TriangleGallery): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = currentList[position]
        holder.binding.apply {
            Glide.with(context).load(photo.uri).into(ivPhoto)
            tvIndex.text = photo.index.toString()

        }
    }
}