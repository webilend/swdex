package it.webilend.swdex.model

data class SWAPIResponse<T>(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<T>
)