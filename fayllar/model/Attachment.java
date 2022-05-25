package com.example.fayllar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    @Column(nullable = false)
    private String fileOrginalName ;
    @Column(nullable = false)
    private String contentType ;
    @Column(nullable = false)
    private Long fileSize ;


}
