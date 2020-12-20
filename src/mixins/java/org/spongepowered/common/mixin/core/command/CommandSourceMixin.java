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
package org.spongepowered.common.mixin.core.command;

import com.mojang.brigadier.ResultConsumer;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKey;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.world.ServerLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.accessor.command.CommandSourceAccessor;
import org.spongepowered.common.accessor.entity.EntityAccessor;
import org.spongepowered.common.bridge.command.CommandSourceBridge;
import org.spongepowered.common.bridge.command.ICommandSourceBridge;
import org.spongepowered.common.event.tracking.PhaseTracker;
import org.spongepowered.common.service.server.permission.SpongePermissions;
import org.spongepowered.common.util.VecHelper;

import java.util.function.Supplier;

@Mixin(CommandSource.class)
public abstract class CommandSourceMixin implements CommandSourceBridge {

    private static final String PROTECTED_CTOR = "(Lnet/minecraft/command/ICommandSource;Lnet/minecraft/util/math/vector/Vector3d;"
            + "Lnet/minecraft/util/math/vector/Vector2f;Lnet/minecraft/world/server/ServerWorld;ILjava/lang/String;Lnet/minecraft/util/text/ITextComponent;"
            + "Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/entity/Entity;ZLcom/mojang/brigadier/ResultConsumer;"
            + "Lnet/minecraft/command/arguments/EntityAnchorArgument$Type;)";
    private static final String PROTECTED_CTOR_METHOD = "<init>" + CommandSourceMixin.PROTECTED_CTOR + "V";

    @Shadow @Final private ICommandSource source;
    @Shadow @Final @Mutable private Vector3d worldPosition;
    @Shadow @Final @Mutable private Vector2f rotation;
    @Shadow @Final @Mutable private ServerWorld level;
    @Shadow @Final @Mutable private int permissionLevel;

    @Shadow @Final private ITextComponent displayName;
    @Shadow @Final private String textName;
    @Shadow @Final @Nullable private Entity entity;

    @Shadow @Final private MinecraftServer server;
    @Shadow @Final private boolean silent;
    @Shadow @Final private ResultConsumer<CommandSource> consumer;
    @Shadow @Final private EntityAnchorArgument.Type anchor;
    private Cause impl$cause;
    @Nullable private Supplier<String> impl$potentialPermissionNode = null;

    @Inject(method = CommandSourceMixin.PROTECTED_CTOR_METHOD, at = @At("RETURN"))
    private void impl$setCauseOnConstruction(
            final ICommandSource p_i49553_1_,
            final Vector3d p_i49553_2_,
            final Vector2f p_i49553_3_,
            final ServerWorld p_i49553_4_,
            final int p_i49553_5_,
            final String p_i49553_6_,
            final ITextComponent p_i49553_7_,
            final MinecraftServer p_i49553_8_,
            @Nullable final Entity p_i49553_9_,
            final boolean p_i49553_10_,
            final ResultConsumer<CommandSource> p_i49553_11_,
            final EntityAnchorArgument.Type p_i49553_12_,
            final CallbackInfo ci
    ) {
        this.impl$cause = PhaseTracker.getCauseStackManager().getCurrentCause();
        final EventContext context = this.impl$cause.getContext();

//        context.get(EventContextKeys.LOCATION).ifPresent(x ->{
//            this.worldPosition = VecHelper.toVanillaVector3d(x.getPosition());
//            this.level = (ServerWorld) x.getWorld();
//        });
//
//        context.get(EventContextKeys.ROTATION).ifPresent(x -> this.rotation = new Vector2f((float) x.getX(), (float) x.getY()));
//        context.get(EventContextKeys.SUBJECT).ifPresent(x -> {
//            if (x instanceof EntityAccessor) {
//                this.permissionLevel = ((EntityAccessor) x).invoker$getPermissionLevel();
//            } else if (x instanceof MinecraftServer && !((MinecraftServer) x).isSingleplayer()) {
//                this.permissionLevel = 4;
//            }
//        });
    }

    /*
     * All the with* methods copy this CommandSource, so we need to do our own copy.
     * This method MUST be above all other with* return injections so that the cause copy happens
     * FIRST. That way, we don't overwrite any changes we then need to make.
     */
    @Inject(method = {
            "withEntity",
            "withPosition",
            "withRotation(Lnet/minecraft/util/math/vector/Vector2f;)Lnet/minecraft/command/CommandSource;",
            "withCallback(Lcom/mojang/brigadier/ResultConsumer;)Lnet/minecraft/command/CommandSource;",
            "withSuppressedOutput",
            "withPermission",
            "withMaximumPermission",
            "withAnchor",
            "withLevel"
    }, at = @At("RETURN"))
    private void impl$copyPermissionOnCopy(final CallbackInfoReturnable<CommandSource> cir) {
        if (cir.getReturnValue() != (Object) this) {
            final CommandSourceBridge commandSourceBridge = ((CommandSourceBridge) cir.getReturnValue());
            commandSourceBridge.bridge$setPotentialPermissionNode(this.impl$potentialPermissionNode);
            commandSourceBridge.bridge$setCause(this.impl$cause);
        }
    }

