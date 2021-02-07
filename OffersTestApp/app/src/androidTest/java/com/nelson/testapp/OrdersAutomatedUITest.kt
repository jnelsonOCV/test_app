package com.nelson.testapp


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class OrdersAutomatedUITest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OrderListActivity::class.java)

    @Test
    fun ordersAutomatedTest() {
        val recyclerView = onView(
            allOf(
                withId(R.id.item_list),
                childAtPosition(
                    withId(R.id.frameLayout),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val imageButton = onView(
            allOf(
                withId(R.id.fab),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.detail_toolbar),
                        childAtPosition(
                            allOf(
                                withId(R.id.toolbar_layout),
                                withContentDescription("$0.75 Cash Back")
                            ),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val textView = onView(
            allOf(
                withText("Offers App"),
                withParent(
                    allOf(
                        withId(R.id.toolbar),
                        withParent(withId(R.id.app_bar))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Offers App")))

        val recyclerView2 = onView(
            allOf(
                withId(R.id.item_list),
                childAtPosition(
                    withId(R.id.frameLayout),
                    0
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(19, click()))

        val imageButton2 = onView(
            allOf(
                withId(R.id.fab),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        imageButton2.check(matches(isDisplayed()))

        val appCompatImageButton2 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.detail_toolbar),
                        childAtPosition(
                            allOf(
                                withId(R.id.toolbar_layout),
                                withContentDescription("$1.00 Cash Back")
                            ),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())
    }

    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
