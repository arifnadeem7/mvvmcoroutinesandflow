package com.arif.kotlincoroutinesplusflow.base

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    fun showSnackBar(view: View, message: String) =
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

    /**
     * Handle visibility of a View based on the Progress state being passed in
     *
     * By default View will be shown if Progress is loading, otherwise it will be hidden
     *
     * Default parameter *reverse* does the opposite, it will hide a View if Progress is loading
     * and will show it otherwise.
     */
    fun toggleVisibility(
        progress: Progress,
        shouldHide: Boolean = false,
        reverse: Boolean = false
    ) =
        when (progress.isLoading) {
            true -> if (!reverse) View.VISIBLE else {
                if (shouldHide) View.INVISIBLE else View.GONE
            }
            false -> if (!reverse) {
                if (shouldHide) View.INVISIBLE else View.GONE
            } else View.VISIBLE
        }
}
