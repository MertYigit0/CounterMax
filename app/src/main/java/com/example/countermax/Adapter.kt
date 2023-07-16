package com.example.countermax

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countermax.databinding.RecyclerRowBinding

class Adapter(private val counterList: ArrayList<Counter>) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

    class MyViewHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.incrementButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    incrementButtonClicked(position)
                }
            }

            binding.decrementButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    decrementButtonClicked(position)
                }
            }
        }

        private fun incrementButtonClicked(position: Int) {
       
        }

        private fun decrementButtonClicked(position: Int) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerRowBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val counter = counterList[position]
        holder.binding.counterText.text = counter.name
        holder.binding.counter.text = counter.count.toString()
    }

    override fun getItemCount(): Int {
        return counterList.size
    }
}
