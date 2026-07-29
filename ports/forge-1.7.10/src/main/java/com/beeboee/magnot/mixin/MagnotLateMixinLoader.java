package com.beeboee.magnot.mixin;

import io.github.tox1cozz.mixinbooterlegacy.ILateMixinLoader;
import io.github.tox1cozz.mixinbooterlegacy.LateMixin;

import java.util.Collections;
import java.util.List;

@LateMixin
public final class MagnotLateMixinLoader implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("magnot-legacy.mixins.json");
    }
}
