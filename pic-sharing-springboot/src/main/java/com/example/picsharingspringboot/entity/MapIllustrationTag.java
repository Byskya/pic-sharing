package com.example.picsharingspringboot.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class MapIllustrationTag extends BaseEntity{
    private static final long serialVersionUID = 2825141239577736634L;
    private Integer id;
    private Integer illustrationId;
    private Integer tagId;
}
