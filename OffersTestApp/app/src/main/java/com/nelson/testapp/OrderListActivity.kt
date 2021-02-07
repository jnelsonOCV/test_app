package com.nelson.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import coil.load

import com.nelson.testapp.models.OrderItem
import com.nelson.testapp.models.OrderRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import java.lang.reflect.ParameterizedType

/**
 * An activity representing a list of OrderItems.
 *
 * This activity has different presentations for handset and tablet-size devices.
 * On handsets, the activity presents a list of items, which when touched,
 * lead to a [OrderDetailActivity] representing item details.
 * On tablets, the activity presents the list of items and item
 * details (in a [OrderDetailFragment]) side-by-side using two panes.
 */
class OrderListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode (running on a tablet)
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (OrderRepository.ITEMS.isEmpty()) {
            val json = getJsonDataFromAsset("offers.json")
            loadOrdersIntoRepository(json!!)
        }

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // If this view is present, then the activity should be in two-pane mode.
            twoPane = true
        }
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView(findViewById(R.id.item_list))
    }

    /**
     * Retrieves JSON data as String from asset .json file
     *
     * @param fileName - The filename of the asset to be parsed
     *
     * @return String representing JSON data
     */
    private fun getJsonDataFromAsset(fileName: String): String? {
        val jsonString: String
        try {
            jsonString = assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            Log.e("Local JSON error", "error in JSON file $ioException")
            return null
        }
        return jsonString
    }

    private fun loadOrdersIntoRepository(json: String) {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, OrderItem::class.java)
        val orderAdapter : JsonAdapter<List<OrderItem>> = moshi.adapter(type)
        OrderRepository.setItems(orderAdapter.fromJson(json)!!)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, OrderRepository.ITEMS, twoPane)
        if (!twoPane) recyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: OrderListActivity,
                                        private val values: List<OrderItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as OrderItem
                if (twoPane) {
                    updateDetailPane(item)
                } else {
                    val intent = Intent(v.context, OrderDetailActivity::class.java).apply {
                        putExtra(OrderDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        /**
         * Updates OrderDetailFragment with the newly selected OrderItem
         *
         * @param item - The new OrderItem that was selected
         */
        private fun updateDetailPane(item: OrderItem) {
            val fragment = OrderDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(OrderDetailFragment.ARG_ITEM_ID, item.id)
                }
            }
            parentActivity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.order_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.orderValue.text = item.value
            holder.orderName.text = item.name
            if (item.url != null) holder.orderImage.load(item.url) {
                placeholder(R.drawable.placeholder)
            }

            holder.orderFavorite.load(if (item.isFavorite) R.drawable.favorite else R.drawable.unfavorite)

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val orderImage: ImageView = view.findViewById(R.id.order_image)
            val orderValue: TextView = view.findViewById(R.id.order_value)
            val orderName: TextView = view.findViewById(R.id.order_name)
            val orderFavorite: ImageView = view.findViewById(R.id.order_favorite)
        }
    }
}