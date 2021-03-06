package com.woocommerce.android.ui.products

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.annotation.StringRes
import com.woocommerce.android.R

sealed class ProductBackorderStatus(@StringRes val stringResource: Int = 0, val value: String = "") : Parcelable {
    object No : ProductBackorderStatus(R.string.product_backorders_no)
    object Yes : ProductBackorderStatus(R.string.product_backorders_yes)
    object Notify : ProductBackorderStatus(R.string.product_backorders_notify)
    object NotAvailable : ProductBackorderStatus()
    class Custom(value: String) : ProductBackorderStatus(value = value)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        fun fromString(value: String?): ProductBackorderStatus {
            return when (value) {
                "no" -> No
                "yes" -> Yes
                "notify" -> Notify
                null, "" -> NotAvailable
                else -> Custom(value)
            }
        }

        @JvmField
        val CREATOR = object : Creator<ProductBackorderStatus> {
            override fun createFromParcel(parcel: Parcel): ProductBackorderStatus {
                return fromString(parcel.readString())
            }

            override fun newArray(size: Int): Array<ProductBackorderStatus?> {
                return arrayOfNulls(size)
            }
        }
    }
}
