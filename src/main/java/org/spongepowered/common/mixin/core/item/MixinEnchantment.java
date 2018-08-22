/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.mixin.core.item;

import com.google.common.base.MoreObjects;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.translation.Translation;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.interfaces.IMixinEnchantment;
import org.spongepowered.common.text.translation.SpongeTranslation;

import javax.annotation.Nullable;

@NonnullByDefault
@Mixin(net.minecraft.enchantment.Enchantment.class)
@Implements(@Interface(iface = EnchantmentType.class, prefix = "enchantment$"))
public abstract class MixinEnchantment implements IMixinEnchantment, EnchantmentType {

    @Shadow protected String name;
    @Shadow @Final private net.minecraft.enchantment.Enchantment.Rarity rarity;

    @Shadow public abstract int getMinLevel();
    @Shadow public abstract int getMaxLevel();
    @Shadow public abstract int getMinEnchantability(int level);
    @Shadow public abstract int getMaxEnchantability(int level);
    @Shadow public abstract boolean canApplyTogether(net.minecraft.enchantment.Enchantment ench);
    @Shadow public abstract String shadow$getName();
    @Shadow public abstract boolean isTreasureEnchantment();
    @Shadow public abstract boolean shadow$isCurse();

    private CatalogKey key;
    @Nullable private Translation translation;

    @Inject(method = "registerEnchantments", at = @At("RETURN"))
    private static void onRegister(CallbackInfo ci) {
        for (ResourceLocation resourceLocation: net.minecraft.enchantment.Enchantment.REGISTRY.getKeys()) {
            ((IMixinEnchantment) net.minecraft.enchantment.Enchantment.REGISTRY.getObject(resourceLocation)).setId(resourceLocation);
        }
    }

    @Override
    public void setId(ResourceLocation location) {
        this.key = (CatalogKey) (Object) location;
    }

    @Override
    public CatalogKey getKey() {
        return this.key;
    }

    @Override
    public int getWeight() {
        return this.rarity.getWeight();
    }

    @Override
    public int getMinimumLevel() {
        return getMinLevel();
    }

    @Override
    public int getMaximumLevel() {
        return getMaxLevel();
    }

    @Override
    public int getMinimumEnchantabilityForLevel(int level) {
        return getMinEnchantability(level);
    }

    @Override
    public int getMaximumEnchantabilityForLevel(int level) {
        return getMaxEnchantability(level);
    }

    @Override
    public boolean canBeAppliedByTable(ItemStack stack) {
        return canBeAppliedToStack(stack);
    }

    @Override
    public boolean isCompatibleWith(EnchantmentType ench) {
        return canApplyTogether((net.minecraft.enchantment.Enchantment) ench);
    }

    @Intrinsic
    public String enchantment$getName() {
        return shadow$getName();
    }

    @Override
    public Translation getTranslation() {
        if (this.translation == null) {
            this.translation = new SpongeTranslation(shadow$getName());
        }
        return this.translation;
    }

    @Override
    public boolean isTreasure() {
        return isTreasureEnchantment();
    }

    @Intrinsic
    public boolean enchantment$isCurse() {
        return shadow$isCurse();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("EnchantmentType")
                .add("key", getKey())
                .add("name", shadow$getName())
                .toString();
    }
}
