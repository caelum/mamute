package org.mamute.builder;

import net.vidageek.mirror.dsl.Mirror;

public class ModelBuilder {
    protected void setId(Object o, Long id) {
        new Mirror().on(o).set().field("id").withValue(id);
    }
}
