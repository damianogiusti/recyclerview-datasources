package com.damianogiusti.recyclerviewdatasource

import android.support.v7.widget.RecyclerView

/**
 * Created by Damiano Giusti on 19/10/17.
 */
class RecyclerViewReloadDataSource<Item, Cell : RecyclerView.ViewHolder>: RecyclerViewDataSource<Item, Cell>() {

    override fun onNext(t: List<Item>) {
        super.onNext(t)
        notifyDataSetChanged()
    }
}