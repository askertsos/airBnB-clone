package com.WebAppTechnologies.AirBnbClone.services;

import com.WebAppTechnologies.AirBnbClone.domain.Photo;
import com.WebAppTechnologies.AirBnbClone.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
@Transactional
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    @Transactional
    public Photo saveImage(MultipartFile file, String directoryPath) throws IOException {
        String filePath = directoryPath + "/" + file.getOriginalFilename();

        Optional<Photo> optionalPhoto = photoRepository.findByFilePath(filePath);
        if (optionalPhoto.isPresent()) return null;

        Photo newPhoto = new Photo(file.getOriginalFilename(), file.getContentType(),filePath);
        file.transferTo(new File(filePath).toPath());
        return photoRepository.save(newPhoto);
    }
}
