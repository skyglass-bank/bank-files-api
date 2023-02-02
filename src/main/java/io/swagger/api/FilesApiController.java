package io.swagger.api;

import io.swagger.model.FileRepository;

import io.swagger.model.File;
import io.swagger.model.Files;
import io.swagger.service.CloudStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-02-27T07:39:21.717Z[GMT]")
@RestController
public class FilesApiController implements FilesApi {

    private static final Logger log = LoggerFactory.getLogger(FilesApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;
    private FileRepository repository;

    @Value("${s3_bucket.bucket_name}")
    private String bucketName;

    @Autowired
    private CloudStorageService storageService;

    @Value("${upload.path}")
    private String uploadPath;

    @org.springframework.beans.factory.annotation.Autowired
    public FilesApiController(ObjectMapper objectMapper, HttpServletRequest request, FileRepository repository) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.repository = repository;
    }

    public ResponseEntity<File> createFile(@Parameter(in = ParameterIn.DEFAULT, description = "Send the File Object not the File content", required=true, schema=@Schema()) @Valid @RequestBody File body) {
        File file = this.repository.save(body);
        return new ResponseEntity<File>(file, HttpStatus.CREATED);
    }

    public ResponseEntity<byte[]> downloadFile(@Parameter(in = ParameterIn.PATH, description = "The id of the file to retrieve", required=true, schema=@Schema()) @PathVariable("fileId") String fileId,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "token", required = false) String token) throws IOException {
        Optional<File> fileObj = this.repository.findById(Long.parseLong(fileId));

        if (!fileObj.isPresent()) {
            return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
        }

        String objectName = fileId + "." + fileObj.get().getExtension();
        byte[] fileData = this.storageService.getEncryptedData(bucketName, objectName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(fileData, HttpStatus.OK);
    }

    public ResponseEntity<ListObjectsV2Result> listFiles(@Parameter(in = ParameterIn.QUERY, description = "How many items to return at one time (max 100)" ,schema=@Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit) {
        ListObjectsV2Result files = this.storageService.getFileList(this.bucketName);
        return new ResponseEntity<ListObjectsV2Result>(files, HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Boolean> recoverFile(@Parameter(in = ParameterIn.PATH, description = "The id of the file to recover", required=true, schema=@Schema()) @PathVariable("fileId") String fileId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Boolean>(objectMapper.readValue("false", Boolean.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Boolean>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Resource> shareableURL(@Parameter(in = ParameterIn.PATH, description = "The id of the file to retrieve", required=true, schema=@Schema()) @PathVariable("fileId") String fileId,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "userName", required = false) String userName,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "expires", required = false) String expires) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Resource>(objectMapper.readValue("\"\"", Resource.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Resource>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<File> showFileById(@Parameter(in = ParameterIn.PATH, description = "The id of the file to retrieve", required=true, schema=@Schema()) @PathVariable("fileId") String fileId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<File>(objectMapper.readValue("{\n  \"descr\" : \"descr\",\n  \"signature\" : \"signature\",\n  \"name\" : \"name\",\n  \"id\" : 0,\n  \"virus\" : true,\n  \"ownedBy\" : 6\n}", File.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<File>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<File>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> deleteFileById(@Parameter(in = ParameterIn.PATH, description = "The id of the file to retrieve", required=true, schema=@Schema()) @PathVariable("fileId") String fileId) {
        Optional<File> fileObj = this.repository.findById(Long.parseLong(fileId));

        if (!fileObj.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        File fileRecord = fileObj.get();
        String key = fileId + "." + fileRecord.getExtension();
        this.storageService.deleteFile(bucketName, key);
        this.repository.delete(fileRecord);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<String> uploadFile(@Parameter(in = ParameterIn.PATH, description = "The id of the file to retrieve", required=true, schema=@Schema()) @PathVariable("fileId") String fileId,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        Optional<File> fileObj = this.repository.findById(Long.parseLong(fileId));

        if (!fileObj.isPresent()) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        File fileRecord = fileObj.get();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        Map<String, String> extensionMap = new HashMap<>();
        extensionMap.put("jpeg", "image/jpeg");
        extensionMap.put("png", "image/png");
        extensionMap.put("jpg", "image/jpg");

        // Validate extension.
        if (!extensionMap.containsKey(extension)) {
            return new ResponseEntity<String>("Not supported extension.", HttpStatus.BAD_REQUEST);
        }

        // Validate mime type.
        if (!extensionMap.get(extension).equals(file.getContentType())) {
            return new ResponseEntity<String>("Invalid MIME Type.", HttpStatus.BAD_REQUEST);
        }

        // Validate content.
        Tika tika = new Tika();
 
        if (!extensionMap.get(extension).equals(tika.detect(file.getBytes()))) {
            return new ResponseEntity<String>("Invalid Content.", HttpStatus.BAD_REQUEST);
        }

        // Filename validate.
		String pattern = "[a-zA-Z_\\-\\.]{3,50}";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(file.getOriginalFilename());
		
        if (!m.matches()) {
            return new ResponseEntity<String>("Invalid Filename.", HttpStatus.BAD_REQUEST);
        }

        fileRecord.setExtension(extension);
        this.repository.save(fileRecord);

        String fileName = fileId + "." + extension;
        this.storageService.upload(bucketName, fileName, new BufferedInputStream(file.getInputStream()));

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }
}
