package controller;
import DTO.FileDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import service.FileService;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final FileService fileService;

    @PostMapping
    public ResponseEntity<FileDTO> uploadFile(@RequestBody FileDTO fileDTO,
                                              @RequestHeader("X-User-Id") Long userId) {
        log.info("Received file upload request: {} from user id: {}", fileDTO.getFilename(), userId);
        try {
            File file = fileService.saveFile(
                    fileDTO.getFilename(),
                    fileDTO.getContent(),
                    userId,
                    fileDTO.getSize()
            );

            FileDTO responseDTO = FileDTO.fromEntity(file);
            log.info("File uploaded successfully: {}", file.getFilename());
            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            log.error("Error uploading file {}: {}", fileDTO.getFilename(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<FileDTO>> getAllFiles(@RequestHeader("X-User-Id") Long userId) {
        log.info("Retrieving all files for user id: {}", userId);
        try {
            List<File> files = fileService.getAllFiles(userId);
            List<FileDTO> fileDTOs = files.stream()
                    .map(FileDTO::fromEntity)
                    .collect(Collectors.toList());

            log.info("Retrieved {} files for user id: {}", fileDTOs.size(), userId);
            return ResponseEntity.ok(fileDTOs);

        } catch (Exception e) {
            log.error("Error retrieving files for user id {}: {}", userId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<Void> deleteFile(@PathVariable String filename,
                                           @RequestHeader("X-User-Id") Long userId) {
        log.info("Received request to delete file: {} for user id: {}", filename, userId);
        try {
            File file = fileService.findByFilenameAndUserId(filename, userId)
                    .orElseThrow(() -> {
                        log.warn("File not found for deletion: {} for user id: {}", filename, userId);
                        return new RuntimeException("File not found");
                    });

            fileService.deleteFile(file.getId());
            log.info("File deleted successfully: {} for user id: {}", filename, userId);
            return ResponseEntity.ok().build();

        } catch (RuntimeException e) {
            log.error("Error deleting file {}: {}", filename, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<FileDTO> downloadFile(@PathVariable String filename,
                                                @RequestHeader("X-User-Id") Long userId) {
        log.info("Received download request for file: {} from user id: {}", filename, userId);
        try {
            File file = fileService.findByFilenameAndUserId(filename, userId)
                    .orElseThrow(() -> {
                        log.warn("File not found for download: {} for user id: {}", filename, userId);
                        return new RuntimeException("File not found");
                    });

            FileDTO fileDTO = FileDTO.fromEntity(file);
            log.info("File downloaded successfully: {} for user id: {}", filename, userId);
            return ResponseEntity.ok(fileDTO);

        } catch (RuntimeException e) {
            log.error("Error downloading file {}: {}", filename, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
}
