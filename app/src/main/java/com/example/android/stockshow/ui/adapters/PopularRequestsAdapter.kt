package com.example.android.stockshow.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.stockshow.databinding.QueryItemBinding

class PopularRequestsAdapter(queryList: List<String>, private val onClickListener: View.OnClickListener) : RecyclerView.Adapter<PopularRequestsAdapter.ViewHolder>() {

    private var list: List<String> = queryList

    inner class ViewHolder(private val binding: QueryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(s: String) {
            binding.queryTextTv.text = s

            binding.queryTextTv.setOnClickListener(onClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QueryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

}
