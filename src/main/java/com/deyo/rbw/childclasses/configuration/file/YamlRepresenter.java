/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses.configuration.file;

import java.util.LinkedHashMap;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Representer;

import com.deyo.rbw.childclasses.configuration.ConfigurationSection;
import com.deyo.rbw.childclasses.configuration.serialization.ConfigurationSerializable;
import com.deyo.rbw.childclasses.configuration.serialization.ConfigurationSerialization;

public class YamlRepresenter
extends Representer {
    public YamlRepresenter() {
        this.multiRepresenters.put(ConfigurationSection.class, new RepresentConfigurationSection());
        this.multiRepresenters.put(ConfigurationSerializable.class, new RepresentConfigurationSerializable());
    }

    private class RepresentConfigurationSerializable
    extends Representer.RepresentMap {
        private RepresentConfigurationSerializable() {
        }

        @Override
        public Node representData(Object data) {
            ConfigurationSerializable serializable = (ConfigurationSerializable)data;
            LinkedHashMap<String, Object> values2 = new LinkedHashMap<String, Object>();
            values2.put("==", ConfigurationSerialization.getAlias(serializable.getClass()));
            values2.putAll(serializable.serialize());
            return super.representData(values2);
        }
    }

    private class RepresentConfigurationSection
    extends Representer.RepresentMap {
        private RepresentConfigurationSection() {
        }

        @Override
        public Node representData(Object data) {
            return super.representData(((ConfigurationSection)data).getValues(false));
        }
    }
}

