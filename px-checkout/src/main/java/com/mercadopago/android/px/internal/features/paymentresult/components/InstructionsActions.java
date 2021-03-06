package com.mercadopago.android.px.internal.features.paymentresult.components;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.features.paymentresult.props.InstructionsActionsProps;
import com.mercadopago.android.px.internal.view.ActionDispatcher;
import com.mercadopago.android.px.internal.view.Component;
import com.mercadopago.android.px.model.InstructionAction;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaserber on 11/14/17.
 */

public class InstructionsActions extends Component<InstructionsActionsProps, Void> {

    public InstructionsActions(@NonNull final InstructionsActionsProps props,
        @NonNull final ActionDispatcher dispatcher) {
        super(props, dispatcher);
    }

    public List<InstructionsAction> getActionComponents() {
        List<InstructionsAction> componentList = new ArrayList<>();

        for (InstructionAction actionInfo : props.instructionActions) {

            if (actionInfo.getTag().equals(InstructionAction.Tags.LINK)) {
                final InstructionsAction.Prop actionProp = new InstructionsAction.Prop.Builder()
                    .setInstructionAction(actionInfo)
                    .build();
                final InstructionsAction component = new InstructionsAction(actionProp, getDispatcher());
                componentList.add(component);
            }
        }

        return componentList;
    }
}
