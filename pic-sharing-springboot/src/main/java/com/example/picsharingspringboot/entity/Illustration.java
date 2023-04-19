package com.example.picsharingspringboot.entity;

import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Illustration extends BaseEntity{
    private static final long serialVersionUID = 5550287284535421826L;
    private Integer id;
    private String title;
    private String description;
    private Integer likes;
    private Integer views;
    private Integer userId;
    private Integer imageId;
    private String imageUrl;
    private byte[] imageResource;
    private List<IllustrationTag> tags;
}
