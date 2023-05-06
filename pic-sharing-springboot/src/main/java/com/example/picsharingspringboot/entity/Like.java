package com.example.picsharingspringboot.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Like extends BaseEntity{
    private static final long serialVersionUID = -757374963678650138L;
    private Integer id;
    private Integer userId;
    private Integer illustrationId;
}
