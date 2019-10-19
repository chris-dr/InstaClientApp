package com.drevnitskaya.instaclientapp.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.drevnitskaya.instaclientapp.data.Result
import com.drevnitskaya.instaclientapp.data.entities.ErrorHolder
import com.drevnitskaya.instaclientapp.domain.auth.ComposeAuthUrlUseCase
import com.drevnitskaya.instaclientapp.domain.auth.GetAccessTokenUseCase
import com.drevnitskaya.instaclientapp.domain.auth.ParseAuthCodeUseCase
import com.drevnitskaya.instaclientapp.testUtils.testObserver
import com.drevnitskaya.instaclientapp.utils.NetworkStateProvider
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@ExperimentalCoroutinesApi
class LoginWebViewModelTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()
    private val networkStateProvider: NetworkStateProvider = mock()
    private val composeAuthUrlUseCase: ComposeAuthUrlUseCase = mock()
    private val parseAuthCodeUseCase: ParseAuthCodeUseCase = mock()
    private val getAccessTokenUseCase: GetAccessTokenUseCase = mock()
    private lateinit var viewModel: LoginWebViewModel
    private val mockedAuthUrl = "http://localhost:8080"
    private val mockedAuthCode = "098765"

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
    fun loadAuthForm_noNetworkConnError() {
        whenever(networkStateProvider.isNetworkAvailable()).thenReturn(false)

        createViewModel()

        val showProgressTest = viewModel.showProgress.testObserver()
        val showLoginFormTest = viewModel.showLoginForm.testObserver()
        val showErrorStateTest = viewModel.showErrorState.testObserver()

        val showProgressValues = showProgressTest.observedValues
        assertTrue(showProgressValues.size == 1 && showProgressValues[0] == false)
        val showLoginFormValues = showLoginFormTest.observedValues
        assertTrue(showLoginFormValues.size == 1 && showLoginFormValues[0] == false)
        val showErrorStateValues = showErrorStateTest.observedValues
        assertTrue(showErrorStateValues.size == 1 && showErrorStateValues[0] is ErrorHolder.NetworkError)
    }

    @Test
    fun loadAuthForm_success() {
        whenever(networkStateProvider.isNetworkAvailable()).thenReturn(true)
        whenever(composeAuthUrlUseCase.execute()).thenReturn(mockedAuthUrl)

        createViewModel()

        val showProgressTest = viewModel.showProgress.testObserver()
        val loadLoginFormTest = viewModel.loadLoginForm.testObserver()

        val loadLoginFormValues = loadLoginFormTest.observedValues

        val showProgressValues = showProgressTest.observedValues
        assertTrue(showProgressValues.size == 1 && showProgressValues[0] == true)
        assertTrue(loadLoginFormValues.size == 1 && loadLoginFormValues[0] == mockedAuthUrl)
    }

    @Test
    fun handleAuthUrl_success() = runBlockingTest {
        whenever(parseAuthCodeUseCase.execute(any()))
            .thenReturn(Result.Success(mockedAuthCode))
        whenever(getAccessTokenUseCase.execute(any()))
            .thenReturn(Result.Complete)
        createViewModel()

        viewModel.handleAuthUrl(mockedAuthUrl)

        val redirectUrlCaptor = argumentCaptor<String>()
        verify(parseAuthCodeUseCase).execute(authUrl = redirectUrlCaptor.capture())
        val actualRedirectUrl = redirectUrlCaptor.firstValue
        assertEquals(mockedAuthUrl, actualRedirectUrl)

        val authCodeCaptor = argumentCaptor<String>()
        verify(getAccessTokenUseCase).execute(authCode = authCodeCaptor.capture())
        val actualAuthCode = authCodeCaptor.firstValue
        assertEquals(mockedAuthCode, actualAuthCode)

        val openProfileTest = viewModel.openProfile.testObserver()
        assertTrue(openProfileTest.observedValues.size == 1)
    }

    @Test
    fun handleAuthUrl_wrongRedirectUrl() = runBlockingTest {
        createViewModel()

        viewModel.handleAuthUrl("https://www.google.com")

        verify(parseAuthCodeUseCase, never()).execute(any())
        verify(getAccessTokenUseCase, never()).execute(any())
    }

    @Test
    fun handleAuthUrl_parseAuthCodeError() = runBlockingTest {
        whenever(parseAuthCodeUseCase.execute(any()))
            .thenReturn(Result.Error(Exception("Auth code parsing error")))
        createViewModel()

        viewModel.handleAuthUrl(mockedAuthUrl)

        verify(getAccessTokenUseCase, never()).execute(any())

        val showErrorStateTest = viewModel.showErrorState.testObserver()
        val showErrorStateValues = showErrorStateTest.observedValues
        assertTrue(showErrorStateValues.size == 1 && showErrorStateValues[0] is ErrorHolder.GeneralError)
    }

    @Test
    fun handleAuthUrl_getTokenError() = runBlockingTest {
        whenever(parseAuthCodeUseCase.execute(any()))
            .thenReturn(Result.Success(mockedAuthCode))
        whenever(getAccessTokenUseCase.execute(any()))
            .thenReturn(Result.Error(Exception("Token getting error")))
        createViewModel()

        viewModel.handleAuthUrl(mockedAuthUrl)

        val redirectUrlCaptor = argumentCaptor<String>()
        verify(parseAuthCodeUseCase).execute(authUrl = redirectUrlCaptor.capture())
        val actualRedirectUrl = redirectUrlCaptor.firstValue
        assertEquals(mockedAuthUrl, actualRedirectUrl)

        val openProfileTest = viewModel.openProfile.testObserver()
        assertTrue(openProfileTest.observedValues.isEmpty())

        val showErrorStateTest = viewModel.showErrorState.testObserver()
        val showErrorStateValues = showErrorStateTest.observedValues
        assertTrue(showErrorStateValues.size == 1 && showErrorStateValues[0] is ErrorHolder.GeneralError)
    }

    @Test
    fun reloadAuthForm_noNetworkConnError() {
        whenever(networkStateProvider.isNetworkAvailable()).thenReturn(false)
        createViewModel()

        viewModel.reloadAuthForm()

        val showErrorStateTest = viewModel.showErrorState.testObserver()
        val showErrorStateValues = showErrorStateTest.observedValues
        assertTrue(showErrorStateValues.size == 1 && showErrorStateValues[0] is ErrorHolder.NetworkError)
    }


    @Test
    fun reloadAuthForm_success() {
        whenever(networkStateProvider.isNetworkAvailable()).thenReturn(true)
        whenever(composeAuthUrlUseCase.execute()).thenReturn(mockedAuthUrl)
        createViewModel()

        viewModel.reloadAuthForm()

        val showErrorStateTest = viewModel.showErrorState.testObserver()
        val showErrorStateValues = showErrorStateTest.observedValues
        assertTrue(showErrorStateValues.size == 1 && showErrorStateValues[0] == null)

        val showProgressTest = viewModel.showProgress.testObserver()
        val showProgressValues = showProgressTest.observedValues
        assertTrue(showProgressValues.size == 1 && showProgressValues[0] == true)

        val loadLoginFormTest = viewModel.loadLoginForm.testObserver()
        val loadLoginFormValues = loadLoginFormTest.observedValues
        assertTrue(loadLoginFormValues.size == 1 && loadLoginFormValues[0] == mockedAuthUrl)
    }

    private fun createViewModel() {
        viewModel = LoginWebViewModel(
            networkStateProvider,
            composeAuthUrlUseCase,
            parseAuthCodeUseCase,
            getAccessTokenUseCase
        )
    }

}