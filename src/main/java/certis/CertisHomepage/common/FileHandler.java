package certis.CertisHomepage.common;

import certis.CertisHomepage.domain.entity.ImageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDate = now.format(dateTimeFormatter);

        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

        //프로젝트  폴더 내에 photo폴더 생성해서 그안에 현재 날짜로 파일생성
        String path;
        for (MultipartFile m : multipartFiles){
            String originalFileExtension;
            String contentType = m.getContentType();

            if(ObjectUtils.isEmpty(contentType)){
                break;
            }else{
                if(contentType.contains("image/jpeg")){
                    originalFileExtension = ".jpg";
                    path = "photo" + File.separator + currentDate;
                }else if(contentType.contains("image/png")){
                    originalFileExtension = ".png";
                    path = "photo" + File.separator + currentDate;
                }else if(contentType.contains("image/gif")){
                    originalFileExtension = ".gif";
                    path = "photo" + File.separator + currentDate;
                }else if(contentType.contains("application/pdf")){
                    originalFileExtension = ".pdf";
                    path = "document" + File.separator + currentDate;
                }else if(contentType.contains("application/vnd.ms-excel") || contentType.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
                    originalFileExtension = ".xls";
                    path = "document" + File.separator + currentDate;
                }else if (contentType.contains("application/x-hwp") || contentType.contains("application/hwp")) {
                    originalFileExtension = ".hwp";
                    path = "document" + File.separator + currentDate;
                } else if (contentType.contains("application/msword") || contentType.contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                    originalFileExtension = ".doc"; // DOC 및 DOCX 파일 모두 처리
                    path = "document" + File.separator + currentDate;
                }
                else {
                    log.error("지원하지 않는 파일 형식: {}", contentType);
                    break;
                }
            }
            File direc = new File(path);

            //경로가존재하지않으면 디렉토리 생성함
            if(!direc.exists()){
                direc.mkdirs();
            }

            //파일 이름 중복안되게 나노단위로
            String newFileName = System.nanoTime() + originalFileExtension;
            ImageEntity image = ImageEntity.builder()
                    .originalImgName(m.getOriginalFilename())
                    .imgUrl(path + File.separator + newFileName)
                    .build();

            fileList.add(image);

            direc = new File(absolutePath + path + File.separator + newFileName);
            m.transferTo(direc);

            direc.setWritable(true);
            direc.setReadable(true);
        }
        return fileList;
    }



}
