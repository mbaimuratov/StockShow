package com.example.android.stockshow.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.stockshow.databinding.QueryItemBinding

class RequestsAdapter(queryList: List<String>, private val onClickListener: OnQueryClickListener) : RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {

    var list: MutableList<String> = queryList as MutableList<String>

    fun appendQuery(query: String) {
        list.add(query)
        notifyItemInserted(list.size - 1)
    }

    inner class ViewHolder(private val binding: QueryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(s: String) {
            binding.queryTextTv.text = s
            binding.queryTextTv.setOnClickListener {
                onClickListener.onQueryClick(s)
            }
        }
    }

    interface OnQueryClickListener {
        fun onQueryClick(query: String)
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
