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
package org.spongepowered.common.registry.type.world;

import static com.google.common.base.Preconditions.checkArgument;

import net.minecraft.world.Teleporter;
import org.spongepowered.api.CatalogKey;
import org.spongepowered.api.registry.AlternateCatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;
import org.spongepowered.api.world.PortalAgent;
import org.spongepowered.api.world.PortalAgentType;
import org.spongepowered.api.world.PortalAgentTypes;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.SpongeImplHooks;
import org.spongepowered.common.interfaces.world.IMixinITeleporter;
import org.spongepowered.common.registry.AbstractCatalogRegistryModule;
import org.spongepowered.common.registry.SpongeAdditionalCatalogRegistryModule;
import org.spongepowered.common.world.SpongePortalAgentType;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RegisterCatalog(PortalAgentTypes.class)
public final class PortalAgentRegistryModule extends AbstractCatalogRegistryModule<PortalAgentType>
    implements SpongeAdditionalCatalogRegistryModule<PortalAgentType>, AlternateCatalogRegistryModule<PortalAgentType> {

    public static PortalAgentRegistryModule getInstance() {
        return Holder.INSTANCE;
    }

    private final Map<Class<? extends PortalAgent>, PortalAgentType> portalAgentClassToTypeMappings = new HashMap<>();

    @Override
    public void registerAdditionalCatalog(PortalAgentType portalAgentType) {
        checkArgument(this.map.get(portalAgentType.getKey().toString()) == null, "Cannot re-register a PortalAgent with the same id: " + portalAgentType.getKey().toString());
        this.map.put(portalAgentType.getKey(), portalAgentType);
        this.portalAgentClassToTypeMappings.put(portalAgentType.getPortalAgentClass(), portalAgentType);
    }

    @Override
    public boolean allowsApiRegistration() {
        return false;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void registerDefaults() {
        final Class clazz = Teleporter.class;
        register(new SpongePortalAgentType("minecraft:default", "Default", (Class<? extends IMixinITeleporter>) clazz));
    }

    PortalAgentRegistryModule() {
    }

    @SuppressWarnings("unchecked")
    public PortalAgentType validatePortalAgent(String portalAgentTypeClass, String worldName) {
        if (portalAgentTypeClass != null && !portalAgentTypeClass.isEmpty()) {
            try {
                Class<?> clazz = Class.forName(portalAgentTypeClass);
                if (Teleporter.class.isAssignableFrom(clazz)) {
                    return this.validatePortalAgent((Class<? extends IMixinITeleporter>) clazz);
                }
                SpongeImpl.getLogger().error("Class " + portalAgentTypeClass + " is not a valid PortalAgentType class for world " + worldName +". Falling back to default type...");
            } catch (ClassNotFoundException e) {
                SpongeImpl.getLogger().error("Could not locate PortalAgentType class " + portalAgentTypeClass + " for world " + worldName +". Falling back to default type...");
            }
        }

        return PortalAgentTypes.DEFAULT;
    }

    public PortalAgentType validatePortalAgent(IMixinITeleporter teleporter) {
        return this.validatePortalAgent(teleporter.getClass());
    }

    @SuppressWarnings("unchecked")
    private PortalAgentType validatePortalAgent(Class<? extends IMixinITeleporter> clazz) {
        PortalAgentType portalAgentType = this.portalAgentClassToTypeMappings.get(clazz);
        if (portalAgentType != null) {
            return portalAgentType;
        }

        String modId = SpongeImplHooks.getModIdFromClass(clazz);
        if (modId.isEmpty()) {
            return PortalAgentTypes.DEFAULT;
        }

        // used for mods only as plugins register in PreInit
        String teleporterName = clazz.getSimpleName().toLowerCase(Locale.ENGLISH);
        final CatalogKey key = CatalogKey.of(modId, teleporterName);
        if (this.map.get(key) == null) {
            portalAgentType = new SpongePortalAgentType(teleporterName, key.toString(), clazz);
            this.map.put(key, portalAgentType);
            this.portalAgentClassToTypeMappings.put((Class<? extends PortalAgent>) clazz, portalAgentType);
        }
        return this.map.get(key);

    }

    private static final class Holder {
        static final PortalAgentRegistryModule INSTANCE = new PortalAgentRegistryModule();
    }
}
