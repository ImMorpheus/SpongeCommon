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
package org.spongepowered.test.chunk;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.chunk.ChunkEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.jvm.Plugin;
import org.spongepowered.test.LoadableModule;

@Plugin("chunkeventtest")
public final class ChunkEventTest implements LoadableModule {

    private final PluginContainer plugin;

    @Inject
    public ChunkEventTest(final PluginContainer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enable(final CommandContext ctx) {
        Sponge.eventManager().registerListeners(this.plugin, new ChunkListener());
    }


    public static class ChunkListener {
        @Listener
        public void onChunkLoad(ChunkEvent.Load event) {
            Sponge.game().systemSubject().sendMessage(Component.text("Load Chunk " + event.targetChunk().chunkPosition() + " in " + event.chunkWorld().asString()));
        }

        @Listener
        public void onChunkSave(ChunkEvent.Save.Pre event) {
            event.setCancelled(true);
            Sponge.game().systemSubject().sendMessage(Component.text("Pre Save Chunk " + event.chunkPosition() + " in " + event.chunkWorld().asString()));
        }

        @Listener
        public void onChunkSave(ChunkEvent.Save.Post event) {
            Sponge.game().systemSubject().sendMessage(Component.text("Post Save Chunk " + event.chunkPosition() + " in " + event.chunkWorld().asString()));
        }

        @Listener
        public void onChunkUnload(ChunkEvent.Unload event) {
            Sponge.game().systemSubject().sendMessage(Component.text("Unload Chunk " + event.chunkPosition() + " in " + event.chunkWorld().asString()));
        }
    }
}
