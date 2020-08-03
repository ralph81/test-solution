package com.db.dataplatform.techtest.server.persistence.repository;

import com.db.dataplatform.techtest.server.persistence.BlockTypeEnum;
import com.db.dataplatform.techtest.server.persistence.model.DataHeaderEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataHeaderRepository extends JpaRepository<DataHeaderEntity, Long> {
	public List<DataHeaderEntity> findByBlocktype(BlockTypeEnum blocktype);

	public DataHeaderEntity findByName(String name);
	

}
