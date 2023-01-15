package com.myshop.api.repository;

import com.myshop.api.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ItemRepositoryCustom 에서 정의한 메서드를 사용하기 위해 상속받음
@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

}
