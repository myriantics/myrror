package net.myriantics.myrror.datagen.template.dynamic.providers;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.myriantics.myrror.datagen.template.dynamic.MyrrorDynamicRegistryProvider;
import net.myriantics.myrror.datagen.template.dynamic.MyrrorDynamicRegistrySubProvider;

import java.util.function.UnaryOperator;

public abstract class DamageTypeProvider extends MyrrorDynamicRegistrySubProvider<DamageType> {
    public DamageTypeProvider(MyrrorDynamicRegistryProvider provider, HolderLookup.Provider lookup, FabricDynamicRegistryProvider.Entries entries) {
        super(provider, lookup, entries, Registries.DAMAGE_TYPE);
    }

    protected DamageType addNeverScaling(ResourceKey<DamageType> key, UnaryOperator<Builder> operator) {
        DamageType type = operator.apply(Builder.neverScale(key)).build();
        return this.add(key, type);
    }

    protected DamageType addLivingNonPlayerScaling(ResourceKey<DamageType> key, UnaryOperator<Builder> operator) {
        DamageType type = operator.apply(Builder.livingNonPlayerScale(key)).build();
        return this.add(key, type);
    }

    protected DamageType addAlwaysScaling(ResourceKey<DamageType> key, UnaryOperator<Builder> operator) {
        DamageType type = operator.apply(Builder.alwaysScale(key)).build();
        return this.add(key, type);
    }

    public static final class Builder {
        private final ResourceKey<DamageType> key;
        private final String msgId;
        private final DamageScaling scaling;
        private DamageEffects effects = DamageEffects.HURT;
        private float exhaustion = 0;
        private DeathMessageType deathMessageType = DeathMessageType.DEFAULT;

        private Builder(ResourceKey<DamageType> key, DamageScaling scaling) {
            this.key = key;
            this.msgId = key.location().getNamespace() + "." + key.location().getPath();
            this.scaling = scaling;
        }

        private static Builder neverScale(ResourceKey<DamageType> key) {
            return new Builder(key, DamageScaling.NEVER);
        }

        private static Builder alwaysScale(ResourceKey<DamageType> key) {
            return new Builder(key, DamageScaling.ALWAYS);
        }

        private static Builder livingNonPlayerScale(ResourceKey<DamageType> key) {
            return new Builder(key, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER);
        }

        public Builder exhaustion(float exhaustion) {
            this.exhaustion = exhaustion;
            return this;
        }

        public Builder effects(DamageEffects effects) {
            this.effects = effects;
            return this;
        }

        public Builder deathMessageType(DeathMessageType type) {
            this.deathMessageType = type;
            return this;
        }

        public DamageType build() {
            return new DamageType(this.msgId, this.scaling, this.exhaustion, this.effects, this.deathMessageType);
        }
    }
}
