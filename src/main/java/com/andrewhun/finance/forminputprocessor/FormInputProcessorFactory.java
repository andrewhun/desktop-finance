/*
This file contains the FormInputProcessorFactory class, which is
responsible for instantiating FormInputProcessor objects.
 */

package com.andrewhun.finance.forminputprocessor;

public class FormInputProcessorFactory {

    public static FormInputProcessor createProcessor(FormController controller) {

        return new SimpleFormInputProcessor(controller);
    }

    public static<T> FormInputProcessor createProcessor(EditFormController<T> controller) {

        return new EditFormInputProcessor<>(controller);
    }
}
