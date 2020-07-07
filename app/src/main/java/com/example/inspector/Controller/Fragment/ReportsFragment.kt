package com.example.inspector.Controller.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.inspector.Controller.Adapter.ControlListAdapter
import com.example.inspector.Controller.Adapter.TabsAdapter
import com.example.inspector.Model.Control
import com.example.inspector.R
import com.example.inspector.Utils.Utils
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ReportsFragment : Fragment() {
    val TAG = "ReportsFragment"
    // Firebase pre sets
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var collectionControl: CollectionReference
    private lateinit var collectionRoom: CollectionReference
    private lateinit var collectionTool: CollectionReference
    //Layout
    private lateinit var tabLayout: TabLayout

    // Listas de modelo para preencher a recyclerView
    private val controlList: ArrayList<Control> = ArrayList()

    private var controlAdapter: ControlListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_my_reports, container, false)
        mDatabase = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val viewPager: ViewPager = root.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = root.findViewById(R.id.tabLayout)

        val tabsAdapter = TabsAdapter(childFragmentManager)
        tabsAdapter.addFragment(ControlListFragment(), "Controle")
        tabsAdapter.addFragment(RoomListFragment(), "Salas")
        tabsAdapter.addFragment(ToolListFragment(), "Instrumentos")

        viewPager.adapter = tabsAdapter
        tabLayout.setupWithViewPager(viewPager)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        return root
    }

    override fun onStart() {
        super.onStart()
        //getAllControlForm()
        //loadControlList()
        //setControlAdapter()
        //controlAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
//        if (controlAdapter != null) {
//            controlAdapter!!.stopListening()
//        }
    }

//    private fun setupRecyclerView() {
//        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//        recyclerView.setHasFixedSize(true)
//    }
//
//    private fun setControlAdapter() {
//        recyclerView.adapter = controlAdapter
//    }
//
//    private fun loadControlList() {
//        val query = collectionControl.orderBy("date", Query.Direction.ASCENDING)
//        val options = FirestoreRecyclerOptions.Builder<Control>().setQuery(query, Control::class.java).build()
//        controlAdapter = ControlListAdapter(options, context!!)
//    }

//    fun setupActsList(acts: List<ActEntity>) {
//        reportRecyclerView.adapter = ActListAdapter(acts, this)
//
//    }
//
//    fun setupPositivesList(positives: List<PositiveEntity>) {
//        reportRecyclerView.adapter = PositiveListAdapter(positives, this)
//    }

    private fun setupCollections() {
        collectionControl = mDatabase.collection("users").document(auth.currentUser?.uid.toString()).collection("controlForm")
        collectionRoom = mDatabase.collection("users").document(auth.currentUser?.uid.toString()).collection("roomForm")
        collectionTool = mDatabase.collection("users").document(auth.currentUser?.uid.toString()).collection("toolForm")
    }

}
