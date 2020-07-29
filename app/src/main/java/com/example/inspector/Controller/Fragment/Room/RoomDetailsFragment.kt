package com.example.inspector.Controller.Fragment.Room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inspector.R

/**
 * Created by Lucas Alves dos Santos on 29/07/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
class RoomDetailsFragment : Fragment() {
    private val TAG = "RoomDetailsFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_details, container, false)
    }

}
