package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.domain.Photo;
import com.rbbnbb.TediTry1.repository.PhotoRepository;
import com.rbbnbb.TediTry1.utils.PhotoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@Transactional
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    @Transactional
    public Photo saveImage(MultipartFile file, String directoryPath) throws IOException {
        String filePath = directoryPath + File.separator + file.getOriginalFilename();

        Photo newPhoto = new Photo(file.getOriginalFilename(), file.getContentType(),filePath);
        file.transferTo(new File(filePath).toPath());
        return photoRepository.save(newPhoto);
    }

    public byte[] getImageData(Photo photo) throws IOException{
        String filePath = photo.getFilePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
