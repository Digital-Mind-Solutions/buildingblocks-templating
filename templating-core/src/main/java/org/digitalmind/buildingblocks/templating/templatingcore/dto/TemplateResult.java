package org.digitalmind.buildingblocks.templating.templatingcore.dto;

import lombok.*;
import org.springframework.core.io.Resource;

import javax.activation.MimeType;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class TemplateResult {
    private MimeType mimeType;
    private Resource resource;
}
