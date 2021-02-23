package com.cloudshare.cloudshareapi.repository;

import com.cloudshare.cloudshareapi.model.FileMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMetaRepository extends JpaRepository<FileMeta, Long> {
    List<FileMeta> findAllByOwnerIdAndParentId(Long ownerId, Long parentId);
    List<FileMeta> findAllByOwnerIdAndParentIdAndType(Long id, Long parentId, String type);
}
