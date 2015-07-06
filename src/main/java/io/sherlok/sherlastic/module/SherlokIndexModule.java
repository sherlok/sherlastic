package io.sherlok.sherlastic.module;

import io.sherlok.sherlastic.index.mapper.RegisterSherlokType;

import org.elasticsearch.common.inject.AbstractModule;

public class SherlokIndexModule extends AbstractModule {
    
    @Override
    protected void configure() {
        bind(RegisterSherlokType.class).asEagerSingleton();
    }
}
