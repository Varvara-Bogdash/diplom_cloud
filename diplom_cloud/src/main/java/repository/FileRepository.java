package repository;
import entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<Files, Long> {
    Optional<Files> findFileByUserIdAndFileName(Long userId, String filename);

    @Query(value = "SELECT * FROM files f WHERE f.user_id = :userId ORDER BY f.file_name LIMIT :limit", nativeQuery = true)
    List<Files> findFilesByUserIdWithLimit(Long userId, int limit);
}