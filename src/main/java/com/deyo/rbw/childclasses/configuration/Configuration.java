/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses.configuration;

import java.util.Map;

import com.deyo.rbw.childclasses.configuration.ConfigurationOptions;
import com.deyo.rbw.childclasses.configuration.ConfigurationSection;

public interface Configuration
extends ConfigurationSection {
    @Override
    public void addDefault(String var1, Object var2);

    public void addDefaults(Map<String, Object> var1);

    public void addDefaults(Configuration var1);

    public void setDefaults(Configuration var1);

    public Configuration getDefaults();

    public ConfigurationOptions options();
}

