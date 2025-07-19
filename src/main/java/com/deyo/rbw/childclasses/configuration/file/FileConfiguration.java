/*
 * Recoded by deyo 
 */
package com.deyo.rbw.childclasses.configuration.file;

import com.deyo.rbw.childclasses.configuration.Configuration;
import com.deyo.rbw.childclasses.configuration.InvalidConfigurationException;
import com.deyo.rbw.childclasses.configuration.MemoryConfiguration;
import com.deyo.rbw.childclasses.configuration.file.FileConfigurationOptions;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.Validate;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public abstract class FileConfiguration
extends MemoryConfiguration {
    static final byte[] testBytes = Base64Coder.decode("ICEiIyQlJicoKSorLC0uLzAxMjM0NTY3ODk6Ozw9Pj9AQUJDREVGR0hJSktMTU5PUFFSU1RVVldYWVpbXF1eX2BhYmNkZWZnaGlqa2xtbm9wcXJzdHV2d3h5ent8fX4NCg==");
    static final Charset defaultCharset = Charset.defaultCharset();
    static final String resultString = new String(testBytes, defaultCharset);
    static final boolean trueUTF = defaultCharset.name().contains("UTF");
    @Deprecated
    public static final boolean UTF8_OVERRIDE = !" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\r\n".equals(resultString) || defaultCharset.equals(StandardCharsets.US_ASCII);
    @Deprecated
    public static final boolean UTF_BIG = trueUTF && UTF8_OVERRIDE;
    @Deprecated
    public static final boolean SYSTEM_UTF = trueUTF || UTF8_OVERRIDE;

    public FileConfiguration() {
    }

    public FileConfiguration(Configuration defaults) {
        super(defaults);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null", new Object[0]);
        Files.createParentDirs(file);
        String data = this.saveToString();
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter((OutputStream)new FileOutputStream(file), UTF8_OVERRIDE && !UTF_BIG ? Charsets.UTF_8 : Charset.defaultCharset());
            writer.write(data);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public void save(String file) throws IOException {
        Validate.notNull(file, "File cannot be null", new Object[0]);
        this.save(new File(file));
    }

    public abstract String saveToString();

    public void load(File file) throws IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null", new Object[0]);
        FileInputStream stream = new FileInputStream(file);
        this.load(new InputStreamReader((InputStream)stream, UTF8_OVERRIDE && !UTF_BIG ? Charsets.UTF_8 : Charset.defaultCharset()));
    }

    @Deprecated
    public void load(InputStream stream) throws IOException, InvalidConfigurationException {
        Validate.notNull(stream, "Stream cannot be null", new Object[0]);
        this.load(new InputStreamReader(stream, UTF8_OVERRIDE ? Charsets.UTF_8 : Charset.defaultCharset()));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void load(Reader reader) throws IOException, InvalidConfigurationException {
        StringBuilder builder = new StringBuilder();
        BufferedReader input = null;
        try {
            input = (BufferedReader)(reader instanceof BufferedReader ? reader : new BufferedReader(reader));
            String line;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            if (input != null) {
                input.close();
            }
        }
        this.loadFromString(builder.toString());
    }

    public void load(String file) throws IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null", new Object[0]);
        this.load(new File(file));
    }

    public abstract void loadFromString(String var1) throws InvalidConfigurationException;

    protected abstract String buildHeader();

    @Override
    public FileConfigurationOptions options() {
        if (this.options == null) {
            this.options = new FileConfigurationOptions(this);
        }
        return (FileConfigurationOptions)this.options;
    }
}

