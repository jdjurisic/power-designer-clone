package com.se.ucservice.controller;

import com.se.ucservice.model.DDocument;
import com.se.ucservice.model.NodeData;
import com.se.ucservice.service.TransformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/cm2code")
@CrossOrigin
public class TransformController {

    @Autowired
    TransformService transformService;

    @PostMapping(value="/transform", produces="application/zip")
    public void saveDocument(@Valid @RequestBody DDocument document, HttpServletResponse response) throws IOException {
        transformService.transformDocument(document);

        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        for (NodeData fileName : document.getNodeDataArray()) {
           // FileWriter myWriter = new FileWriter("C:\\Users\\Djordje\\Desktop\\DesignerPower_GeneratedClasses\\"+clas.getName() + ".java");
            FileSystemResource resource = new FileSystemResource("C:\\Users\\Djordje\\Desktop\\DesignerPower_GeneratedClasses\\" + fileName.getName() +".java");
            ZipEntry zipEntry = new ZipEntry(resource.getFilename());
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "perin_fajl" + "\"");
    }
        //return transformService.transformDocument(document);
}

    /*
    @GetMapping(value = "/zip-download", produces="application/zip")
    public void zipDownload(@RequestParam List<String> name, HttpServletResponse response) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        for (String fileName : name) {
            FileSystemResource resource = new FileSystemResource(fileBasePath + fileName);
            ZipEntry zipEntry = new ZipEntry(resource.getFilename());
            zipEntry.setSize(resource.contentLength());
            zipOut.putNextEntry(zipEntry);
            StreamUtils.copy(resource.getInputStream(), zipOut);
            zipOut.closeEntry();
        }
        zipOut.finish();
        zipOut.close();
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipFileName + "\"");
    }
    */
