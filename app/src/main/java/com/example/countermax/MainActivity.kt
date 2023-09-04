package com.example.countermax

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countermax.databinding.ActivityMainBinding
import com.example.countermax.databinding.RecyclerRowBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var counterList: ArrayList<Counter>
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: Adapter // Adapter değişkenini onCreate içinde tanımlıyoruz
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        counterList = ArrayList()
        loadCountersFromSharedPreferences()

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = Adapter(counterList) // Adapter'i onCreate içinde oluşturuyoruz
        binding.recyclerView.adapter = adapter

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            fabClicked()
        }
    }

    fun fabClicked() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add New Counter")

        val inputLayout = LinearLayout(this)
        inputLayout.orientation = LinearLayout.VERTICAL

        val nameEditText = EditText(this)
        nameEditText.hint = "Name"
        inputLayout.addView(nameEditText)

        val countEditText = EditText(this)
        countEditText.hint = "Count"
        inputLayout.addView(countEditText)

        builder.setView(inputLayout)

        builder.setPositiveButton("Add") { dialog, _ ->
            val name = nameEditText.text.toString()
            val count = countEditText.text.toString().toIntOrNull()

            if (name.isNotEmpty() && count != null) {
                val newCounter = Counter(name, count)
                counterList.add(newCounter)
                adapter.notifyItemInserted(counterList.size - 1)
                saveCountersToSharedPreferences()
            }

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun saveCountersToSharedPreferences() {
        val editor = sharedPref.edit()
        for (counter in counterList) {
            editor.putString("CounterName_${counter.name}", counter.name)
            editor.putInt("CounterValue_${counter.name}", counter.count)

        }
        editor.apply()
    }

    private fun loadCountersFromSharedPreferences() {
        val counterNames = sharedPref.all.keys.filter { it.startsWith("CounterName_") }
        for (nameKey in counterNames) {
            val name = sharedPref.getString(nameKey, "") ?: ""
            val countKey = "CounterValue_$name"
            val count = sharedPref.getInt(countKey, 0)
            counterList.add(Counter(name, count))
        }
    }
}

