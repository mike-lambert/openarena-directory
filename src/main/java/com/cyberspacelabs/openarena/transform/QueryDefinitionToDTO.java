package com.cyberspacelabs.openarena.transform;

import com.cyberspacelabs.openarena.dto.Definition;
import com.cyberspacelabs.openarena.model.QueryDefinition;
import com.google.common.base.Function;

/**
 * Created by mike on 08.09.17.
 */
public class QueryDefinitionToDTO implements Function<QueryDefinition, Definition> {
    @Override
    public Definition apply(QueryDefinition input) {
        Definition result = new Definition();
        result.set_default(input.isDefaultDefintion());
        result.setId(input.getId());
        result.setLabel(input.getLabel());
        return result;
    }
}
