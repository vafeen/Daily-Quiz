package ru.vafeen.domain.network

/**
 * Класс, представляющий результат сетевой операции.
 * Обеспечивает явную обработку успешного и ошибочного исходов на уровне компиляции.
 *
 * @param T Тип данных успешного результата
 *
 * Пример использования:
 * ```
 * when (result) {
 *     is ResponseResult.Success -> processData(result.data)
 *     is ResponseResult.Error -> showError(result.stacktrace)
 * }
 * ```
 */
sealed class ResponseResult<out T> {

    /**
     * Представляет успешное выполнение сетевой операции с полученными данными.
     *
     * @property data Успешно полученные данные типа [T]
     * @param T Тип содержащихся данных
     */
    data class Success<out T>(val data: T) : ResponseResult<T>()

    /**
     * Представляет неудачное выполнение сетевой операции с деталями ошибки.
     *
     * @property stacktrace Строковое представление стектрейса ошибки.
     *                         Обычно содержит детали исключения и трассировку вызовов.
     */
    data class Error(val stacktrace: String) : ResponseResult<Nothing>()
}
