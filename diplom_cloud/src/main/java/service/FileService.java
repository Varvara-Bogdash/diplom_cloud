package service;
import entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import repository.FileRepository;
import repository.UserRepository;


import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public File saveFile(String filename, byte[] content, Long userId, Long size) {
        log.info("Saving file '{}' for user id: {}", filename, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new RuntimeException("User not found");
                });

        File file = new File();
        file.setFilename(filename);
        file.setContent(content);
        file.setSize(size);
        file.setUser(user);
        file.setUploadDate(LocalDateTime.now());

        File savedFile = fileRepository.save(file);
        log.info("File saved successfully with id: {}", savedFile.getId());
        return savedFile;
    }

    public Optional<File> getFile(Long id) {
        log.debug("Retrieving file by id: {}", id);
        return fileRepository.findById(id);
    }

    public List<File> getAllFiles(Long userId) {
        log.debug("Retrieving all files for user id: {}", userId);
        return fileRepository.findByUserId(userId);
    }

    public void deleteFile(Long id) {
        log.info("Deleting file with id: {}", id);
        if (!fileRepository.existsById(id)) {
            log.warn("Attempt to delete non-existent file with id: {}", id);
            throw new RuntimeException("File not found");
        }
        fileRepository.deleteById(id);
        log.info("File deleted successfully with id: {}", id);
    }

    public Optional<File> findByFilenameAndUserId(String filename, Long userId) {
        log.debug("Searching for file '{}' for user id: {}", filename, userId);
        return fileRepository.findByFilenameAndUserId(filename, userId);
    }
}