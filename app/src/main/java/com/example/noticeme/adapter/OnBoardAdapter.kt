package com.example.noticeme.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.noticeme.R
import com.example.noticeme.model.OnBoarding
import com.example.noticeme.databinding.OnBoardingItemBinding
import com.example.noticeme.sharedpref.SharedPref

class OnBoardAdapter(private var onBoardList: ArrayList<OnBoarding>): RecyclerView.Adapter<OnBoardAdapter.ViewHolder>() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var context: Context

    class ViewHolder(var binding: OnBoardingItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OnBoardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvOnBoardTitle.text = onBoardList[position].title
        holder.binding.tvOnBoardDesc.text = onBoardList[position].desc
        holder.binding.ivImage.setImageResource(onBoardList[position].img)
        if(position != onBoardList.lastIndex){
            holder.binding.btnOnBoard.visibility = View.INVISIBLE
        }else{
            sharedPref = context.getSharedPreferences(SharedPref.name, Context.MODE_PRIVATE)
            holder.binding.btnOnBoard.visibility = View.VISIBLE
            holder.binding.btnOnBoard.setOnClickListener {
                // if user click next button, they cannot see the on boarding page anymore
                val firstInstall = sharedPref.edit()
                firstInstall.putBoolean(SharedPref.firstInstall, false)
                firstInstall.apply()
                Navigation.findNavController(holder.binding.root).navigate(R.id.action_onBoardingFragment_to_loginFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return onBoardList.size
    }
}