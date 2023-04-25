package com.example.picsharingspringboot.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Favorite extends BaseEntity{
    private static final long serialVersionUID = -3247483800791498037L;
    private Integer id;
    private Integer userId;
    private Integer illustrationId;
}
