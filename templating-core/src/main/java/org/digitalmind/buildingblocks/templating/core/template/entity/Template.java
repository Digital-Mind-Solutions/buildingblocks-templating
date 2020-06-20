package org.digitalmind.buildingblocks.templating.core.template.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.digitalmind.buildingblocks.core.jpautils.entity.ContextVersionableAuditModel;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

@ApiModel(value = "Template", description = "templates defined in the signing process.")
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
    @ApiModelProperty(value = "Unique id of the template", required = false)
    private Long id;

    @ApiModelProperty(value = "The template engine", required = true)
    @Column(name = "engine")
    @NotNull
    private String engine;

    @ApiModelProperty(value = "The template namespace", required = true)
    @Column(name = "namespace", length = 750)
    @NotNull
    private String namespace;

    @ApiModelProperty(value = "The template name", required = true)
    @Column(name = "name", length = 256)
    @NotNull
    private String name;

    @ApiModelProperty(value = "The template description", required = false)
    @Column(name = "description")
    private String description;

    @ApiModelProperty(value = "The template content", required = true)
    @Column(name = "content")
    @NotNull
    private String content;

    @ApiModelProperty(value = "The template content type", required = true)
    @Column(name = "content_type")
    @NotNull
    private String contentType;

    @ApiModelProperty(value = "The template supported result mime types ", required = true)
    @Column(name = "result_mime_types")
    @NotNull
    @ElementCollection
    @CollectionTable(name = "template_resultmimetypes")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> resultMimeTypes = new ArrayList<>();

}
