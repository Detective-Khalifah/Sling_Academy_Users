package com.blogspot.thengnet.slingacademyusers

data class User(
    var id: Int,
    var firstName: String,
    var lastName: String,
    var dateOfBirth: String,
    var gender: String,
    var eMail: String,
    var phoneNumber: String,
    var country: String,
    var state: String,
    var city: String,
    var street: String,
    var theLatitude: Double,
    var theLongitude: Double,
    var zipcode: String,
    var job: String,
)
