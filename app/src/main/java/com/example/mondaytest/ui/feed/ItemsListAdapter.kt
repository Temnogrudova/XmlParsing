package com.example.mondaytest.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mondaytest.R
import com.example.mondaytest.models.Article

class ItemsListAdapter(
        private var items: List<Article?>,
        private val interactionListener: InteractionListener
) : RecyclerView.Adapter<ItemsListAdapter.BaseViewHolder>() {

    interface InteractionListener {
        fun onItemClick(v: View, feed: Article?)
    }
    fun setData(items: List<Article?>) {
        this.items = items
        notifyDataSetChanged()
    }

    private lateinit var inflater: LayoutInflater
    abstract class BaseViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class ItemViewHolder (itemView: View) : BaseViewHolder(itemView) {
        var mTitle: TextView = itemView.findViewById(R.id.title)

        fun bind(item: Article?) {
            item?.title?.let {
                mTitle.text = it
            }
            itemView.setOnClickListener { view ->
                 interactionListener.onItemClick(view, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(parent.context)

        val view: View = inflater.inflate(R.layout.item, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(items[position])
    }
}