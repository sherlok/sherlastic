package io.sherlok.sherlastic.module;

import io.sherlok.sherlastic.indices.analysis.SherlokIndicesAnalysis;

import org.elasticsearch.common.inject.AbstractModule;

public class SherlokAnalysisModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SherlokIndicesAnalysis.class).asEagerSingleton();
    }
}