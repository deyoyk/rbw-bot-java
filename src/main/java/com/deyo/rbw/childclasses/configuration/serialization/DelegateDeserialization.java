/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses.configuration.serialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.deyo.rbw.childclasses.configuration.serialization.ConfigurationSerializable;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface DelegateDeserialization {
    public Class<? extends ConfigurationSerializable> value();
}

