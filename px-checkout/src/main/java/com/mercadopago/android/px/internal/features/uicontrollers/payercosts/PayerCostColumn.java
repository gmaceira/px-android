package com.mercadopago.android.px.internal.features.uicontrollers.payercosts;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.util.CurrenciesUtil;
import com.mercadopago.android.px.internal.view.MPTextView;
import com.mercadopago.android.px.model.Site;
import java.math.BigDecimal;

public class PayerCostColumn {

    private final Site site;
    private final Context context;
    private View view;
    private MPTextView installmentsTextView;
    private MPTextView zeroRateText;
    private MPTextView totalText;

    private final BigDecimal installmentsRate;
    private final BigDecimal installmentsAmount;
    private final Integer installments;

    public PayerCostColumn(final Context context, final Site site, final BigDecimal installmentsRate,
        final BigDecimal installmentsAmount, final Integer installments) {
        this.context = context;
        this.site = site;
        this.installmentsRate = installmentsRate;
        this.installmentsAmount = installmentsAmount;
        this.installments = installments;
    }

    public void drawPayerCostWithoutTotal() {
        drawBasicPayerCost();
        hideTotalAmount();
        alignRight();
    }

    private void drawBasicPayerCost() {
        setInstallmentsText();

        if (!site.shouldWarnAboutBankInterests()) {
            if (installmentsRate.compareTo(BigDecimal.ZERO) == 0) {
                if (installments > 1) {
                    zeroRateText.setVisibility(View.VISIBLE);
                } else {
                    zeroRateText.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setInstallmentsText() {
        final Spanned spannedInstallmentsText =
            CurrenciesUtil.getSpannedAmountWithCurrencySymbol(installmentsAmount, site.getCurrencyId());
        final String x = installmentsTextView.getContext().getString(R.string.px_installments_by);
        installmentsTextView
            .setText(new SpannableStringBuilder(installments.toString()).append(x).append(" ")
                .append(spannedInstallmentsText));
    }

    public void setOnClickListener(final View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }

    public void initializeControls() {
        installmentsTextView = view.findViewById(R.id.mpsdkInstallmentsText);
        zeroRateText = view.findViewById(R.id.mpsdkInstallmentsZeroRate);
        totalText = view.findViewById(R.id.mpsdkInstallmentsTotalAmount);
    }

    public View inflateInParent(final ViewGroup parent, final boolean attachToRoot) {
        view = LayoutInflater.from(context)
            .inflate(R.layout.px_column_payer_cost, parent, attachToRoot);
        return view;
    }

    public View getView() {
        return view;
    }

    private void hideTotalAmount() {
        totalText.setVisibility(View.GONE);
    }

    private void alignRight() {
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        zeroRateText.setLayoutParams(params);
    }
}
