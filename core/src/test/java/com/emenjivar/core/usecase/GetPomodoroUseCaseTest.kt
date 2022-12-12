package com.emenjivar.core.usecase

import com.emenjivar.core.model.Pomodoro
import com.emenjivar.pomodoro.data.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetPomodoroUseCaseTest {
    private lateinit var repository: com.emenjivar.pomodoro.data.SettingsRepository
    private lateinit var useCase: com.emenjivar.pomodoro.usecases.GetPomodoroUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(com.emenjivar.pomodoro.data.SettingsRepository::class.java)
        useCase = com.emenjivar.pomodoro.usecases.GetPomodoroUseCase(repository)
    }

    @Test
    fun `getPomodoroUseCaseTest when repository returns pomodoro instance`(): Unit =
        runBlocking {
            val pomodoro = Pomodoro(0L, 0L)
            Mockito.`when`(repository.getPomodoro()).thenReturn(pomodoro)
            val result = useCase.invoke()
            Assert.assertEquals(pomodoro, result)
        }
}
