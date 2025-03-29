package org.digitalmind.buildingblocks.templating.core.template.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.digitalmind.buildingblocks.core.jpautils.entity.ContextVersionableAuditModel;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static org.digitalmind.buildingblocks.templating.core.template.entity.Template.TABLE_NAME;

@Entity
@Table(
        name = TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = TABLE_NAME + "_ux1",
                        columnNames = {"name", "namespace"}
                )
        }
)
@EntityListeners({AuditingEntityListener.class})

@Data
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

@Schema(name = "Template", description = "templates defined in the signing process.")
@JsonPropertyOrder(
        {
                "id", "engine", "namespace", "name", "description",
                "content", "resultMimeTypes", "templateMimeType", /*"mimeTypes",*/
                "createdAt", "createdBy", "updatedAt", "updatedBy"
        }
)

public class Template extends ContextVersionableAuditModel {

    public static final String TABLE_NAME = "template";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Schema(name = "Unique id of the template")
    private Long id;

    @Schema(name = "The template engine")
    @Column(name = "engine")
    @NotNull
    private String engine;

    @Schema(name = "The template namespace")
    @Column(name = "namespace", length = 750)
    @NotNull
    private String namespace;

    @Schema(name = "The template name")
    @Column(name = "name", length = 256)
    @NotNull
    private String name;

    @Schema(name = "The template description")
    @Column(name = "description")
    private String description;

    @Schema(name = "The template content")
    @Column(name = "content")
    @NotNull
    private String content;

    @Schema(name = "The template content type")
    @Column(name = "[content_type]")
    @NotNull
    private String contentType;

    @Schema(name = "The template supported result mime types ")
    @Column(name = "result_mime_types")
    @NotNull
    @ElementCollection
    @CollectionTable(name = "template_resultmimetypes")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> resultMimeTypes = new ArrayList<>();

}
