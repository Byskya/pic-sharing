package com.example.picsharingspringboot.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class IllustrationTag extends BaseEntity{
    private static final long serialVersionUID = 5317896381200852491L;
    private Integer id;
    private String tagName;
}
