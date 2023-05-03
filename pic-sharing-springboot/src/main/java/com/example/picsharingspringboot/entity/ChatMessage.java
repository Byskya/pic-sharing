package com.example.picsharingspringboot.entity;

import com.example.picsharingspringboot.entity.BaseEntity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ChatMessage extends BaseEntity {
    private static final long serialVersionUID = 4934716624598598039L;
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String message;
    private boolean isRead;
    private boolean isDelete;
}
