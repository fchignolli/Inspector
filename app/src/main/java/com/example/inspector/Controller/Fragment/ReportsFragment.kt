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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_my_reports, container, false)
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
}
