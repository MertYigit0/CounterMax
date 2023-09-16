package com.example.countermax

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.countermax.databinding.ActivityMainBinding
import com.example.countermax.databinding.RecyclerRowBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Collections

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

            //Drag and drop to Reorder the RecylerView Items
        val itemTouchHelper = ItemTouchHelper(object  : ItemTouchHelper.SimpleCallback
            (ItemTouchHelper.UP or  ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT , 0){
            override fun onMove(
                recyclerView: RecyclerView,
                source: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = source.adapterPosition
                val targerPosition = target.adapterPosition

                Collections.swap(counterList,sourcePosition,targerPosition)
                adapter.notifyItemMoved(sourcePosition,targerPosition)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

        })
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)


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

  fun saveCountersToSharedPreferences() {
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

    fun deleteCounterFromSharedPreferences(counter: Counter) {
        val editor = sharedPref.edit()
        editor.remove("CounterName_${counter.name}")
        editor.remove("CounterValue_${counter.name}")
        editor.apply()
    }

}

