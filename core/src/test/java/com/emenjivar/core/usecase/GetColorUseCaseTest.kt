package com.emenjivar.core.usecase

import com.emenjivar.pomodoro.data.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetColorUseCaseTest {
    private lateinit var repository: com.emenjivar.pomodoro.data.SettingsRepository
    private lateinit var useCase: com.emenjivar.pomodoro.usecases.GetColorUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(com.emenjivar.pomodoro.data.SettingsRepository::class.java)
        useCase = com.emenjivar.pomodoro.usecases.GetColorUseCase(repository)
    }

    @Test
    fun `getColorUseCaseTest when repository returns a number`(): Unit = runBlocking {
        Mockito.`when`(repository.getColor()).thenReturn(1)
        val result = useCase.invoke()
        assertEquals(1, result)
    }

    @Test
    fun `getColorUseCaseTest when repository returns null`(): Unit =
        runBlocking {
            Mockito.`when`(repository.getColor()).thenReturn(null)
            val result = useCase.invoke()
            assertNull(result)
        }
}
