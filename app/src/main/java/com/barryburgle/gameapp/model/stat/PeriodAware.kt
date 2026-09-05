package com.barryburgle.gameapp.model.stat

interface PeriodAware {
    var yearNumber: Int
    var periodNumber: Int
    var label: String
}