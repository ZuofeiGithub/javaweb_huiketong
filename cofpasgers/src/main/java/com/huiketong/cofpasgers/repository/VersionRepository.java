package com.huiketong.cofpasgers.repository;

import com.huiketong.cofpasgers.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<Version,Integer> {
        Version findVersionByPlatform(String platform);
}
