package com.sema.automauto.domain.util

sealed class FilterType(val sortingType: SortingType) {

    abstract fun copy(sortingType: SortingType): FilterType

    class Title(orderType: SortingType) : FilterType(orderType) {
        override fun copy(sortingType: SortingType): Title {
            return Title(sortingType)
        }
    }

    class Date(orderType: SortingType) : FilterType(orderType) {
        override fun copy(sortingType: SortingType): Date {
            return Date(sortingType)
        }
    }

    class Color(orderType: SortingType) : FilterType(orderType) {
        override fun copy(sortingType: SortingType): Color {
            return Color(sortingType)
        }
    }

    class Price(orderType: SortingType) : FilterType(orderType) {
        override fun copy(sortingType: SortingType): Price {
            return Price(sortingType)
        }
    }
}
