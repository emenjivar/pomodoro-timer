package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class GetColorUseCaseTest {
    private lateinit var repository: SettingsRepository
    private lateinit var useCase: GetColorUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(SettingsRepository::class.java)
        useCase = GetColorUseCase(repository)
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
