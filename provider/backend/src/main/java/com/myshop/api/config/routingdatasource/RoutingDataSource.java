package com.myshop.api.config.routingdatasource;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/*
    AbstractRoutingDataSource: 여러개의 데이터소스를 하나로 묶고 자동으로 분기처리를 해주는 Spring 기본 클래스
    targetDataSources라는 Map에 <조회 키, DataSource>가 저장된다.
    getConnection()에서 determineCurrentLookupKey()를 호출해 조회 키를 리턴받은 다음
    해당 키로 targetDataSource에 저장된 DataSource 중 라우팅 할 DataSource가 결정된다.
    조회 키 결정 로직은 보통 스레드 바운드 트랜잭션 컨텍스트로 구현된다.
*/
@Profile("prod")
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                ? DataSourceType.SLAVE
                : DataSourceType.MASTER;
    }
}
