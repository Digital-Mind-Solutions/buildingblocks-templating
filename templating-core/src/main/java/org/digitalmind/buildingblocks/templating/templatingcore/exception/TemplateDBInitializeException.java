package org.digitalmind.buildingblocks.templating.templatingcore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class TemplateDBInitializeException extends TemplateException {
    public TemplateDBInitializeException() {
    }

    public TemplateDBInitializeException(String message) {
        super(message);
    }

    public TemplateDBInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateDBInitializeException(Throwable cause) {
        super(cause);
    }

    public TemplateDBInitializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
