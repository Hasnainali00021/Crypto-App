package com.example.cryptoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.databinding.RvItemBinding

class RvAdapter(val context: Context, var data:ArrayList<Modal>):
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    fun changeData(filterdata: ArrayList<Modal>) {
        data = filterdata
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: RvItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {   // jo next next banta ja raha rv_item
        val view = RvItemBinding.inflate(LayoutInflater.from(context),parent,false)   // IMP Line
        return ViewHolder(view)      // For new ViewHoder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setAnimation(holder.itemView)
        holder.binding.name.text = data[position].name
        holder.binding.symbol.text = data[position].symbol
        holder.binding.price.text = data[position].price
    }

    override fun getItemCount(): Int {
        return data.size
    }
    fun setAnimation(view: View){
        val anim = AlphaAnimation(0.0f ,1.0f)
        anim.duration=1000
        view.startAnimation(anim)
    }
}