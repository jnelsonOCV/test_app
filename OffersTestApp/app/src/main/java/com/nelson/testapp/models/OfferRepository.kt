package com.nelson.testapp.models

import android.app.Activity
import android.content.Context
import android.util.Log
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.ArrayList

/**
 * A static object for storing and accessing the OrderItems.
 */
object OrderRepository {

    /**
     * An ArrayList of OrderItem objects for filling the list.
     */
    val ITEMS: ArrayList<OrderItem> = ArrayList()

    /**
     * Sets the OrderRepository's ITEMS with the provided ArrayList.
     *
     * @param items - The new items for the OrderRepository
     */
    @JvmStatic
    fun setItems(items: List<OrderItem>) {
        ITEMS.clear()
        ITEMS.addAll(items)
    }

    /**
     * Retrieves the OrderItem corresponding to the given id
     *
     * @param id - The id of the OrderItem to be returned
     *
     * @return the requested OrderItem or null if it doesn't exist
     */
    fun getItem(id: String) : OrderItem? {
        ITEMS.forEach {
            if (it.id == id) return it
        }
        return null
    }

    /**
     * Caches the orders to save user's favorite orders.
     *
     * @param activity - Activity needed to write to file
     */
    fun cacheOrders(activity: Activity): Boolean {
        val cacheName = "ORDERS"
        try {
            activity.openFileOutput(cacheName, Context.MODE_PRIVATE).use { fos ->
                val oos = ObjectOutputStream(fos)
                oos.writeObject(ITEMS)
                oos.close()
            }
        } catch (e: Exception) {
            Log.d("CACHE", "Caching error for $cacheName")
        }
        return true
    }

    /**
     * Loads the orders to recover user's favorite orders.
     *
     * @param activity - Activity needed to read from file
     */
    fun loadOrders(activity: Activity): Any? {
        val cacheName = "ORDERS"
        try {
            activity.openFileInput(cacheName).use { fis ->
                val ois = ObjectInputStream(fis)
                val data = ois.readObject()
                ois.close()
                return data
            }
        } catch (e: Exception) {
            Log.d("CACHE", "Couldn't read cache for $cacheName")
            return null
        }
    }
}