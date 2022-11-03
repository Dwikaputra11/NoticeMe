package com.example.noticeme.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.noticeme.adapter.OnBoardAdapter
import com.example.noticeme.databinding.FragmentOnBoardingBinding
import com.example.noticeme.helper.DummyData

class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private lateinit var onBoardingAdapter: OnBoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBoardingAdapter = OnBoardAdapter(DummyData.onBoardingItem)
        binding.vpOnBoarding.orientation =ViewPager2.ORIENTATION_HORIZONTAL
        binding.vpOnBoarding.offscreenPageLimit = 3
        binding.vpOnBoarding.adapter = onBoardingAdapter
        binding.dotsIndicator.attachTo(binding.vpOnBoarding)
    }
}