    package com.example.systemfayl.model;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import javax.persistence.Entity;
    import javax.persistence.GeneratedValue;
    import javax.persistence.GenerationType;
    import javax.persistence.Id;
    import java.util.UUID;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    public class Attachment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id ;

        private String orginalName ;

        private Long size ;

        private String name ;

        private String contentType ;


    }











