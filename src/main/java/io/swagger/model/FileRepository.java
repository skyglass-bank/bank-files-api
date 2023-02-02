package io.swagger.model;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {
    Optional<File> findById(String fileId);
}
