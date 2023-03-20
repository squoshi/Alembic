package foundry.alembic.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import foundry.alembic.AlembicAPI;
import foundry.alembic.types.potion.AlembicFlammablePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin {

    @Shadow
    public abstract void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity);

    @WrapOperation(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean alembic$onEntityInside(Entity instance, DamageSource pSource, float pAmount, Operation<Boolean> original, BlockState pState) {
        if (pState.is(Blocks.SOUL_FIRE)) {
            ((AlembicFlammablePlayer) instance).setAlembicLastFireBlock("soul");
            return instance.hurt(AlembicAPI.SOUL_FIRE, pAmount);
        } else {
            return original.call(instance, pSource, pAmount);
        }
    }
}