    @Override
    public CommandCause bridge$withCurrentCause() {
        // Cause is set in ctor.
        return (CommandCause) CommandSourceAccessor.invoker$new(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel,
                this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor);
    }

    /*
     * A note on why we're doing this with the cause manually.
     *
     * When the object is first constructed, we get the cause from the stack manager. However, as the command processor
     * works through the nodes, this entire source may get replaced. We want to keep some of the changes in sync,
     * but the original cause may have gone by the time the source changes. Really, this command source is the analogue
     * of our Cause, NOT our CauseStackManager, so we just need to do `Cause.with(...)` along with their select `with*(...)`
     * methods.
     */

    @Inject(method = "withLevel", at = @At("RETURN"))
    private void impl$updateCauseOnWithWorld(final ServerWorld serverWorld, final CallbackInfoReturnable<CommandSource> cir) {
        if (cir.getReturnValue() != (Object) this) {
            final ServerLocation location = this.impl$cause.getContext().get(EventContextKeys.LOCATION)
                    .map(x -> ServerLocation.of((org.spongepowered.api.world.server.ServerWorld) serverWorld, x.getPosition()))
                    .orElseGet(() -> ServerLocation.of((org.spongepowered.api.world.server.ServerWorld) serverWorld,
                            VecHelper.toVector3d(cir.getReturnValue().getPosition())));
            ((CommandSourceBridge) cir.getReturnValue()).bridge$setCause(this.impl$applyToCause(EventContextKeys.LOCATION.get(), location));
        }
    }

    @Inject(method = "withPosition", at = @At("RETURN"))
    private void impl$updateCauseOnWithPosition(final Vector3d pos, final CallbackInfoReturnable<CommandSource> cir) {
        if (cir.getReturnValue() != (Object) this) {
            final org.spongepowered.math.vector.Vector3d position = VecHelper.toVector3d(pos);
            final ServerLocation location = this.impl$cause.getContext().get(EventContextKeys.LOCATION)
                    .map(x -> ServerLocation.of(x.getWorld(), position))
                    .orElseGet(() -> ServerLocation.of((org.spongepowered.api.world.server.ServerWorld) cir.getReturnValue().getLevel(), position));
            ((CommandSourceBridge) cir.getReturnValue()).bridge$setCause(this.impl$applyToCause(EventContextKeys.LOCATION.get(), location));
        }
    }

    @Inject(method = "withRotation(Lnet/minecraft/util/math/vector/Vector2f;)Lnet/minecraft/command/CommandSource;", at = @At("RETURN"))
    private void impl$updateCauseOnWithRotation(final Vector2f rotation, final CallbackInfoReturnable<CommandSource> cir) {
        if (cir.getReturnValue() != (Object) this) {
            final org.spongepowered.math.vector.Vector3d rot = new org.spongepowered.math.vector.Vector3d(rotation.x, rotation.y, 0); // no roll
            ((CommandSourceBridge) cir.getReturnValue()).bridge$setCause(this.impl$applyToCause(EventContextKeys.ROTATION.get(), rot));
        }
    }

    @Inject(method = "hasPermission", at = @At(value = "HEAD"), cancellable = true)
    private void impl$checkPermission(final int opLevel, final CallbackInfoReturnable<Boolean> cir) {
        if (this.impl$potentialPermissionNode != null) {
            final String perm = this.impl$potentialPermissionNode.get();
            // This will register the permission with the first op level we retrieve.
            SpongePermissions.registerPermission(perm, opLevel);
            cir.setReturnValue(((CommandCause) this).hasPermission(perm));
        }
        // fall through to the op level check if we haven't set a permission node.
    }

    @Override
    public void bridge$setPotentialPermissionNode(final @Nullable Supplier<String> permission) {
        this.impl$potentialPermissionNode = permission;
    }

    @Override
    public void bridge$setCause(final Cause cause) {
        this.impl$cause = cause;
    }

    @Override
    public Cause bridge$getCause() {
        return this.impl$cause;
    }

    @Override
    public ICommandSource bridge$getICommandSource() {
        return this.source;
    }

    @Override
    public void bridge$updateFrameFromICommandSource(final CauseStackManager.StackFrame frame) {
        ((ICommandSourceBridge) this.source).bridge$addToCauseStack(frame);
    }

    @Override
    public CommandCause bridge$asCommandCause() {
        return (CommandCause) this;
    }

    private <T> Cause impl$applyToCause(final EventContextKey<T> key, final T value) {
        final EventContext.Builder builder = EventContext.builder().from(this.impl$cause.getContext());
        builder.add(key, value);
        return Cause.builder().from(this.impl$cause).build(builder.build());
    }

}
