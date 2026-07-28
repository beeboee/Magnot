package com.beeboee.magnot.material;

import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdaptiveMaterialsTest {
    @Test
    void magnotDustDoesNotCountAsExternalDust() {
        assertFalse(AdaptiveMaterials.externalIronDustAvailableIds(List.of(AdaptiveMaterials.MAGNOT_IRON_DUST)));
    }

    @Test
    void anyDifferentTaggedItemCountsAsExternalDust() {
        assertTrue(AdaptiveMaterials.externalIronDustAvailableIds(List.of(
                AdaptiveMaterials.MAGNOT_IRON_DUST,
                ResourceLocation.fromNamespaceAndPath("example", "iron_dust")
        )));
    }

    @Test
    void emptyTagDoesNotCountAsExternalDust() {
        assertFalse(AdaptiveMaterials.externalIronDustAvailableIds(List.of()));
    }
}
