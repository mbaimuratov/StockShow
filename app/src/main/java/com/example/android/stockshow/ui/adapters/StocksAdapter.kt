package com.example.android.stockshow.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.stockshow.R
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.StockItemBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class StocksAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<StocksAdapter.StockViewHolder>(), Filterable{
//class StocksAdapter(private val listener: OnItemClickListener) :
//    ListAdapter<StockItem, StocksAdapter.StockViewHolder>(FavouriteAdapter),
    private var stockList = mutableListOf<StockItem>()

    private var stockListFull = mutableListOf<StockItem>()

//    fun appendStock(stockItem: StockItem) {
//        currentList.add(stockItem)
//        notifyItemInserted(itemCount - 1)
//    }

    inner class StockViewHolder(private val binding: StockItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        @Synchronized
        fun bind(stockItem: StockItem, position: Int) {
            binding.run {
                //company name
                companyNameTv.text = stockItem.name

                //company ticker
                tickerTv.text = stockItem.ticker

                //current price
                currentPriceTv.text = "$%.2f ".format(stockItem.latestPrice)

                //load logo
                Glide.with(binding.logoIv.context)
                    .load(stockItem.logo)
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .into(binding.logoIv)

                //calculate day delta and percent rise
                val stockDelta = stockItem.latestPrice - stockItem.previousClose
                val stockRisePercent = abs(stockDelta) / stockItem.previousClose * 100
                if (stockDelta < 0) {
                    dayDeltaTv.setTextColor(Color.RED)
                    dayDeltaTv.text = "-%.2f (%.2f".format(stockDelta, stockRisePercent) + "%)"
                } else {
                    dayDeltaTv.text = "+%.2f (%.2f".format(stockDelta, stockRisePercent) + "%)"
                    dayDeltaTv.setTextColor(Color.GREEN)
                }

                //make zebra pattern
                if (position % 2 == 0) {
                    root.setBackgroundColor(Color.parseColor("#F0F4F7"))
                } else {
                    root.setBackgroundColor(Color.parseColor("#ffffff"))
                }

                //color of the star
                if (stockItem.isFavourite) {
                    addFavouriteStarIv.setImageResource(R.drawable.ic_baseline_yellow_star_24)
                } else {
                    addFavouriteStarIv.setImageResource(R.drawable.ic_baseline_grey_star_24)
                }

                addFavouriteStarIv.setOnClickListener {
                    listener.onStarClick(stockList[position])
                    notifyItemChanged(position)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onStarClick(stockItem: StockItem)
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
//        val binding = StockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return StockViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
//        val stock = getItem(position)
//        holder.bind(stock, position)
//    }

    companion object DiffCallback : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.ticker == newItem.ticker
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filteredList = arrayListOf<StockItem>()
                filteredList.addAll(stockListFull)
                if (!constraint.isNullOrBlank()) {
                    val filterPattern = constraint.toString().toUpperCase(Locale.ROOT).trim()

                    filteredList = stockListFull.filter { stockItem ->
                        stockItem.name.toUpperCase(Locale.ROOT).contains(filterPattern)
                                || stockItem.ticker.contains(filterPattern)
                    } as ArrayList<StockItem>
                }

                Log.i("performFiltering", filteredList.toString())

                val result = FilterResults()
                result.values = filteredList
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                if (results.values != null) {
                    stockList.clear()
                    stockList.addAll(results.values as List<StockItem>)
                    //Log.i("publishResults", results.values.toString())
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = StockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stockList[position]
        holder.bind(stock, position)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    fun submitList(stockList: List<StockItem>) {
        this.stockList = stockList as MutableList<StockItem>
        this.stockListFull = ArrayList(this.stockList)
        notifyDataSetChanged()
    }
}