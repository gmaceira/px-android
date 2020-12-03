package com.mercadopago.android.px.internal.features.payment_result

import androidx.annotation.ColorRes
import com.mercadopago.android.px.internal.base.MvpView
import com.mercadopago.android.px.internal.features.payment_result.presentation.PaymentResultFooter
import com.mercadopago.android.px.internal.features.payment_result.viewmodel.PaymentResultViewModel
import com.mercadopago.android.px.internal.view.ActionDispatcher
import com.mercadopago.android.px.internal.view.PaymentResultBody
import com.mercadopago.android.px.internal.viewmodel.PaymentModel
import com.mercadopago.android.px.model.exceptions.ApiException

internal interface PaymentResult {
    interface View : MvpView {
        fun configureViews(model: PaymentResultViewModel, paymentModel: PaymentModel, listener: Listener,
            footerListener: PaymentResultFooter.Listener)
        fun showApiExceptionError(exception: ApiException, requestOrigin: String)
        fun showInstructionsError()
        fun openLink(url: String)
        fun finishWithResult(resultCode: Int, backUrl: String?, redirectUrl: String?)
        fun changePaymentMethod()
        fun recoverPayment()
        fun copyToClipboard(content: String)
        fun setStatusBarColor(@ColorRes color: Int)
        fun launchDeepLink(deepLink: String)
        fun processCrossSellingBusinessAction(deepLink: String)
        fun updateAutoReturnLabel(label: String)
    }

    interface Presenter {
        fun onFreshStart()
        fun onAbort()
        fun onStart()
        fun onStop()
    }

    interface Listener : PaymentResultBody.Listener, ActionDispatcher
}
