package org.spongepowered.common.statistic;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatCrafting;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.scoreboard.critieria.Criterion;
import org.spongepowered.api.statistic.ItemStatistic;
import org.spongepowered.api.statistic.StatisticType;
import org.spongepowered.api.statistic.StatisticTypes;
import org.spongepowered.api.text.translation.Translation;
import org.spongepowered.common.registry.type.ItemTypeRegistryModule;
import org.spongepowered.common.text.translation.SpongeTranslation;

import javax.annotation.Nullable;
import java.util.Optional;

public class SpongeItemStatistic extends StatCrafting implements ItemStatistic, SpongeStatistic {

    private String spongeId;
    private final Translation translation;

    public SpongeItemStatistic(String statId, String itemName, ITextComponent statName, Item item) {
        super(statId, itemName, statName, item);
        String text = new ItemStack(item).getTextComponent().getUnformattedText();
        this.translation = new SpongeTranslation(statId.substring(0, statId.length() -1), text.substring(1, text.length() - 1));
    }

    @Override
    public Translation getTranslation() {
        return translation;
    }

    @Override
    public ItemType getItemType() {
        return ItemTypeRegistryModule.getInstance().getById(
                this.statId.substring(this.statId.lastIndexOf('.') + 1)).get();
    }

    @Override
    public Optional<Criterion> getCriterion() {
        return Optional.ofNullable((Criterion) getCriteria());
    }

    @Override
    public String getName() {
        return getStatName().getUnformattedText();
    }

    @Nullable
    @Override
    public String getSpongeId() {
        return this.spongeId;
    }

    @Override
    public void setSpongeId(String id) {
        this.spongeId = id;
    }

    @Override
    public String getMinecraftId() {
        return this.statId;
    }
    @Override
    public StatisticType getType() {
        return StatisticTypes.ITEMS;
    }
}
