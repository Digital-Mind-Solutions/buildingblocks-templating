package org.digitalmind.buildingblocks.templating.core.template.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class TemplateEngineNotInitializedException extends TemplateException {

    public TemplateEngineNotInitializedException() {
    }

    public TemplateEngineNotInitializedException(String message) {
        super(message);
    }

    public TemplateEngineNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateEngineNotInitializedException(Throwable cause) {
        super(cause);
    }

    public TemplateEngineNotInitializedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
