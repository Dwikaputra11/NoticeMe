package com.example.noticeme.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.noticeme.R
import com.example.noticeme.data.OnBoarding
import com.example.noticeme.databinding.FragmentOnBoardingBinding
import com.example.noticeme.databinding.OnBoardingItemBinding
import com.example.noticeme.ui.OnBoardingFragment

class OnBoardAdapter(private var onBoardList: ArrayList<OnBoarding>): RecyclerView.Adapter<OnBoardAdapter.ViewHolder>() {

    class ViewHolder(var binding: OnBoardingItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OnBoardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvOnBoardTitle.text = onBoardList[position].title
        holder.binding.tvOnBoardDesc.text = onBoardList[position].desc
        holder.binding.ivImage.setImageResource(onBoardList[position].img)
        if(position != onBoardList.lastIndex){
            holder.binding.btnOnBoard.visibility = View.GONE
        }else{
            holder.binding.btnOnBoard.visibility = View.VISIBLE
            holder.binding.btnOnBoard.setOnClickListener {
                Navigation.findNavController(holder.binding.root).navigate(R.id.action_onBoardingFragment_to_loginFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return onBoardList.size
    }
}