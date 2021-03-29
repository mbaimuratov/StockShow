package com.example.android.stockshow.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.android.stockshow.R
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.StockItemBinding
import kotlin.math.abs


class StocksAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<StocksAdapter.StockViewHolder>() {

    private var stockList = mutableListOf<StockItem>()

    private var stockListFull = mutableListOf<StockItem>()

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
                    .load(stockItem.logo.trim())
                    .placeholder(R.drawable.ic_image)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(40)))
                    .into(binding.logoIv)

                //calculate day delta and percent rise
                val stockDelta = stockItem.latestPrice - stockItem.previousClose
                val stockRisePercent = abs(stockDelta) / stockItem.previousClose * 100
                if (stockDelta < 0) {
                    dayDeltaTv.setTextColor(Color.RED)
                    dayDeltaTv.text = "-$%.2f (%.2f".format(-stockDelta, stockRisePercent) + "%)"
                } else {
                    dayDeltaTv.text = "+$%.2f (%.2f".format(stockDelta, stockRisePercent) + "%)"
                    dayDeltaTv.setTextColor(Color.GREEN)
                }

                //make zebra pattern
                if (position % 2 == 0) {
                    root.setCardBackgroundColor(Color.parseColor("#F0F4F7"))
                    root.radius = 70f
                } else {
                    root.setCardBackgroundColor(Color.parseColor("#ffffff"))
                }

                //color of the star
                if (stockItem.isFavourite) {
                    addFavouriteStarIv.setImageResource(R.drawable.ic_yellow_star)
                } else {
                    addFavouriteStarIv.setImageResource(R.drawable.ic_grey_star)
                }

                addFavouriteStarIv.setOnClickListener {
                    listener.onStarClick(stockList[position])
                }

                binding.root.setOnClickListener {
                    listener.onItemClick(stockItem)
                }
            }
        }
    }

    fun submitList(stockList: List<StockItem>) {
        this.stockList = stockList as MutableList<StockItem>
        this.stockListFull = ArrayList(this.stockList)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onStarClick(stockItem: StockItem)
        fun onItemClick(stockItem: StockItem)
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

    companion object DiffCallback : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.ticker == newItem.ticker
        }
    }
}