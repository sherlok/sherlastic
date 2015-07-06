package io.sherlok.sherlastic.indices.analysis;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.PreBuiltTokenFilterFactoryFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;

public class SherlokTokenFilterFactoryFactory extends
        PreBuiltTokenFilterFactoryFactory {
    private final TokenFilterFactory tokenFilterFactory;

    public SherlokTokenFilterFactoryFactory(
            final TokenFilterFactory tokenFilterFactory) {
        super(tokenFilterFactory);
        this.tokenFilterFactory = tokenFilterFactory;
    }

    @Override
    public TokenFilterFactory create(final String name, final Settings settings) {
        return tokenFilterFactory;
    }
}
