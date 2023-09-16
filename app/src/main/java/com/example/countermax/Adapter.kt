package com.example.countermax

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countermax.databinding.RecyclerRowBinding

class Adapter(private val counterList: ArrayList<Counter>) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

    class MyViewHolder(val binding: RecyclerRowBinding, private val adapter: Adapter) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.incrementButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    incrementButtonClicked(position)
                }
                // Silme ve done butonlarını gizle
                binding.deleteButton.visibility = View.GONE
                binding.doneButton.visibility = View.GONE

                // EditText alanlarını etkileşim dışı bırakın
                binding.counter.isFocusable = false
                binding.counter.isFocusableInTouchMode = false
                binding.counterText.isFocusable = false
                binding.counterText.isFocusableInTouchMode = false
            }

            binding.decrementButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    decrementButtonClicked(position)
                }
                // Silme ve done butonlarını gizle
                binding.deleteButton.visibility = View.GONE
                binding.doneButton.visibility = View.GONE

                // EditText alanlarını etkileşim dışı bırakın
                binding.counter.isFocusable = false
                binding.counter.isFocusableInTouchMode = false
                binding.counterText.isFocusable = false
                binding.counterText.isFocusableInTouchMode = false

            }

            // Silme butonunu varsayılan olarak gizle
            binding.deleteButton.visibility = View.GONE
            binding.doneButton.visibility = View.GONE


            // CardView'a uzun basıldığında
            binding.counterCard.setOnLongClickListener {

                val vibrationDuration = 200L
                binding.counter.isEnabled = true
                binding.counterText.isEnabled = true

                vibrateDeviceWithCustomIntensity(binding.counter.context, 200, 100)
                // Silme butonunu görünür yapın
                binding.deleteButton.visibility = View.VISIBLE
                binding.doneButton.visibility = View.VISIBLE

                binding.counter.isFocusable = true
                binding.counter.isFocusableInTouchMode = true
                binding.counterText.isFocusable = true
                binding.counterText.isFocusableInTouchMode = true
                true
            }

// Silme butonuna tıklanınca
            binding.deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteButtonClicked(position)
                }
            }

//done button tiklaninca
            binding.doneButton.setOnClickListener {
                // EditText'leri etkileşim dışı bırak
                binding.counter.isEnabled = false
                binding.counterText.isEnabled = false

                // EditText'lerin içindeki değerleri al
                val updatedCounterName = binding.counterText.text.toString()
                val updatedCounterValue = binding.counter.text.toString().toInt()

                // Değişiklikleri kaydetmek için adapterdaki ilgili Counter nesnesini güncelle
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val counter = adapter.counterList[position]
                    counter.name = updatedCounterName
                    counter.count = updatedCounterValue
                    adapter.notifyItemChanged(position)
                }

                // Silme ve done butonlarını gizle
                binding.deleteButton.visibility = View.GONE
                binding.doneButton.visibility = View.GONE
            }



        }
        private fun vibrateDeviceWithCustomIntensity(context: Context, duration: Long, amplitude: Int) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Android 8.0 ve üstü için özelleştirilmiş titreşim kalıbı oluşturun
                    val vibrationEffect = VibrationEffect.createOneShot(duration, amplitude)
                    vibrator.vibrate(vibrationEffect)
                } else {
                    // Android 8.0'dan daha düşük sürümlerde, siddet ayarı yapamayız
                    vibrator.vibrate(duration)
                }
            }
        }
        private fun incrementButtonClicked(position: Int) {
            val counter = adapter.counterList[position]
            counter.count++
            adapter.notifyItemChanged(position)
            val mainActivity =  binding.counterCard.context as? MainActivity
            mainActivity?.saveCountersToSharedPreferences()
        }

        private fun decrementButtonClicked(position: Int) {
            val counter = adapter.counterList[position]
            counter.count--
            adapter.notifyItemChanged(position)
            val mainActivity =  binding.counterCard.context as? MainActivity
            mainActivity?.saveCountersToSharedPreferences()
        }

        private fun deleteButtonClicked(position: Int) {
            val counter = adapter.counterList[position]
            adapter.counterList.removeAt(position)
            adapter.notifyItemRemoved(position)

            // Şimdi SharedPreferences'ten de silme işlemi gerçekleştirin
            val mainActivity =  binding.counterCard.context as? MainActivity
            mainActivity?.deleteCounterFromSharedPreferences(counter)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerRowBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding, this)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val counter = counterList[position]
        holder.binding.counterText.setText(counter.name)
        holder.binding.counter.setText(counter.count.toString())
        holder.binding.deleteButton.visibility= View.GONE
        holder.binding.deleteButton.visibility = View.GONE



    }

    override fun getItemCount(): Int {
        return counterList.size
    }

}
