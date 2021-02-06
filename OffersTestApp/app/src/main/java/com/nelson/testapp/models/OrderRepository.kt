package com.nelson.testapp.models

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
    fun setItems(items: ArrayList<OrderItem>) {
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
}