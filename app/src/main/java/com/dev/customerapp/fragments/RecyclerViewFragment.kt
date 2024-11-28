package com.dev.customerapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.databinding.FragmentRecyclerViewBinding


class RecyclerViewFragment : BaseFragment() {

    private lateinit var binding: FragmentRecyclerViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      binding = FragmentRecyclerViewBinding.inflate(inflater, container,false  )
        return binding.root
    }

}