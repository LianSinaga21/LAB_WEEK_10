package com.example.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel

class MainActivity : AppCompatActivity() {

    private val db by lazy { prepareDatabase() }

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    private var lastUpdatedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onStart() {
        super.onStart()

        if (lastUpdatedDate.isNotEmpty()) {
            Toast.makeText(this, "Last updated: $lastUpdatedDate", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { value ->
            updateText(value)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    private fun initializeValueFromDatabase() {
        val totalData = db.totalDao().getTotal(ID)

        if (totalData.isEmpty()) {

            val first = Total(
                id = ID,
                total = TotalObject(
                    value = 0,
                    date = java.util.Date().toString()
                )
            )

            db.totalDao().insert(first)
            viewModel.setTotal(0)

        } else {
            val saved = totalData.first()
            viewModel.setTotal(saved.total.value)
            lastUpdatedDate = saved.total.date
        }
    }

    override fun onPause() {
        super.onPause()

        val updated = Total(
            id = ID,
            total = TotalObject(
                value = viewModel.total.value ?: 0,
                date = java.util.Date().toString()
            )
        )

        db.totalDao().update(updated)
    }

    companion object {
        const val ID: Long = 1
    }
}
