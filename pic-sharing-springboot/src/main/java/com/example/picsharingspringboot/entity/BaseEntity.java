package com.example.picsharingspringboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class BaseEntity implements Serializable{
    private static final long serialVersionUID = -5549498079990275465L;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
