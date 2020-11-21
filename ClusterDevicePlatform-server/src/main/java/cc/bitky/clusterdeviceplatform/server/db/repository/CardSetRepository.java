package cc.bitky.clusterdeviceplatform.server.db.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import cc.bitky.clusterdeviceplatform.server.db.dto.CardSet;

public interface CardSetRepository extends ReactiveCrudRepository<CardSet, String> {

   // Mono<CardSet> findByCardList();

}
