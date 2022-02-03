package com.graphicauth.authservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Setter
@Getter
@Entity
public class Image {
    @Id
    @GeneratedValue
    Long id;

    @Lob
    byte[] content;

    String name;

    String memType;
}
