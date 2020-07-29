package com.example.inspector.Controller.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.inspector.Model.Tool
import com.example.inspector.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_default_form.view.*

/**
 * Created by Lucas Alves dos Santos on 29/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
class ToolListAdapter(options: FirestoreRecyclerOptions<Tool>, private val context: Context): FirestoreRecyclerAdapter<Tool, ToolListAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_default_form, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Tool) {
        holder.let { item ->
            item.bindView(model)
            item.itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("tool", model)
                it.findNavController().navigate(R.id.action_nav_my_reports_to_toolDetailsFragment, bundle)
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(tool: Tool){
            val placeName = itemView.placleNameTextView
            val name = itemView.nameTextView
            val date = itemView.dateTextView

            placeName.text = tool.placeName
            name.text = tool.inspectedName
            date.text = tool.getUserDate()
        }
    }
}