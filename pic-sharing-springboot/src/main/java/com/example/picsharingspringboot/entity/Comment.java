package com.example.picsharingspringboot.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Comment extends BaseEntity{
    private static final long serialVersionUID = 1221628594358300492L;
    private Integer id;
    private Integer userId;
    private Integer illustrationId;
    private String content;
    private byte[] avatar;
    private String username;
}
