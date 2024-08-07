package com.example.rpn_calculator.domain

import com.example.rpn_calculator.domain.model.Operation
import com.example.rpn_calculator.domain.model.CalculatorState

/**
 * `RpnCalculator` class implements Reverse Polish Notation (RPN) calculator.
 * *Processes series of arithmetic commands using stack-based RPN approach to perform calculations.
 *
 * **Usage Example**:
 * val calculator = RpnCalculator()
 * val result = calculator.execute(CalculatorCommand.PushNumber(5.0))
 * // Pushes 5 onto the stack
 * val result = calculator.execute(CalculatorCommand.PushNumber(3.0))
 * // Pushes 3 onto the stack
 * val result = calculator.execute(CalculatorCommand.Add)
 * // Adds 5 and 3 from the stack, result is 8
 *
 * **Notes**:
 * - The stack is used to hold operands and intermediate results.
 * - Commands are executed in the order they are received.
 */
class RpnCalculator {
    private var _state = CalculatorState()
    val state: CalculatorState
        get() = _state

    //Various commands (Note: Suspend function not as all operations asynchronous and executed immediately.
    fun execute(command: Operation): String {
        return when (command) {
            is Operation.PushNumber -> {
                _state = _state.copy(stack = _state.stack + command.number)
                command.number.toString()
            }
            is Operation.Add -> performOperation { a, b -> a + b }
            is Operation.Subtract -> performOperation { a, b -> a - b }
            is Operation.Multiply -> performOperation { a, b -> a * b }
            is Operation.Divide -> performOperation { a, b -> a / b }
            is Operation.Quit -> {
               _state = _state.copy(shouldQuit = true)
                "Calculator exiting..."
            }
            is Operation.Error -> "Invalid operation"
        }
    }

    //Function to determine result of RPN functionality as per requirements
    private fun performOperation(operation: (Double, Double) -> Double): String {
        return if (_state.stack.size >= 2) {
            val (b, a) = _state.stack.takeLast(2)
            val result = operation(b, a)
            _state = _state.copy(stack = _state.stack.dropLast(2) + result)
            result.toString()
        } else {
            "Insufficient operands"
        }
    }
}
