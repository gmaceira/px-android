package com.mercadopago.android.px.internal.features.business_result

import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.features.payment_congrats.model.CongratsViewModelMapper
import com.mercadopago.android.px.internal.features.payment_congrats.model.PaymentCongratsModel
import com.mercadopago.android.px.internal.features.payment_congrats.model.PaymentCongratsModel.CongratsType
import com.mercadopago.android.px.internal.features.payment_congrats.model.PaymentCongratsResponse
import com.mercadopago.android.px.internal.features.payment_result.CongratsAutoReturn
import com.mercadopago.android.px.internal.features.payment_result.presentation.PaymentResultButton
import com.mercadopago.android.px.internal.features.payment_result.presentation.PaymentResultFooter
import com.mercadopago.android.px.internal.view.PaymentResultBody
import com.mercadopago.android.px.internal.view.PaymentResultHeader
import com.mercadopago.android.px.internal.view.PaymentResultMethod
import com.mercadopago.android.px.internal.viewmodel.GenericLocalized
import com.mercadopago.android.px.internal.viewmodel.PaymentResultType
import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper
import java.util.*

internal class BusinessPaymentResultMapper : Mapper<PaymentCongratsModel, BusinessPaymentResultViewModel>() {

    override fun map(model: PaymentCongratsModel): BusinessPaymentResultViewModel {
        return BusinessPaymentResultViewModel(
            getHeaderModel(model),
            getBodyModel(model),
            getFooterModel(model),
            getAutoReturnModel(model.paymentCongratsResponse?.autoReturn)
        )
    }

    private fun getBodyModel(model: PaymentCongratsModel): PaymentResultBody.Model {
        val methodModels: MutableList<PaymentResultMethod.Model> = ArrayList()
        if (model.shouldShowPaymentMethod == true) {
            for (paymentInfo in model.paymentsInfo) {
                methodModels.add(PaymentResultMethod.Model.with(paymentInfo,
                    model.statementDescription))
            }
        }
        val type = model.congratsType
        return PaymentResultBody.Model.Builder()
            .setMethodModels(methodModels)
            .apply { model.paymentCongratsResponse?.let {
                setCongratsViewModel(CongratsViewModelMapper(BusinessPaymentResultTracker()).map(it))
            }}
            .apply {
                if (type == CongratsType.APPROVED && model.shouldShowReceipt == true) setReceiptId(model.paymentId.toString())
            }
            .setHelp(model.help)
            .setStatement(model.statementDescription)
            .setTopFragment(model.topFragment)
            .setBottomFragment(model.bottomFragment)
            .setImportantFragment(model.importantFragment)
            .build()
    }

    private fun getHeaderModel(model: PaymentCongratsModel): PaymentResultHeader.Model {
        val type = PaymentResultType.from(model.congratsType)
        return PaymentResultHeader.Model.Builder()
            .setIconUrl(model.imageUrl)
            .setIconImage(if (model.iconId == 0) R.drawable.px_icon_product else model.iconId)
            .setBackground(type.resColor)
            .setBadgeImage(type.badge)
            .setTitle(GenericLocalized(model.title, 0))
            .setLabel(GenericLocalized(model.subtitle, type.message))
            .build()
    }

    private fun getFooterModel(model: PaymentCongratsModel): PaymentResultFooter.Model {
        return PaymentResultFooter.Model(
            model.footerMainAction?.let {
                PaymentResultButton(PaymentResultButton.Type.LOUD, it)
            },
            model.footerSecondaryAction?.let {
                PaymentResultButton(PaymentResultButton.Type.TRANSPARENT, it)
            }
        )
    }

    private fun getAutoReturnModel(autoReturn: PaymentCongratsResponse.AutoReturn?): CongratsAutoReturn.Model? {
        return autoReturn?.let {
            CongratsAutoReturn.Model(it.label, it.seconds)
        }
    }
}
