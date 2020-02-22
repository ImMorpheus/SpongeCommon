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
package org.spongepowered.common.data.processor.data.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.manipulator.immutable.ImmutableColoredData;
import org.spongepowered.api.data.manipulator.mutable.ColoredData;
import org.spongepowered.api.data.value.Value.Immutable;
import org.spongepowered.api.data.value.Value.Mutable;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.util.Color;
import org.spongepowered.common.data.manipulator.mutable.SpongeColoredData;
import org.spongepowered.common.data.processor.common.AbstractItemSingleDataProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;
import org.spongepowered.common.data.value.mutable.SpongeValue;
import org.spongepowered.common.util.ColorUtil;
import org.spongepowered.common.util.Constants;

import java.util.Optional;

public class ColoredDataProcessor extends AbstractItemSingleDataProcessor<Color, Mutable<Color>, ColoredData, ImmutableColoredData> {

    public ColoredDataProcessor() {
        super(ColorUtil::hasColor, Keys.COLOR);
    }

    @Override
    public DataTransactionResult removeFrom(final ValueContainer<?> container) {
        if (this.supports(container)) {
            final ItemStack stack = (ItemStack) container;
            final Optional<Color> old = this.getVal(stack);
            if (!old.isPresent()) {
                return DataTransactionResult.successNoData();
            }
            if (!ColorUtil.hasColorInNbt(stack)) {
                return DataTransactionResult.failNoData();
            }
            final CompoundNBT display = stack.getChildTag(Constants.Item.ITEM_DISPLAY);
            if (display != null) {
                display.remove(Constants.Item.ITEM_COLOR);
            }
            return DataTransactionResult.successRemove(this.constructImmutableValue(old.get()));
        }
        return DataTransactionResult.failNoData();
    }

    @Override
    protected boolean set(final ItemStack container, final Color value) {
        if (!this.supports(container)) {
            return false;
        }
        ColorUtil.setItemStackColor(container, value);
        return true;
    }

    @Override
    protected Optional<Color> getVal(final ItemStack container) {
        return ColorUtil.getItemStackColor(container);
    }

    @Override
    protected Mutable<Color> constructValue(final Color actualValue) {
        return new SpongeValue<>(Keys.COLOR, Color.BLACK, actualValue);
    }

    @Override
    protected Immutable<Color> constructImmutableValue(final Color value) {
        return ImmutableSpongeValue.cachedOf(Keys.COLOR, Color.BLACK, value);
    }

    @Override
    protected ColoredData createManipulator() {
        return new SpongeColoredData();
    }

}
