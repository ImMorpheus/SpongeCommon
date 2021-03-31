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
package org.spongepowered.common.scheduler;

public abstract class SyncScheduler extends SpongeScheduler {

    // The number of ticks elapsed since this scheduler began.
    private volatile long counter = 0L;

    SyncScheduler(final String tag) {
        super(tag);
    }

    /**
     * The hook to update the Ticks known by the SyncScheduler.
     */
    public void tick() {
        this.counter++;
        this.runTick();
    }

    @Override
    protected long timestamp(final boolean tickBased) {
        // The task is based on minecraft ticks, so we generate
        // a timestamp based on the elapsed ticks
        if (tickBased) {
            return this.counter * SpongeScheduler.TICK_DURATION_NS;
        }
        return super.timestamp(false);
    }

    @Override
    protected void executeTaskRunnable(final SpongeScheduledTask task, final Runnable runnable) {
        runnable.run();
    }
}
