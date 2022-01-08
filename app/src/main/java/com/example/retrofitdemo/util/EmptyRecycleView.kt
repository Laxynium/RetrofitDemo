package com.example.retrofitdemo.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EmptyRecyclerView : RecyclerView {
    private var emptyView: View? = null

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    )

    fun checkIfEmpty() {
        if (emptyView != null) {
            emptyView!!.visibility = if (adapter!!.itemCount > 0) View.GONE else View.VISIBLE
        }
    }

    val observer: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            checkIfEmpty()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)
    }

    fun setEmptyView(emptyView: View?) {
        this.emptyView = emptyView
        checkIfEmpty()
    }
}
