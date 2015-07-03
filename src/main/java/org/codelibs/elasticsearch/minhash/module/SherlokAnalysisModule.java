package org.codelibs.elasticsearch.minhash.module;

import org.codelibs.elasticsearch.minhash.indices.analysis.SherlokIndicesAnalysis;
import org.elasticsearch.common.inject.AbstractModule;

public class SherlokAnalysisModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SherlokIndicesAnalysis.class).asEagerSingleton();
    }
}