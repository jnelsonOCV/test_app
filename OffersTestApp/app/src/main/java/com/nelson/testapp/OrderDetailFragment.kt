package com.nelson.testapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.nelson.testapp.models.OrderItem
import com.nelson.testapp.models.OrderRepository

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [OrderListActivity]
 * in two-pane mode (on tablets) or a [OrderDetailActivity]
 * on handsets.
 */
class OrderDetailFragment : Fragment() {

    /**
     * The content this fragment is presenting.
     */
    private var item: OrderItem? = null

    private val fab : FloatingActionButton? by lazy { activity?.findViewById<FloatingActionButton>(R.id.fab) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = OrderRepository.getItem(it.getString(ARG_ITEM_ID)!!)
                activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = item?.value
            }
        }

        reloadFab()

        fab?.setOnClickListener { view ->
            item?.isFavorite = !item!!.isFavorite
            reloadFab()
            val text = if (item!!.isFavorite) "favorited" else "unfavorited"
            Snackbar.make(view, "Order $text", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun reloadFab() {
        if (item != null) fab?.load(if (item!!.isFavorite) R.drawable.favorite else R.drawable.unfavorite)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        item?.let {
            rootView.findViewById<TextView>(R.id.item_detail_title).text = it.name
            rootView.findViewById<TextView>(R.id.item_detail_desc).text = it.description
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}