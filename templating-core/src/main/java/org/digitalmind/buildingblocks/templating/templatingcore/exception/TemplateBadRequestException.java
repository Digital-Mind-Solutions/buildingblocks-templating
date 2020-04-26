package org.digitalmind.buildingblocks.templating.templatingcore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TemplateBadRequestException extends TemplateException {
    public TemplateBadRequestException() {
    }

    public TemplateBadRequestException(String message) {
        super(message);
    }

    public TemplateBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateBadRequestException(Throwable cause) {
        super(cause);
    }

    public TemplateBadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
