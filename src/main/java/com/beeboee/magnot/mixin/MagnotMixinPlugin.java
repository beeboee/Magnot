package com.beeboee.magnot.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Allows individual optional-mod adapters to be disabled before their mixins are applied.
 * This is primarily a diagnostic switch; it does not unload the target mod.
 */
public final class MagnotMixinPlugin implements IMixinConfigPlugin {
    public static final String DISABLED_INTEGRATIONS_PROPERTY = "magnot.disableIntegrations";
    public static final String DISABLED_INTEGRATIONS_ENVIRONMENT = "MAGNOT_DISABLE_INTEGRATIONS";

    private String mixinPackage;
    private Set<String> disabledIntegrations = Set.of();

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage;
        this.disabledIntegrations = readDisabledIntegrations();
        if (!disabledIntegrations.isEmpty()) {
            System.out.println("[Magnot] Disabled integration mixins: " + disabledIntegrations);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (disabledIntegrations.isEmpty()) {
            return true;
        }
        if (disabledIntegrations.contains("all")) {
            return false;
        }

        String prefix = mixinPackage + ".";
        if (!mixinClassName.startsWith(prefix)) {
            return true;
        }

        String relativeName = mixinClassName.substring(prefix.length());
        int separator = relativeName.indexOf('.');
        String integration = separator < 0 ? relativeName : relativeName.substring(0, separator);
        return !disabledIntegrations.contains(normalize(integration));
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private static Set<String> readDisabledIntegrations() {
        LinkedHashSet<String> disabled = new LinkedHashSet<>();
        addTokens(disabled, System.getenv(DISABLED_INTEGRATIONS_ENVIRONMENT));
        addTokens(disabled, System.getProperty(DISABLED_INTEGRATIONS_PROPERTY));
        return Set.copyOf(disabled);
    }

    private static void addTokens(Set<String> disabled, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        for (String token : value.split("[,;\\s]+")) {
            String normalized = normalize(token);
            if (!normalized.isEmpty()) {
                disabled.add(normalized);
            }
        }
    }

    private static String normalize(String value) {
        String lower = value.toLowerCase(Locale.ROOT);
        StringBuilder normalized = new StringBuilder(lower.length());
        for (int index = 0; index < lower.length(); index++) {
            char character = lower.charAt(index);
            if (Character.isLetterOrDigit(character)) {
                normalized.append(character);
            }
        }
        return normalized.toString();
    }
}
