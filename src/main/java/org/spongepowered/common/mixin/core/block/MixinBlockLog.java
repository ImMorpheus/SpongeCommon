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
package org.spongepowered.common.mixin.core.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.ImmutableAxisData;
import org.spongepowered.api.data.manipulator.immutable.ImmutableTreeData;
import org.spongepowered.api.data.value.Value;
import org.spongepowered.api.util.Axis;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeAxisData;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeTreeData;
import org.spongepowered.common.data.util.DirectionResolver;
import org.spongepowered.common.registry.type.block.TreeTypeRegistryModule;

import java.util.List;
import java.util.Optional;

@NonnullByDefault
@Mixin(BlockLog.class)
public abstract class MixinBlockLog extends MixinBlock {

    protected ImmutableTreeData getTreeData(IBlockState blockState) {
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeTreeData.class, TreeTypeRegistryModule.getTreeType(blockState).get());
    }

    protected ImmutableAxisData getLogAxisData(IBlockState blockState) {
        final Axis axis = DirectionResolver.getFor(blockState.get(BlockLog.AXIS));
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeAxisData.class, axis);
    }

    @Override
    public boolean supports(Class<? extends ImmutableDataManipulator<?, ?>> immutable) {
        return ImmutableTreeData.class.isAssignableFrom(immutable) || ImmutableAxisData.class.isAssignableFrom(immutable);
    }

    @Override
    public Optional<BlockState> getStateWithData(IBlockState blockState, ImmutableDataManipulator<?, ?> manipulator) {
        if (manipulator instanceof ImmutableAxisData) {
            final Axis axis = ((ImmutableAxisData) manipulator).type().get();
            return Optional.of((BlockState) blockState.with(BlockLog.AXIS, DirectionResolver.getFor(axis)));
        }
        return super.getStateWithData(blockState, manipulator);
    }

    @Override
    public <E> Optional<BlockState> getStateWithValue(IBlockState blockState, Key<? extends Value<E>> key, E value) {
        if (key.equals(Keys.AXIS)) {
            return Optional.of((BlockState) blockState.with(BlockLog.AXIS, DirectionResolver.getFor((Axis) value)));
        }
        return super.getStateWithValue(blockState, key, value);
    }

    @Override
    public List<ImmutableDataManipulator<?, ?>> getManipulators(IBlockState blockState) {
        return ImmutableList.of(getTreeData(blockState), getLogAxisData(blockState));
    }
}
