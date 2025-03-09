package com.mock.myanalytics

import javax.inject.Inject

interface AnalyticsTracker {
    fun logEvent(eventName:String, params: Map<String, Any> )
}

class FBTracker @Inject constructor(): AnalyticsTracker {
    override fun logEvent(eventName: String, params: Map<String, Any>) {
        println("Logging Event: $eventName with params $params")

    }
}