package com.damianogiusti.recyclerviewdatasource

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by Damiano Giusti on 19/10/17.
 */
abstract class RecyclerViewDataSource<Item, Cell : RecyclerView.ViewHolder>
    : DisplayableDatasource<Item, Cell, RecyclerView>, RecyclerView.Adapter<Cell>() {

    internal var item: ((position: Int) -> Item)? = null

    @CallSuper
    override fun onNext(t: List<Item>) {
        itemCount = { t.size }
        item = { t[it] }
    }

    override fun onError(e: Throwable) {
        throw e
    }

    override fun onComplete() {}

    override final fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Cell? {
        return (parent as? RecyclerView)?.let {
            createCell?.invoke(it, viewType)
        }
    }

    override final fun getItemCount(): Int {
        return itemCount()
    }

    override final fun onBindViewHolder(holder: Cell, position: Int) {
        item?.invoke(position)?.let {
            configureCell?.invoke(it, holder)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        item = null
        itemCount = { 0 }
        createCell = null
        configureCell = null
    }

    override var createCell: ((parent: RecyclerView, itemType: Int) -> Cell)? = null

    override var configureCell: ((item: Item, viewHolder: Cell) -> Unit)? = null

    ///////////////////////////////////////////////////////////////////////////
    // Private properties
    ///////////////////////////////////////////////////////////////////////////

    private var itemCount: (() -> Int) = { 0 }
}