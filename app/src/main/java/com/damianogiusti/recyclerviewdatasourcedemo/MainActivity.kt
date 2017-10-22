package com.damianogiusti.recyclerviewdatasourcedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.damianogiusti.recyclerviewdatasource.RecyclerViewAnimatedDataSource
import com.damianogiusti.recyclerviewdatasource.bind
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        val datasource = RecyclerViewAnimatedDataSource<NumberItem, NumberItemViewHolder>()

        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = datasource.apply {

            createCell = { parent: RecyclerView, itemType: Int ->
                val view = LayoutInflater.from(parent.context).inflate(android.R.layout.test_list_item, parent, false)
                NumberItemViewHolder(view)
            }

            configureCell = { item: NumberItem, viewHolder: NumberItemViewHolder ->
                viewHolder.textView.text = item.toString()
            }

            itemId = {
                it.number.toLong()
            }
        }

        val d = Observable.interval(0, 1, TimeUnit.SECONDS)
                .map { NumberItem(it.toInt()) }
                .scan(mutableListOf<NumberItem>(), { acc, x ->
                    acc.add(x)
                    acc
                })
                .map { it.toList() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { recyclerView.scrollToPosition(it.size - 1) }
                .bind(datasource)

        compositeDisposable.add(d)
    }

    override fun onDestroy() {
        super.onDestroy()
//        compositeDisposable.clear()
    }
}

data class NumberItem(val number: Int)

class NumberItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textView: TextView

    init {
        textView = itemView.findViewById(android.R.id.text1)
    }
}