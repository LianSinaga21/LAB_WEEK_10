package com.example.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.viewmodels.TotalViewModel

class MainActivity : AppCompatActivity() {

    // Database instance
    private val db by lazy { prepareDatabase() }

    // ViewModel instance
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load previous value from DB
        initializeValueFromDatabase()

        // Start observing ViewModel & set button
        prepareViewModel()
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {

        // Observe LiveData
        viewModel.total.observe(this) {
            updateText(it)
        }

        // Button click
        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // PREPARE DATABASE
    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        ).allowMainThreadQueries().build()
    }

    // INITIALIZE VALUE FROM DB
    private fun initializeValueFromDatabase() {
        val total = db.totalDao().getTotal(ID)

        if (total.isEmpty()) {
            // If DB empty, insert starting value = 0
            db.totalDao().insert(Total(id = ID, total = 0))
        } else {
            // If DB has previous value → load it into ViewModel
            viewModel.setTotal(total.first().total)
        }
    }

    // SAVE NEW VALUE WHEN APP IS PAUSED
    override fun onPause() {
        super.onPause()

        // Update DB with latest total
        db.totalDao().update(
            Total(ID, viewModel.total.value!!)
        )
    }

    companion object {
        const val ID: Long = 1
    }
}
