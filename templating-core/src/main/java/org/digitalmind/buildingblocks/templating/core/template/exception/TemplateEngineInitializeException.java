package org.digitalmind.buildingblocks.templating.core.template.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class TemplateEngineInitializeException extends TemplateException {
    public TemplateEngineInitializeException() {
    }

    public TemplateEngineInitializeException(String message) {
        super(message);
    }

    public TemplateEngineInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateEngineInitializeException(Throwable cause) {
        super(cause);
    }

    public TemplateEngineInitializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
