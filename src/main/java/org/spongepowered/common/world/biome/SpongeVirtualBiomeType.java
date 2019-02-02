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
package org.spongepowered.common.world.biome;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeGenerationSettings;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.VirtualBiomeType;
import org.spongepowered.common.SpongeCatalogType;

import java.util.function.Function;

public class SpongeVirtualBiomeType extends SpongeCatalogType implements VirtualBiomeType {

    private final double tempterature;
    private final double humidity;
    private final BiomeType persisted;
    private final Function<World, BiomeGenerationSettings> defaultSettings;

    public SpongeVirtualBiomeType(CatalogKey key, String name, double t, double h, BiomeType persist, Function<World, BiomeGenerationSettings> func) {
        super(key, name);
        this.tempterature = t;
        this.humidity = h;
        this.persisted = checkNotNull(persist);
        this.defaultSettings = func;
    }

    @Override
    public double getTemperature() {
        return this.tempterature;
    }

    @Override
    public double getHumidity() {
        return this.humidity;
    }

    @Override
    public BiomeType getPersistedType() {
        return this.persisted;
    }

    @Override
    public BiomeGenerationSettings createDefaultGenerationSettings(World world) {
        return this.defaultSettings.apply(world);
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("persistedBiome", this.persisted);
    }
}
