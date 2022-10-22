package com.example.arduino_data

import android.os.Parcel
import android.os.Parcelable

data class Data(
    val field1: String,
    val field1Date: String,

    val field2: String,
    val field2Date: String
): Parcelable {
    constructor(parcel: Parcel) : this (
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
        )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }
}
