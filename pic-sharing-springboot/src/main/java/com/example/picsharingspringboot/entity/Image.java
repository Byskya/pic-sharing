package com.example.picsharingspringboot.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Image extends BaseEntity{
    private static final long serialVersionUID = 8613159143285171699L;
    private Integer id;
    private String url;
}
