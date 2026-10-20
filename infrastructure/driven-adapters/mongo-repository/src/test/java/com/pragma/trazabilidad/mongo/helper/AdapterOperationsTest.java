package com.pragma.trazabilidad.mongo.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AdapterOperationsTest {

    @Mock
    private DummyRepository repository;

    @Mock
    private ObjectMapper mapper;

    private DummyAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Instanciamos el adaptador con nuestras clases dummy
        adapter = new DummyAdapter(repository, mapper, data -> new DummyEntity(data.getId()));
    }

    @Test
    void save_Success() {
        DummyEntity entity = new DummyEntity("1");
        DummyData data = new DummyData("1");

        when(mapper.map(entity, DummyData.class)).thenReturn(data);
        when(repository.save(data)).thenReturn(Mono.just(data));

        StepVerifier.create(adapter.save(entity))
                .expectNextMatches(e -> e.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void saveAll_Success() {
        DummyEntity entity = new DummyEntity("1");
        DummyData data = new DummyData("1");

        when(mapper.map(entity, DummyData.class)).thenReturn(data);
        // Usamos cast seguro para simular el Flux
        when(repository.saveAll((org.reactivestreams.Publisher<DummyData>) any())).thenReturn(Flux.just(data));

        StepVerifier.create(adapter.saveAll(Flux.just(entity)))
                .expectNextMatches(e -> e.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void findById_Success() {
        DummyData data = new DummyData("1");
        when(repository.findById("1")).thenReturn(Mono.just(data));

        StepVerifier.create(adapter.findById("1"))
                .expectNextMatches(e -> e.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void findByExample_Success() {
        DummyEntity entity = new DummyEntity("1");
        DummyData data = new DummyData("1");

        when(mapper.map(entity, DummyData.class)).thenReturn(data);
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(data));

        StepVerifier.create(adapter.findByExample(entity))
                .expectNextMatches(e -> e.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    void deleteById_Success() {
        when(repository.deleteById("1")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteById("1"))
                .verifyComplete();
    }

    @Test
    void findAll_Success() {
        DummyData data = new DummyData("1");
        when(repository.findAll()).thenReturn(Flux.just(data));

        StepVerifier.create(adapter.findAll())
                .expectNextMatches(e -> e.getId().equals("1"))
                .verifyComplete();
    }

    // --- CLASES DUMMY INTERNAS PARA SATISFACER LOS GENÉRICOS ---

    static class DummyEntity {
        private final String id;
        public DummyEntity(String id) { this.id = id; }
        public String getId() { return id; }
    }

    static class DummyData {
        private final String id;
        public DummyData(String id) { this.id = id; }
        public String getId() { return id; }
    }

    interface DummyRepository extends ReactiveCrudRepository<DummyData, String>, ReactiveQueryByExampleExecutor<DummyData> {
    }

    static class DummyAdapter extends AdapterOperations<DummyEntity, DummyData, String, DummyRepository> {
        public DummyAdapter(DummyRepository repository, ObjectMapper mapper, Function<DummyData, DummyEntity> toEntityFn) {
            super(repository, mapper, toEntityFn);
        }
    }
}