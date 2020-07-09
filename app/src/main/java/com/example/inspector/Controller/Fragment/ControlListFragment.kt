package com.example.inspector.Controller.Fragment

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inspector.Controller.Adapter.ControlListAdapter
import com.example.inspector.Model.Control
import com.example.inspector.R
import com.example.inspector.Utils.Utils
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class ControlListFragment : Fragment() {
    private val TAG = "ControlListFragment"
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var collectionControl: CollectionReference
    private lateinit var controlAdapter: ControlListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabase = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        collectionControl = mDatabase.collection("users").document(auth.currentUser?.uid.toString()).collection("control_form")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_control_list, container, false)
        recyclerView = root.findViewById(R.id.recyclerView)
        setupRecyclerView()

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if(direction == ItemTouchHelper.LEFT) {
                        deleteDocument(viewHolder.adapterPosition)
                    }
                }

                override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                    RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(context!!, R.color.red))
                        .addActionIcon(R.drawable.ic_delete_white_24dp)
                        .create()
                        .decorate()
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return root
    }

    override fun onStart() {
        super.onStart()
        loadControlList()
        controlAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        controlAdapter.stopListening()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
    }

    private fun loadControlList() {
        val query = collectionControl.orderBy("date", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Control>().setQuery(query, Control::class.java).build()
        controlAdapter = ControlListAdapter(options, context!!)
        recyclerView.adapter = controlAdapter
    }

    private fun deleteDocument(position: Int) {
        val control = controlAdapter.getItem(position)
        val documentReference = controlAdapter.snapshots.getSnapshot(position).reference
        documentReference.delete()
            .addOnSuccessListener {
                Log.d(TAG, "Item ${control.placeName} deletado com sucesso!" )
            }

        Snackbar.make(recyclerView, "Item deletado", Snackbar.LENGTH_LONG)
            .setAction("Defazer", View.OnClickListener {
                documentReference.set(control)
            })
            .show()
    }
}

