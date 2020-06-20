package org.digitalmind.buildingblocks.templating.core.template.service;

import lombok.extern.slf4j.Slf4j;
import org.digitalmind.buildingblocks.templating.core.template.model.TemplateDate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("templateUtil")
@Slf4j
public class TemplateUtilService {

    public TemplateDate with(Date date) {
        return new TemplateDate(date);
    }

    public String format(Date date, String format) {
        return this.with(date).format(format);
    }

    public String format(TemplateDate date, String format) {
        return date.format(format);
    }

}
