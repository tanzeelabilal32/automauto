package com.sema.shared_test.data

import com.sema.data.model.CarSearchItem
import com.sema.data.model.Image
import com.sema.data.model.Seller

object TestData {

    val mockCars = listOf(
        CarSearchItem(
            colour = null,
            description = "Almost like new. Full service history",
            firstRegistration = "01-2000",
            fuel = "Gasoline",
            id = 1,
            images = listOf(Image("https://loremflickr.com/g/320/240/bmw")),
            make = "BMW",
            mileage = 25000,
            model = "316i",
            modelline = "3 series",
            price = 13000,
            seller = null
        ),
        CarSearchItem(
            colour = "Brown",
            description = "Engine replaced at 180000km.",
            firstRegistration = null,
            fuel = "Gasoline",
            id = 2,
            images = listOf(Image("https://loremflickr.com/g/320/240/bmw")),
            make = "Porsche",
            mileage = 4500,
            model = "911",
            modelline = "3 series",
            price = 100000,
            seller = Seller("Köln", "+123456789", "Private")
        )
    )
}