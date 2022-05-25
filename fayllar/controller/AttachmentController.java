    package com.example.fayllar.controller;

    import com.example.fayllar.model.Attachment;
    import com.example.fayllar.model.AttachmentContent;
    import com.example.fayllar.repository.AttachmentContentRepository;
    import com.example.fayllar.repository.AttachmentRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.util.FileCopyUtils;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RestController;
    import org.springframework.web.multipart.MultipartFile;
    import org.springframework.web.multipart.MultipartHttpServletRequest;

    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;
    import java.util.Iterator;
    import java.util.List;
    import java.util.Optional;

    @RestController
    public class AttachmentController {

        @Autowired
        private AttachmentRepository attachmentRepository;
        @Autowired
        private AttachmentContentRepository attachmentContentRepository ;

        @GetMapping("/info")
        public List<Attachment> getAll(){
            return attachmentRepository.findAll();
        }

        @GetMapping("/info/{id}")
        public Attachment getOne(@PathVariable Integer id){
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
            if (optionalAttachment.isPresent())
                return optionalAttachment.get();
            return new Attachment();
        }

        @PostMapping("/upload")
        public String saveFile(MultipartHttpServletRequest request) throws IOException {
                Iterator<String> fileNames = request.getFileNames();
                MultipartFile file = request.getFile(fileNames.next());
                if(!file.isEmpty()){
                Attachment attachment = new Attachment();
                attachment.setFileOrginalName(file.getOriginalFilename());
                attachment.setContentType(file.getContentType());
                attachment.setFileSize(file.getSize());
                Attachment saveAttachment = attachmentRepository.save(attachment);

                AttachmentContent attachmentContent = new AttachmentContent() ;

                attachmentContent.setOrginalFile(file.getBytes());
                attachmentContent.setAttachment(saveAttachment);
                attachmentContentRepository.save(attachmentContent);
                return "Saved ";
                }
                return "Error ";


        }
        @GetMapping("/download/{id}")
        public void download(@PathVariable Integer id , HttpServletResponse response ) throws IOException {
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);

            if (optionalAttachment.isPresent()){
                Optional<AttachmentContent> optionalAttachmentContent = attachmentContentRepository.findById(id);
                if (optionalAttachmentContent.isPresent()){
                    AttachmentContent attachmentContent = optionalAttachmentContent.get();
                    response.setHeader("Content-Disposition","attachment ; filename = \""+optionalAttachment.get().getFileOrginalName()+"\"");
                response.setContentType(optionalAttachment.get().getContentType());
                    FileCopyUtils.copy(attachmentContent.getOrginalFile(),response.getOutputStream());

                }

            }

        }
    }