package com.example.inspector.Controller.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inspector.Model.Image
import com.example.inspector.R
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.recycler_image_item.view.*

/**
 * Created by Lucas Alves dos Santos on 08/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */

class FormImageAdapter(
    private val imageList: ArrayList<Image>,
    private val storageReferences: ArrayList<StorageReference>,
    private val context: Context?,
    private val canRemove: Boolean = true
) : RecyclerView.Adapter<FormImageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_image_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.let { item ->
            item.bindView(imageList[position])
            if(canRemove) {
                item.itemView.removeimageButton.setOnClickListener {
                    removeImageAtPosition(position)
                }
            } else {
                item.itemView.removeimageButton.visibility = View.GONE
            }
        }
    }

    private fun removeImageAtPosition(position: Int) {
        storageReferences[position].delete()
        imageList.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(image: Image) {
            Glide
                .with(context!!)
                .load(image.uriString)
                .placeholder(R.drawable.loading_spinner)
                .error(R.drawable.error_image)
                .into(itemView.imageView)
        }
    }
}