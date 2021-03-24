package com.nelson.branch_app

import com.nelson.branch_app.data.ToDoModel
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Local unit test for testing data models.
 */
class ToDoUnitTest {

    @Test
    fun todo_fieldAccess() {
        val image = "/images/1033/dynamic/foodandrecipes/2015/07/TANDORI_GRILLED_SHRIMP_1053_800x800.jpg"
        val filter = "[\\\\\\\"contentTags.food_cookingmethod.tags: Grilling\\\\\\\"]\""
        val todoModel = ToDoModel(image, filter, "Summer Grilling")
        assertEquals(image, todoModel.image)
        assertEquals("Summer Grilling", todoModel.title)
        assertEquals(filter, todoModel.filter)
    }

}