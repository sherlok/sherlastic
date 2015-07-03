package org.codelibs.elasticsearch.minhash.module;

import org.codelibs.elasticsearch.minhash.index.mapper.RegisterSherlokType;
import org.elasticsearch.common.inject.AbstractModule;

public class SherlokIndexModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RegisterSherlokType.class).asEagerSingleton();
    }
}
