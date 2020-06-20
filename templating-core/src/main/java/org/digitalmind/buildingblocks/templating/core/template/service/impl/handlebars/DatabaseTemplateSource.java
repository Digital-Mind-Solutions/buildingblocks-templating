package org.digitalmind.buildingblocks.templating.core.template.service.impl.handlebars;

import com.github.jknack.handlebars.io.AbstractTemplateSource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class DatabaseTemplateSource extends AbstractTemplateSource {

    private final String content;
    private final String filename;
    private final long lastModified;

    public DatabaseTemplateSource(String content, String filename, long lastModified) {
        this.content = content;
        this.filename = filename;
        this.lastModified = lastModified;
    }

    @Override
    public String content(Charset charset) throws IOException {
        return this.content;
    }

    @Override
    public String filename() {
        return this.filename;
    }

    @Override
    public long lastModified() {
        return this.lastModified;
    }

}
