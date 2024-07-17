package certis.CertisHomepage.service;

import certis.CertisHomepage.domain.ImageEntity;
import certis.CertisHomepage.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;




}
