package org.digitalmind.buildingblocks.templating.core.template.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class TemplateExecutionException extends TemplateException {
    public TemplateExecutionException() {
    }

    public TemplateExecutionException(String message) {
        super(message);
    }

    public TemplateExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateExecutionException(Throwable cause) {
        super(cause);
    }

    public TemplateExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
