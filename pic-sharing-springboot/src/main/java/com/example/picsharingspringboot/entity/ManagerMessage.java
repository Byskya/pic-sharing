package com.example.picsharingspringboot.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ManagerMessage extends BaseEntity{
    private static final long serialVersionUID = 1971667322318947098L;
    private Integer id;
    private String content;
}
