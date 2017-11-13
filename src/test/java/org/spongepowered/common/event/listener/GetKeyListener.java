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
package org.spongepowered.common.event.listener;

import org.junit.Assert;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.data.ChangeDataHolderEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.data.GetKey;

public class GetKeyListener {

    public boolean getKeyListenerCalled;
    public boolean dataHolderListenerCalled;

    @Listener
    public void getKeyListener(ChangeDataHolderEvent.ValueChange event, @GetKey("FOOD_LEVEL") Integer foodLevel, @GetKey("FOOD_LEVEL") MutableBoundedValue<Integer> foodValue, @GetKey("FOOD_LEVEL") ImmutableValue<Integer> foodImmut) {
        this.getKeyListenerCalled = true;
        Assert.assertEquals(foodLevel, foodValue.get());
        Assert.assertEquals(foodLevel, foodImmut.get());
        Assert.assertEquals((long) foodLevel, 15);
    }

    @Listener
    public void dataHolderListener(ChangeDataHolderEvent.ValueChange event, @First(tag = "foo") Player player, @GetKey(value = "FOOD_LEVEL", tag = "foo") Integer taggedFood) {
        this.dataHolderListenerCalled = true;
        Assert.assertEquals(taggedFood, player.get(Keys.FOOD_LEVEL).get());
        Assert.assertEquals((long) taggedFood, 15);
    }

}
