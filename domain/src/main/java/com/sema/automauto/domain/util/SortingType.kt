package com.sema.automauto.domain.util

sealed class SortingType {
    object Ascending: SortingType()
    object Descending: SortingType()
}
