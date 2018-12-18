package com.tdi.northwind.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    @Value("${user.upload.location}")
    private String pathFile;
    public void store(MultipartFile file, Integer id){
        try{
            Files.copy(file.getInputStream(), Paths.get(pathFile + id + ".png"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("FAIL!");
        }
    }
}
