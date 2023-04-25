package com.example.picsharingspringboot.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class AuditInfo extends BaseEntity {
    private static final long serialVersionUID = 2966487035156122157L;
    private Integer id;
    private Integer userId;
    private Integer illustrationId;
//    待审核，通过，未通过
    private String status;
    private String feedback;
}
