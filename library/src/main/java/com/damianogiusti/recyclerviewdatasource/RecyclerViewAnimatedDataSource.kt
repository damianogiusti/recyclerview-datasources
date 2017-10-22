package com.damianogiusti.recyclerviewdatasource

import android.support.v7.widget.RecyclerView
import android.util.Log

/**
 * Created by Damiano Giusti on 19/10/17.
 */
class RecyclerViewAnimatedDataSource<Item, Cell : RecyclerView.ViewHolder>
    : RecyclerViewDataSource<Item, Cell>() {

    var itemId: ((item: Item) -> Long)? = null

    init {
        setHasStableIds(true)
    }

    override fun onNext(t: List<Item>) {
        super.onNext(t)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return item?.invoke(position)?.let {
            itemId?.invoke(it)
        } ?: {
            Log.w("RecyclerViewAnimatedDataSource", "You have to implement the itemId" +
                    " closure to get the animations working!")
            super.getItemId(position)
        }()
    }
}