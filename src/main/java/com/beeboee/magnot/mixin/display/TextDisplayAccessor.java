package com.beeboee.magnot.mixin.display;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.TextDisplay.class)
public interface TextDisplayAccessor {
    @Invoker("setText")
    void magnot$setText(Component text);

    @Invoker("setLineWidth")
    void magnot$setLineWidth(int lineWidth);
}
