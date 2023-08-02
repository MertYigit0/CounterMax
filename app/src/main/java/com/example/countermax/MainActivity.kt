package com.example.countermax

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.countermax.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var counterList: ArrayList<Counter>
    private lateinit var binding: ActivityMainBinding




    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        counterList = ArrayList<Counter>()

        val counter1 = Counter("Water",1)
        val counter2 = Counter("Book Page",4)

        counterList.add(counter1)
        counterList.add(counter2)



        binding.recyclerView.layoutManager = GridLayoutManager(this,2)
        val adapter = Adapter(counterList)
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
                binding.recyclerView.adapter?.notifyItemInserted(counterList.size - 1)
            }

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }














}