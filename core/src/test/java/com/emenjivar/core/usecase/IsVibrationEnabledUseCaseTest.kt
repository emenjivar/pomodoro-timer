package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class IsVibrationEnabledUseCaseTest {
    private lateinit var repository: SettingsRepository
    private lateinit var useCase: IsVibrationEnabledUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(SettingsRepository::class.java)
        useCase = IsVibrationEnabledUseCase(repository)
    }

    @Test
    fun `isVibrationEnabledUseCase when repository returns true`(): Unit =
        runBlocking {
            Mockito.`when`(repository.isVibrationEnabled()).thenReturn(true)
            val result = useCase.invoke()
            Assert.assertTrue(result)
        }

    @Test
    fun `isVibrationEnabledUseCase when repository returns false`(): Unit =
        runBlocking {
            Mockito.`when`(repository.isVibrationEnabled()).thenReturn(false)
            val result = useCase.invoke()
            Assert.assertFalse(result)
        }
}
