package net.myriantics.myrror.mixin;

import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;
import java.util.Set;

@Mixin(ModelTemplate.class)
public interface ModelTemplateAccessor {

    @Accessor("requiredSlots")
    public Set<TextureSlot> klaxon$getRequiredTextureSlots();

    @Accessor("model")
    public Optional<ResourceLocation> klaxon$getParent();
}
