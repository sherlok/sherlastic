package io.sherlok.sherlastic;

import static org.elasticsearch.common.collect.Lists.newArrayList;
import io.sherlok.sherlastic.index.analysis.SherlokTokenFilterFactory;
import io.sherlok.sherlastic.module.SherlokAnalysisModule;
import io.sherlok.sherlastic.module.SherlokIndexModule;

import java.util.Collection;

import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;

public class SherlokPlugin extends AbstractPlugin {
    @Override
    public String name() {
        return "SherlokPlugin";
    }

    @Override
    public String description() {
        return "Sherlastic provides an Elasticsearch plugin to enhance its index with semantic information from Sherlok.";
    }

    @Override
    public Collection<Class<? extends Module>> modules() {
        return ImmutableList
                .<Class<? extends Module>> of(SherlokAnalysisModule.class);
    }

    public void onModule(final AnalysisModule module) {
        module.addTokenFilter("sherlok", SherlokTokenFilterFactory.class);
    }

    @Override
    public Collection<Class<? extends Module>> indexModules() {
        final Collection<Class<? extends Module>> modules = newArrayList();
        modules.add(SherlokIndexModule.class);
        return modules;
    }
}
