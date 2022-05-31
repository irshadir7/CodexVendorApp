package com.codex.ventorapp.foundatiion.utilz

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.gson.internal.bind.util.ISO8601Utils
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class Utils {

    companion object {
        const val CLIENT_ID = "lUcGfmdoGeo06BExzBcs17BWcdwYm2D9lq7U61Fg"
        const val CLIENT_SECRET = "BvRgGGTNTheZcdRfU1oMaowhtUAc7Tw5mpeL1sB3"
        const val GRANT_TYPE = "client_credentials"
        const val TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ss"
        private val PASSWORD_REGEX =
            Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])((?=.*[#?!@\$%^&<>*~:`-])|(?=.*[0-9])).{6,20}\$")
        private var duration: Long = 0

        fun CharSequence.isValidEmail(): Boolean {
            val matcher = android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            return matcher.matches()
        }

        fun CharSequence.isValidPassword(): Boolean {
            val matcher = PASSWORD_REGEX.matcher(this)
            return matcher.matches()
        }

        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }

        fun isCapsOrNot(showHideCapsView: View, s: CharSequence) {
            if (Pattern.matches("[a-z]", s)) {
                Log.e("", "CapsLock is OFF")
                showHideCapsView.visibility = View.GONE
            } else if (Pattern.matches("[A-Z]", s)) {
                Log.e("", "CapsLock is ON")
                showHideCapsView.visibility = View.VISIBLE
            } else if (Pattern.matches("[a-z][A-Z]", s)) {
                Log.e("", "CapsLock is ON")
                showHideCapsView.visibility = View.VISIBLE
            }
        }

        private fun Context.hideKeyboard(view: View) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }


        fun validateDecimalToString(value: Double, precision: Int): String {
            val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance()
            val decimalFormatSymbols: DecimalFormatSymbols =
                (numberFormat as DecimalFormat).decimalFormatSymbols
            decimalFormatSymbols.currencySymbol = ""
            numberFormat.decimalFormatSymbols = decimalFormatSymbols
            numberFormat.minimumFractionDigits = 2
            numberFormat.maximumFractionDigits = precision
            return numberFormat.format(value)
        }

        @SuppressLint("SimpleDateFormat")
        fun getCurrentDate(): String {
            val sdf = SimpleDateFormat(TIME_ZONE)
            return sdf.format(Date())
        }

        fun Fragment.showKeyboard() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ViewCompat.getWindowInsetsController(requireView())
                    ?.show(WindowInsetsCompat.Type.ime())
            } else {
                val focusedView = view?.findFocus() ?: view?.apply { requestFocus() }
                val imm =
                    (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                val isShowSucceeded =
                    imm.showSoftInput(focusedView, InputMethodManager.SHOW_IMPLICIT)
                if (!isShowSucceeded) {
                    imm.toggleSoftInputFromWindow(
                        view?.windowToken, 0, InputMethodManager.HIDE_IMPLICIT_ONLY
                    )
                }
            }

        }

        @SuppressLint("SimpleDateFormat")
        fun compareExpiryTime(expiryTime: String): Boolean {
            val currentTime = ISO8601Utils.format(Date())
            try {
                val formatter = SimpleDateFormat(TIME_ZONE)
                val expiryDate = formatter.parse(expiryTime)
                val currentDate = formatter.parse(currentTime)
                duration = expiryDate!!.time - currentDate!!.time
                if (duration > 0) {
                    return false
                } else if (duration < 0) {
                    return true
                }
            } catch (e: Exception) {
                Log.d("TimeZoneStatus", e.toString()) // this never gets called either
            }
            return false
        }
    }
}
