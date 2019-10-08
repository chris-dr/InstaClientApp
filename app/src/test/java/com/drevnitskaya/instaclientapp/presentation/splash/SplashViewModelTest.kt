package com.drevnitskaya.instaclientapp.presentation.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.drevnitskaya.instaclientapp.domain.auth.CheckAuthStateUseCase
import com.drevnitskaya.instaclientapp.testUtils.testObserver
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertTrue

@ExperimentalCoroutinesApi
class SplashViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()
    private val checkAuthStateUseCase: CheckAuthStateUseCase = mock()
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun reset() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun checkAuthState_loggedIn() = runBlockingTest {
        whenever(checkAuthStateUseCase.execute()).thenReturn(true)

        viewModel = SplashViewModel(checkAuthStateUseCase)

        val openProfileTest = viewModel.openProfile.testObserver()
        val openLoginTest = viewModel.openLogin.testObserver()

        assertTrue(openProfileTest.observedValues.size == 1)
        assertTrue(openLoginTest.observedValues.isEmpty())
    }

    @Test
    fun checkAuthState_notLoggedIn() = runBlockingTest {
        whenever(checkAuthStateUseCase.execute()).thenReturn(false)

        viewModel = SplashViewModel(checkAuthStateUseCase)

        val openProfileTest = viewModel.openProfile.testObserver()
        val openLoginTest = viewModel.openLogin.testObserver()

        assertTrue(openProfileTest.observedValues.isEmpty())
        assertTrue(openLoginTest.observedValues.size == 1)
    }
}