package ru.yandex.loginapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty fields sets EmptyFieldsError`() = runTest {
        viewModel.login("", "")
        assertEquals(LoginScreenState.EmptyFieldsError, viewModel.state.value)
    }

    @Test
    fun `login with invalid email sets EmailValidationError`() = runTest {
        viewModel.login("invalidemail", "password")
        assertEquals(LoginScreenState.EmailValidationError, viewModel.state.value)
    }

    @Test
    fun `login with valid data sets Loading`() = runTest {
        viewModel.login("test@example.com", "password")
        testDispatcher.scheduler.runCurrent()
        assertEquals(LoginScreenState.Loading, viewModel.state.value)
    }

    @Test
    fun `login with valid data sets Loading then Success`() = runTest {
        viewModel.login("test@example.com", "password")
        testDispatcher.scheduler.runCurrent()
        assertEquals(LoginScreenState.Loading, viewModel.state.value)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(LoginScreenState.Success, viewModel.state.value)
    }
}