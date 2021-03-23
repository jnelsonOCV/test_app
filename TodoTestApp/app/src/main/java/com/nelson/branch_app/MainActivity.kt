package com.nelson.branch_app

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.nelson.branch_app.databinding.ActivityMainBinding

private const val FRAG_TAG = "TODO_FRAG"

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var toDoFrag : ToDoFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Maintain state after activity restarts
        toDoFrag = if (savedInstanceState != null) {
            supportFragmentManager.findFragmentByTag(FRAG_TAG) as ToDoFragment
        } else {
            ToDoFragment()
        }
        showToDoFrag()
    }

    fun displaySnackbar(text: String) {
        val snackbar: Snackbar = Snackbar.make(binding.container,
                text,
                Snackbar.LENGTH_LONG)
        val view = snackbar.view
        val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        tv.setTextColor(Color.WHITE)
        snackbar.show()
    }

    private fun showToDoFrag() {
        supportFragmentManager.beginTransaction().replace(binding.container.id, toDoFrag!!, FRAG_TAG).commitNow()
    }
}