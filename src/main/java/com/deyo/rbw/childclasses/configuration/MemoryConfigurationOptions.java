/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses.configuration;

import com.deyo.rbw.childclasses.configuration.ConfigurationOptions;
import com.deyo.rbw.childclasses.configuration.MemoryConfiguration;

public class MemoryConfigurationOptions
extends ConfigurationOptions {
    protected MemoryConfigurationOptions(MemoryConfiguration configuration) {
        super(configuration);
    }

    @Override
    public MemoryConfiguration configuration() {
        return (MemoryConfiguration)super.configuration();
    }

    @Override
    public MemoryConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @Override
    public MemoryConfigurationOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }
}

