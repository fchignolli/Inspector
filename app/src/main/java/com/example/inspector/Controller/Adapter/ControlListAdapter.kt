package com.example.inspector.Controller.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.inspector.Model.Control
import com.example.inspector.R
import com.example.inspector.Utils.Utils
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_default_form.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lucas Alves dos Santos on 07/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
class ControlListAdapter(options: FirestoreRecyclerOptions<Control>, private val context: Context): FirestoreRecyclerAdapter<Control, ControlListAdapter.ViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_default_form, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Control) {
        holder.let {
            it.bindView(model)
            it.itemView.setOnClickListener {
                Utils.alert("abrir details")
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(control: Control){
            val placeName = itemView.placleNameTextView
            val name = itemView.nameTextView
            val date = itemView.dateTextView

            placeName.text = control.placeName
            name.text = control.inspectedName
            date.text = control.getUserDate()
        }
    }

}