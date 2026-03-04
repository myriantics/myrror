package net.myriantics.myrror.datagen.template.advancement;

import net.minecraft.advancements.*;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class MyrrorAdvancementSubProvider {

    protected final MyrrorAdvancementProvider provider;
    protected final Consumer<AdvancementHolder> consumer;
    protected final String path;

    public MyrrorAdvancementSubProvider(MyrrorAdvancementProvider provider, Consumer<AdvancementHolder> consumer, String path) {
        this.path = path;
        this.consumer = consumer;
        this.provider = provider;
    }

    protected abstract void generate();

    protected AdvancementHolder addTask(AdvancementHolder parent, String name, ItemLike display, Criterion<?> criterion) {
        return addTask(parent, name, display, false, criterion, null);
    }

    protected AdvancementHolder addTask(AdvancementHolder parent, String name, ItemStack display, Criterion<?> criterion) {
        return addTask(parent, name, display, false, criterion, null);
    }

    protected AdvancementHolder addGoal(AdvancementHolder parent, String name, ItemLike display, Criterion<?> criterion) {
        return addGoal(parent, name, display, false, criterion, null);
    }

    protected AdvancementHolder addGoal(AdvancementHolder parent, String name, ItemStack display, Criterion<?> criterion) {
        return addGoal(parent, name, display, false, criterion, null);
    }

    protected AdvancementHolder addChallenge(AdvancementHolder parent, String name, ItemStack display, Criterion<?> criterion) {
        return addChallenge(parent, name, display, false, criterion, null);
    }

    protected AdvancementHolder addChallenge(AdvancementHolder parent, String name, ItemLike display, Criterion<?> criterion) {
        return addChallenge(parent, name, display, false, criterion, null);
    }

    protected AdvancementHolder addHiddenTask(AdvancementHolder parent, String name, ItemLike display, Criterion<?> criterion) {
        return addTask(parent, name, display, true, criterion, null);
    }

    protected AdvancementHolder addHiddenTask(AdvancementHolder parent, String name, ItemStack display, Criterion<?> criterion) {
        return addTask(parent, name, display, true, criterion, null);
    }

    protected AdvancementHolder addHiddenGoal(AdvancementHolder parent, String name, ItemLike display, Criterion<?> criterion) {
        return addGoal(parent, name, display, true, criterion, null);
    }

    protected AdvancementHolder addHiddenGoal(AdvancementHolder parent, String name, ItemStack display, Criterion<?> criterion) {
        return addGoal(parent, name, display, true, criterion, null);
    }

    protected AdvancementHolder addHiddenChallenge(AdvancementHolder parent, String name, ItemStack display, Criterion<?> criterion) {
        return addChallenge(parent, name, display, true, criterion, null);
    }

    protected AdvancementHolder addHiddenChallenge(AdvancementHolder parent, String name, ItemLike display, Criterion<?> criterion) {
        return addChallenge(parent, name, display, true, criterion, null);
    }

    protected AdvancementHolder addTask(AdvancementHolder parent, String name, ItemLike display, boolean hidden, Criterion<?> criterion, @Nullable AdvancementRewards.Builder rewards) {
        return addAdvancement(parent, name, display, AdvancementType.TASK, hidden, criterion, rewards);
    }

    protected AdvancementHolder addTask(AdvancementHolder parent, String name, ItemStack display, boolean hidden, Criterion<?> criterion, @Nullable AdvancementRewards.Builder rewards) {
        return addAdvancement(parent, name, display, AdvancementType.TASK, hidden, criterion, rewards);
    }

    protected AdvancementHolder addGoal(AdvancementHolder parent, String name, ItemLike display, boolean hidden, Criterion<?> criterion, @Nullable AdvancementRewards.Builder rewards) {
        return addAdvancement(parent, name, display, AdvancementType.GOAL, hidden, criterion, rewards);
    }

    protected AdvancementHolder addGoal(AdvancementHolder parent, String name, ItemStack display, boolean hidden, Criterion<?> criterion, @Nullable AdvancementRewards.Builder rewards) {
        return addAdvancement(parent, name, display, AdvancementType.GOAL, hidden, criterion, rewards);
    }

    protected AdvancementHolder addChallenge(AdvancementHolder parent, String name, ItemLike display, boolean hidden, Criterion<?> criterion, @Nullable AdvancementRewards.Builder rewards) {
        return addAdvancement(parent, name, display, AdvancementType.CHALLENGE, hidden, criterion, rewards);
    }

    protected AdvancementHolder addChallenge(AdvancementHolder parent, String name, ItemStack display, boolean hidden, Criterion<?> criterion, @Nullable AdvancementRewards.Builder rewards) {
        return addAdvancement(parent, name, display, AdvancementType.CHALLENGE, hidden, criterion, rewards);
    }


    protected AdvancementHolder addRootAdvancement(ItemLike display, AdvancementType frame, Criterion<?> criterion) {
        return addAdvancement(null, "root", this.locate("textures/gui/advancements/backgrounds/" + path + ".png"), display, frame, false, false, false, criterion, null);
    }

    protected AdvancementHolder addRootAdvancement(ItemStack display, AdvancementType frame, Criterion<?> criterion) {
        return addAdvancement(null, "root", this.locate("textures/gui/advancements/backgrounds/" + path + ".png"), display, frame, false, false, false, criterion, null);
    }

    protected AdvancementHolder addAdvancement(@Nullable AdvancementHolder parent, String name, ItemLike display, AdvancementType frame, boolean hidden, Criterion<?> criterion, @Nullable AdvancementRewards.Builder rewards) {
        return addAdvancement(parent, name, null, display, frame, true, true, hidden, criterion, rewards);
    }

    protected AdvancementHolder addAdvancement(@Nullable AdvancementHolder parent, String name, ItemStack display, AdvancementType frame, boolean hidden, Criterion<?> criterion, @Nullable AdvancementRewards.Builder rewards) {
        return addAdvancement(parent, name, null, display, frame, true, true, hidden, criterion, rewards);
    }

    protected AdvancementHolder addAdvancement(@Nullable AdvancementHolder parent, String name, ResourceLocation backgroundId, ItemLike display, AdvancementType frame, boolean showToast, boolean showToChat, boolean hidden, Criterion<?> criterion, @Nullable AdvancementRewards.Builder rewards) {
        return this.addAdvancement(parent, name, backgroundId, new ItemStack(display), frame, showToast, showToChat, hidden, criterion, rewards);
    }

    protected AdvancementHolder addAdvancement(@Nullable AdvancementHolder parent, String name, ResourceLocation backgroundId, ItemStack display, AdvancementType frame, boolean showToast, boolean showToChat, boolean hidden, Criterion<?> criterion, @Nullable AdvancementRewards.Builder rewards) {
        Advancement.Builder builder = Advancement.Builder.advancement();
        if (parent != null) {
            builder.parent(parent);
        }

        builder.display(
                        display,
                        Component.translatable("advancements." + this.provider.getNamespace() + "." + path + "." + name + ".title"),
                        Component.translatable("advancements." + this.provider.getNamespace() + "." + path + "." + name + ".description"),
                        backgroundId,
                        frame,
                        showToast,
                        showToChat,
                        hidden
                )
                .addCriterion(name, criterion);

        if (rewards != null) {
            builder.rewards(rewards);
        }

        return builder.save(this.consumer, this.locate(name).toString());
    }

    protected ResourceLocation locate(String name) {
        return ResourceLocation.fromNamespaceAndPath(this.provider.getNamespace(), this.path + "/" + name);
    }
}
