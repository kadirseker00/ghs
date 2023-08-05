package usi.si.seart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import usi.si.seart.model.join.GitRepoTopic;

import javax.validation.constraints.NotNull;

public interface GitRepoTopicRepository extends JpaRepository<GitRepoTopic, GitRepoTopic.Key> {

    @Modifying
    @Query("delete from GitRepoTopic where repo.id = :id")
    void deleteByRepoId(@NotNull @Param("id") Long repoId);
}
