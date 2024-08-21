package certis.CertisHomepage.common;

import certis.CertisHomepage.domain.entity.ImageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FileHandler {

    public List<ImageEntity> parseFileInfo(List<MultipartFile> multipartFiles) throws IOException {
        List<ImageEntity> fileList = new ArrayList<>();
        if(multipartFiles.isEmpty()){
            log.info("파일이 비었음");
            return fileList;
        }

        //비어있지않다면
        if(!CollectionUtils.isEmpty(multipartFiles)){
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String currentDate = now.format(dateTimeFormatter);

            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            //프로젝트  폴더 내에 photo폴더 생성해서 그안에 현재 날짜로 파일생성
            String path = "photo" + File.separator + currentDate;
            File file = new File(path);

            //경로가존재하지않으면 디렉토리 생성함
            if(!file.exists()){
                file.mkdirs();
            }

            for (MultipartFile m : multipartFiles){
                String originalFileExtension;
                String contentType = m.getContentType();

                if(ObjectUtils.isEmpty(contentType)){
                    break;
                }else{
                    if(contentType.contains("image/jpeg")){
                        originalFileExtension = ".jpg";
                    }else if(contentType.contains("image/png")){
                        originalFileExtension = ".png";
                    }else if(contentType.contains("image/gif")){
                        originalFileExtension = ".gif";
                    }else {
                        log.error("jpg, png, gif파일 외 다른 파일 형식이 들어옴");
                        break;
                    }
                }

                //파일 이름 중복안되게 나노단위로
                String newFileName = System.nanoTime() + originalFileExtension;
                ImageEntity image = ImageEntity.builder()
                        .originalImgName(m.getOriginalFilename())
                        .imgUrl(path + File.separator + newFileName)
                        .build();

                fileList.add(image);

                file = new File(absolutePath + path + File.separator + newFileName);
                m.transferTo(file);

                file.setWritable(true);
                file.setReadable(true);
            }
        }
        return fileList;
    }
}
