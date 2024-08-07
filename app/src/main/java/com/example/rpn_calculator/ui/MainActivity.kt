package com.example.rpn_calculator.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.rpn_calculator.databinding.ActivityMainBinding
import com.example.rpn_calculator.ui.viewmodel.CalculatorViewModel
import com.example.rpn_calculator.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            // repeatOnLifecycle to collect Flows (instead of launching multiple coroutine, all flow in single launch block)
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Collecting output state
                launch {
                    viewModel.output.collect { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                binding.outputTextView.text = resource.data
                            }

                            Status.LOADING -> {
                                // If I had SPINNER, should make visible here in complex loading features
                            }

                            Status.ERROR -> {
                                // Handle further error state
                            }
                        }
                    }
                }

                //Collecting error state
                launch {
                    viewModel.error.collect { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                // Handle success state here
                            }

                            Status.ERROR -> {
                                resource.message?.let {
                                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                                    viewModel.clearError()
                                }
                            }

                            Status.LOADING -> {
                                // Show a loading indicator/progress bar
                            }
                        }
                    }
                }
            }
        }

        viewModel.quit.observe(this) { shouldQuit ->
            if (shouldQuit) {
                finish() // Close activity, which will "Quit"/exit and terminate the app
            }
        }

        binding.enterButton.setOnClickListener {
            val input = binding.inputEditText.text.toString()
            viewModel.onInput(input)
            binding.inputEditText.text.clear()
        }
    }
}
