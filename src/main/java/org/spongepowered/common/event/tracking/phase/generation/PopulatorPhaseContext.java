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
package org.spongepowered.common.event.tracking.phase.generation;

import org.spongepowered.api.world.gen.PopulatorType;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.common.event.tracking.IPhaseState;

public class PopulatorPhaseContext extends GenerationContext<PopulatorPhaseContext> {

    private PopulatorType type;

    PopulatorPhaseContext(
        IPhaseState<? extends PopulatorPhaseContext> state) {
        super(state);
    }

    public PopulatorPhaseContext populator(PopulatorType type) {
        this.type = type;
        return this;
    }

    public PopulatorType getPopulatorType() {
        return this.type;
    }

    @Override
    public PrettyPrinter printCustom(PrettyPrinter printer, int indent) {
        String s = String.format("%1$"+indent+"s", "");
        return super.printCustom(printer, indent)
            .add(s + "- %s: %s", "PopulatorType", this.type);
    }
}
