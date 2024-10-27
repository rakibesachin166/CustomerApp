package com.dev.customerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.customerapp.R
import com.dev.customerapp.adapter.SliderImageAdapter
import com.dev.customerapp.databinding.FragmentCreateUserFormBinding
import com.dev.customerapp.databinding.FragmentHomeBinding
import com.dev.customerapp.models.BannerModel
import com.smarteist.autoimageslider.SliderView


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bannerList = listOf(
            BannerModel("https://cdn.pixabay.com/photo/2023/10/20/11/14/diya-8329247_640.jpg"),
            BannerModel("https://img.freepik.com/free-photo/chicken-wings-barbecue-sweetly-sour-sauce-picnic-summer-menu-tasty-food-top-view-flat-lay_2829-6471.jpg")
        )


        val sliderAdapter = SliderImageAdapter(bannerList)
        with(binding.slider) {
            setSliderAdapter(sliderAdapter)
            setScrollTimeInSec(3)
            setAutoCycle(true)
            startAutoCycle()
        }
    }
}