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
package org.spongepowered.common.registry.type.effect;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.effect.sound.record.RecordType;
import org.spongepowered.api.effect.sound.record.RecordTypes;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.api.registry.util.RegistrationDependency;
import org.spongepowered.common.effect.record.SpongeRecordType;
import org.spongepowered.common.registry.AbstractCatalogRegistryModule;
import org.spongepowered.common.text.translation.SpongeTranslation;

import java.util.Optional;

@RegisterCatalog(RecordTypes.class)
@RegistrationDependency(SoundRegistryModule.class)
public final class RecordTypeRegistryModule extends AbstractCatalogRegistryModule<RecordType> implements CatalogRegistryModule<RecordType> {

    public static RecordTypeRegistryModule getInstance() {
        return Holder.INSTANCE;
    }

    private final Int2ObjectMap<RecordType> byInternalId = new Int2ObjectOpenHashMap<>();

    RecordTypeRegistryModule() {
    }

    public Optional<RecordType> getByInternalId(int internalId) {
        return Optional.ofNullable(this.byInternalId.get(internalId));
    }

    private void add(SpongeRecordType recordType) {
        register(recordType);
        this.byInternalId.put(recordType.getInternalId(), recordType);
    }

    @Override
    public void registerDefaults() {
        this.add(new SpongeRecordType("minecraft:thirteen", new SpongeTranslation("item.record.13.desc"), 2256, SoundTypes.RECORD_13));
        this.add(new SpongeRecordType("minecraft:cat", new SpongeTranslation("item.record.cat.desc"), 2257, SoundTypes.RECORD_CAT));
        this.add(new SpongeRecordType("minecraft:blocks", new SpongeTranslation("item.record.blocks.desc"), 2258, SoundTypes.RECORD_BLOCKS));
        this.add(new SpongeRecordType("minecraft:chirp", new SpongeTranslation("item.record.chirp.desc"), 2259, SoundTypes.RECORD_CHIRP));
        this.add(new SpongeRecordType("minecraft:far", new SpongeTranslation("item.record.far.desc"), 2260, SoundTypes.RECORD_FAR));
        this.add(new SpongeRecordType("minecraft:mall", new SpongeTranslation("item.record.mall.desc"), 2261, SoundTypes.RECORD_MALL));
        this.add(new SpongeRecordType("minecraft:mellohi", new SpongeTranslation("item.record.mellohi.desc"), 2262, SoundTypes.RECORD_MELLOHI));
        this.add(new SpongeRecordType("minecraft:stal", new SpongeTranslation("item.record.stal.desc"), 2263, SoundTypes.RECORD_STAL));
        this.add(new SpongeRecordType("minecraft:strad", new SpongeTranslation("item.record.strad.desc"), 2264, SoundTypes.RECORD_STRAD));
        this.add(new SpongeRecordType("minecraft:ward", new SpongeTranslation("item.record.ward.desc"), 2265, SoundTypes.RECORD_WARD));
        this.add(new SpongeRecordType("minecraft:eleven", new SpongeTranslation("item.record.11.desc"), 2266, SoundTypes.RECORD_11));
        this.add(new SpongeRecordType("minecraft:wait", new SpongeTranslation("item.record.wait.desc"), 2267, SoundTypes.RECORD_WAIT));
    }

    private static final class Holder {
        final static RecordTypeRegistryModule INSTANCE = new RecordTypeRegistryModule();
    }
}
