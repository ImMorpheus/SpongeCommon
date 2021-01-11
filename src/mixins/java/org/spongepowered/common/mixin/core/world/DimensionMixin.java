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
package org.spongepowered.common.mixin.core.world;

import com.mojang.serialization.Codec;
import net.kyori.adventure.text.Component;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Dimension;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.world.SerializationBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.common.bridge.ResourceKeyBridge;
import org.spongepowered.common.bridge.world.DimensionBridge;
import org.spongepowered.common.server.BootstrapProperties;
import org.spongepowered.common.world.server.SpongeWorldTemplate;

import java.util.UUID;
import java.util.function.Function;

@Mixin(Dimension.class)
public abstract class DimensionMixin implements DimensionBridge, ResourceKeyBridge {

    private ResourceKey impl$key;
    private ResourceLocation impl$gameMode;
    @Nullable private ResourceLocation impl$difficulty;
    private SerializationBehavior impl$serializationBehavior = SerializationBehavior.AUTOMATIC;
    @Nullable private Component impl$displayName = null;
    private Integer impl$viewDistance = BootstrapProperties.viewDistance;

    private boolean impl$enabled = true, impl$loadOnStartup = true, impl$performsSpawnLogic = false,impl$hardcore = BootstrapProperties.hardcore,
            impl$commands = true, impl$pvp = BootstrapProperties.pvp;

    @Override
    public ResourceKey bridge$getKey() {
        return this.impl$key;
    }

    @Override
    public void bridge$setKey(final ResourceKey key) {
        this.impl$key = key;
    }

    @Override
    public @Nullable Component bridge$displayName() {
        return this.impl$displayName;
    }

    @Override
    public ResourceLocation bridge$gameMode() {
        return this.impl$gameMode;
    }

    @Nullable
    @Override
    public ResourceLocation bridge$difficulty() {
        return this.impl$difficulty;
    }

    @Override
    public SerializationBehavior bridge$serializationBehavior() {
        return this.impl$serializationBehavior;
    }

    @Override
    public @Nullable Integer bridge$viewDistance() {
        return this.impl$viewDistance;
    }

    @Override
    public boolean bridge$enabled() {
        return this.impl$enabled;
    }

    @Override
    public boolean bridge$loadOnStartup() {
        return this.impl$loadOnStartup;
    }

    @Override
    public boolean bridge$performsSpawnLogic() {
        return this.impl$performsSpawnLogic;
    }

    @Override
    public boolean bridge$hardcore() {
        return this.impl$hardcore;
    }

    @Override
    public boolean bridge$commands() {
        return this.impl$commands;
    }

    @Override
    public boolean bridge$pvp() {
        return this.impl$pvp;
    }

    @Override
    public void bridge$populateFromData(final SpongeWorldTemplate.SpongeDataSection spongeData) {
        this.impl$gameMode = spongeData.gameMode;
        this.impl$difficulty = spongeData.difficulty;
        this.impl$serializationBehavior = spongeData.serializationBehavior == null ? SerializationBehavior.AUTOMATIC : spongeData.serializationBehavior;
        this.impl$displayName = spongeData.displayName;
        this.impl$viewDistance = spongeData.viewDistance == null ? BootstrapProperties.viewDistance : spongeData.viewDistance;
        this.impl$enabled = spongeData.enabled == null || spongeData.enabled;
        this.impl$loadOnStartup = spongeData.loadOnStartup == null || spongeData.loadOnStartup;
        this.impl$performsSpawnLogic = spongeData.performsSpawnLogic != null && spongeData.performsSpawnLogic;
        this.impl$hardcore = spongeData.hardcore == null ? BootstrapProperties.hardcore : spongeData.hardcore;
        this.impl$commands = spongeData.commands == null || spongeData.commands;
        this.impl$pvp = spongeData.pvp == null || spongeData.pvp;
    }

    @Override
    public void bridge$populateFromTemplate(final SpongeWorldTemplate s) {
        this.impl$key = s.getKey();
        this.impl$gameMode = (ResourceLocation) s.gameMode().orElse(null);
        this.impl$difficulty = (ResourceLocation) s.difficulty().orElse(null);
        this.impl$serializationBehavior = s.serializationBehavior();
        this.impl$displayName = s.displayName().orElse(null);
        this.impl$viewDistance = s.viewDistance().orElse(null);
        this.impl$enabled = s.enabled();
        this.impl$loadOnStartup = s.loadOnStartup();
        this.impl$performsSpawnLogic = s.performsSpawnLogic();
        this.impl$hardcore = s.hardcore();
        this.impl$commands = s.commands();
        this.impl$pvp = s.pvp();
    }

    @Override
    public SpongeWorldTemplate bridge$asTemplate() {
        return new SpongeWorldTemplate((Dimension) (Object) this);
    }

    @Redirect(
            method = "*",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
            )
    )
    private static Codec impl$useTemplateCodec(final Function function) {
        return SpongeWorldTemplate.DIRECT_CODEC;
    }
}
