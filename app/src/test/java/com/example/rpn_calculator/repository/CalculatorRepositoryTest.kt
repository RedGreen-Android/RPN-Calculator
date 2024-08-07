package com.example.rpn_calculator.repository

import com.example.rpn_calculator.data.CalculatorRepositoryImpl
import com.example.rpn_calculator.domain.RpnCalculator
import com.example.rpn_calculator.domain.model.Operation
import com.example.rpn_calculator.domain.repository.CalculatorRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalculatorRepositoryTest {

    private lateinit var calculatorRepository: CalculatorRepository

    @Before
    fun setUp() {
        calculatorRepository = CalculatorRepositoryImpl(RpnCalculator())
    }

    @Test
    fun `addition test`() = runTest {
        calculatorRepository.executeCommand(Operation.PushNumber(5.0))
        calculatorRepository.executeCommand(Operation.PushNumber(3.0))
        calculatorRepository.executeCommand(Operation.Add)
        val state = calculatorRepository.stateFlow.first() //stateflow always contain value, get current state
        assertEquals(8.0, state.stack.lastOrNull())
    }

    @Test
    fun `subtraction test`() = runTest {
        calculatorRepository.executeCommand(Operation.PushNumber(5.0))
        calculatorRepository.executeCommand(Operation.PushNumber(3.0))
        calculatorRepository.executeCommand(Operation.Subtract)
        val state = calculatorRepository.stateFlow.first()
        assertEquals(2.0, state.stack.lastOrNull())
    }

    @Test
    fun `multiplication test`() = runTest {
        calculatorRepository.executeCommand(Operation.PushNumber(5.0))
        calculatorRepository.executeCommand(Operation.PushNumber(3.0))
        calculatorRepository.executeCommand(Operation.Multiply)
        val state = calculatorRepository.stateFlow.first()
        assertEquals(15.0, state.stack.lastOrNull())
    }

    @Test
    fun `division test`() = runTest {
        calculatorRepository.executeCommand(Operation.PushNumber(9.0))
        calculatorRepository.executeCommand(Operation.PushNumber(3.0))
        calculatorRepository.executeCommand(Operation.Divide)
        val state = calculatorRepository.stateFlow.first()
        assertEquals(3.0, state.stack.lastOrNull())
    }
}
