package com.example.systemfayl.controller;

import com.example.systemfayl.entity.Attachment;
import com.example.systemfayl.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.UUID;

@RestController
public class AttachmentController {

    @Autowired
    private AttachmentRepository attachmentRepository ;

    private static final String pathFile="uploads";
    @PostMapping("/upload")
    public String save(MultipartHttpServletRequest request){

        try {

            Iterator<String> fileNames = request.getFileNames();
            while (fileNames.hasNext()){
                MultipartFile file = request.getFile(fileNames.next());
                if (!file.isEmpty()){
                    String type[] = file.getOriginalFilename().split("\\.");
                    String name = UUID.randomUUID().toString()+"."+type[type.length-1];

                    Attachment attachment = new Attachment();

                    attachment.setContentType(file.getContentType());
                    attachment.setOrginalName(file.getOriginalFilename());
                    attachment.setSize(file.getSize());
                    attachment.setName(name);
                    attachmentRepository.save(attachment);

                    Path path = Paths.get(pathFile+"/"+name);
                    Files.copy(file.getInputStream(),path);

                }
                else return "Null point exception";
            }


            return "Save file  ";
        }catch (Exception e){
            return "Error";
        }
    }


}
