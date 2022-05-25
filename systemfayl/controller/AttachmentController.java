    package com.example.systemfayl.controller;


    import com.example.systemfayl.model.Attachment;
    import com.example.systemfayl.repository.AttachmentRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.util.FileCopyUtils;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.multipart.MultipartHttpServletRequest;

    import javax.servlet.http.HttpServletResponse;
    import java.io.FileInputStream;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.Iterator;
    import java.util.List;
    import java.util.Optional;
    import java.util.UUID;

    @RestController
    @RequestMapping("/attachment")
    public class AttachmentController {

        @Autowired
        private AttachmentRepository attachmentRepository ;

        private static final String urlFiles = "downloadFiles";
        @PostMapping
        public String save(MultipartHttpServletRequest request ){
            try {
                Iterator<String> fileNames = request.getFileNames();
                MultipartFile file = request.getFile(fileNames.next());

                Attachment attachment = new Attachment();
                attachment.setSize(file.getSize());
                String [] type = file.getOriginalFilename().split("\\.");
                String name = UUID.randomUUID().toString()+"."+type[type.length-1];
                attachment.setName(name);
                attachment.setOrginalName(file.getOriginalFilename());
                attachment.setContentType(file.getContentType());
                attachmentRepository.save(attachment);
                Path path = Paths.get(urlFiles+ "/"+name);
                Files.copy(file.getInputStream(),path);

                return "Save ";
            }
            catch (Exception e){
             return "Error ";
            }
        }

        @GetMapping
        public List<Attachment> getAll(){
            return attachmentRepository.findAll();
        }

        @GetMapping("/download/{id}")
        public void download(@PathVariable Integer id, HttpServletResponse response){

            try {
                Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
                if (optionalAttachment.isPresent()){
                    Attachment attachment = optionalAttachment.get();

                    response.setHeader("Content-Disposition","attachment; filename=\""+attachment.getName()+"\"");

                    response.setContentType(attachment.getContentType());

                    FileInputStream fileInputStream = new FileInputStream(urlFiles+"/"+attachment.getName());
                    FileCopyUtils.copy(fileInputStream,response.getOutputStream());
                }



            }catch (Exception e){

                response.setHeader("Error","Error this");
            }



        }

    }
