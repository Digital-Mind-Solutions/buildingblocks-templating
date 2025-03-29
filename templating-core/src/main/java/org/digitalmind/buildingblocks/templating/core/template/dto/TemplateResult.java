package org.digitalmind.buildingblocks.templating.core.template.dto;

import jakarta.activation.MimeType;
import lombok.*;
import org.springframework.core.io.Resource;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class TemplateResult {
    private MimeType mimeType;
    private Resource resource;
}
