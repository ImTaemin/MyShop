package com.myshop.api.repository;

import com.myshop.api.domain.entity.Favorite;
import com.myshop.api.domain.entity.id.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId>, FavoriteRepositoryCustom {
}
