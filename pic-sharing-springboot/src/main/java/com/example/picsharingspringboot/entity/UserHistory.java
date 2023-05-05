package com.example.picsharingspringboot.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UserHistory extends BaseEntity{
    private static final long serialVersionUID = 5083196609843718635L;
    private Integer id;
    private Integer userId;
    private Integer illustrationId;
}
