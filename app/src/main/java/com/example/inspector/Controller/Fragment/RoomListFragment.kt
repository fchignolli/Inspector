package com.example.inspector.Controller.Fragment

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inspector.Controller.Adapter.ControlListAdapter
import com.example.inspector.Controller.Adapter.RoomListAdapter
import com.example.inspector.Model.Room
import com.example.inspector.R
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class RoomListFragment : Fragment() {
    private val TAG = "RoomListFragment"
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var collectionRoom: CollectionReference
    private lateinit var roomlistAdapter: RoomListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabase = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        collectionRoom = mDatabase.collection("users").document(auth.currentUser?.uid.toString()).collection("room_form")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_room_list, container, false)
        recyclerView = root.findViewById(R.id.roomRecyclerView)
        return root
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        itemTouchEvent()
        loadRoomList()
        roomlistAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        roomlistAdapter.stopListening()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
    }

    private fun loadRoomList() {
        val query = collectionRoom.orderBy("date", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Room>().setQuery(query, Room::class.java).build()
        roomlistAdapter = RoomListAdapter(options, context!!)
        recyclerView.adapter = roomlistAdapter
    }

    private fun itemTouchEvent() {
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
    }

    private fun deleteDocument(position: Int) {
        val control = roomlistAdapter.getItem(position)
        val documentReference = roomlistAdapter.snapshots.getSnapshot(position).reference
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
