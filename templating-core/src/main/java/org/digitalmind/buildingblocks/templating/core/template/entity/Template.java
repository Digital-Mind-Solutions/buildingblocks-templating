package org.digitalmind.buildingblocks.templating.core.template.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.digitalmind.buildingblocks.core.jpautils.entity.ContextVersionableAuditModel;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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

@Schema(description = "templates defined in the signing process.")
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
    @Schema(description = "Unique id of the template")
    private Long id;

    @Schema(description = "The template engine", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "engine")
    @NotNull
    private String engine;

    @Schema(description = "The template namespace", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "namespace", length = 750)
    @NotNull
    private String namespace;

    @Schema(description = "The template name", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "name", length = 256)
    @NotNull
    private String name;

    @Schema(description = "The template description")
    @JdbcTypeCode(SqlTypes.CLOB)
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Schema(description = "The template content", requiredMode = Schema.RequiredMode.REQUIRED)
    @JdbcTypeCode(SqlTypes.CLOB)
    @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false)
    @NotNull
    private String content;

    @Schema(description = "The template content type", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "[content_type]")
    @NotNull
    private String contentType;

    @Schema(description = "The template supported result mime types", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "result_mime_types")
    @NotNull
    @ElementCollection
    @CollectionTable(name = "template_resultmimetypes")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> resultMimeTypes = new ArrayList<>();

}
