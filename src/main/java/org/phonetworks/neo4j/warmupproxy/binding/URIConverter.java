package org.phonetworks.neo4j.warmupproxy.binding;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@ConfigurationPropertiesBinding
public class URIConverter implements Converter<String, URI> {

    @Override
    public URI convert(String value) {
        if (value == null){
            return null;
        }

        return URI.create(value);
    }
}