package com.example.picsharingspringboot.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Follow extends BaseEntity{
    private static final long serialVersionUID = -5248477981871075021L;
    private Integer id;
    private Integer followerId;
    private Integer followingId;
}
