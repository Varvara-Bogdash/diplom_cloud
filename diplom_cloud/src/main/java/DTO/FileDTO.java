package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;

import java.io.File;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private static Logger log;
    private Long id;
    private String filename;
    private byte[] content;
    private Long size;
    private LocalDateTime uploadDate;
    private Long userId;

    public static FileDTO fromEntity(File file) {
        log.debug("Converting File entity to DTO for file: {}", file.getFilename());
        return new FileDTO(
                file.getId(),
                file.getFilename(),
                file.getContent(),
                file.getSize(),
                file.getUploadDate(),
                file.getUser().getId()
        );
    }

    public static File toEntity(FileDTO fileDTO) {
        log.debug("Converting File DTO to entity for file: {}", fileDTO.getFilename());
        File file = new File();
        file.setFilename(fileDTO.getFilename());
        file.setContent(fileDTO.getContent());
        file.setSize(fileDTO.getSize());
        file.setUploadDate(fileDTO.getUploadDate());
        return file;
    }
}
