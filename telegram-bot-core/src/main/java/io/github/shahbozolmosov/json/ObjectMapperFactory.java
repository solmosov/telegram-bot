package io.github.shahbozolmosov.json;

import tools.jackson.core.StreamReadConstraints;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.json.JsonMapper;

public final class ObjectMapperFactory {


    private static final int MAX_NESTING_DEPTH = 100;
    private static final int MAX_STRING_LENGTH = 1_000_000;


    private ObjectMapperFactory() {

    }

    public static JsonMapper create() {
        StreamReadConstraints constraints = StreamReadConstraints.builder()
                .maxNestingDepth(MAX_NESTING_DEPTH)
                .maxStringLength(MAX_STRING_LENGTH)
                .build();

        JsonFactory factory = JsonFactory.builder()
                .streamReadConstraints(constraints)
                .build();


        return JsonMapper.builder(factory)
                .build();
    }
}
