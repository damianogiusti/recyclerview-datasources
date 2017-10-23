package com.damianogiusti.recyclerviewdatasource

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by Damiano Giusti on 19/10/17.
 */
interface DisplayableDatasource<Item, Cell, Parent>: Observer<List<Item>> {

    var createCell: ((parent: Parent, itemType: Int) -> Cell)?

    var configureCell: ((item: Item, viewHolder: Cell) -> Unit)?

    override fun onSubscribe(d: Disposable) {}
}

fun <T, C, P> Flowable<List<T>>.bind(to: DisplayableDatasource<T, C, P>) {
    val datasource = to
}

fun <T, O : Observer<List<T>>> Observable<List<T>>.bind(to: O): Disposable {
    return subscribe({ to.onNext(it) }, { to.onError(it) }, { to.onComplete() })
}

fun <T, O : Observer<List<T>>> Flowable<List<T>>.bind(to: O): Disposable {
    return subscribe({ to.onNext(it) }, { to.onError(it) }, { to.onComplete() })
}