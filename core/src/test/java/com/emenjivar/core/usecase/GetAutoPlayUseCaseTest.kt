package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetAutoPlayUseCaseTest {
    private lateinit var repository: SettingsRepository
    private lateinit var useCase: GetAutoPlayUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(SettingsRepository::class.java)
        useCase = GetAutoPlayUseCase(repository)
    }

    @Test
    fun `getAutoPlayUseCase when repository returns true`(): Unit = runBlocking {
        Mockito.`when`(repository.getAutoPlay()).thenReturn(true)
        val result = useCase.invoke()
        assertTrue(result)
    }

    @Test
    fun `getAutoPlayUseCase when repository returns false`(): Unit =
        runBlocking {
            Mockito.`when`(repository.getAutoPlay()).thenReturn(false)
            val result = useCase.invoke()
            assertFalse(result)
        }
}
