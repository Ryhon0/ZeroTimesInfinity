package xyz.ryhon.zeroxinfinity.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(at = @At("HEAD"), method = "getProjectileType", cancellable = true)
	private void getProjectileType(ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
		if (stack.getItem() instanceof RangedWeaponItem r) {
			PlayerEntity p = (PlayerEntity) (Object) this;
			Optional<RegistryEntry.Reference<Enchantment>> infinity = p.getWorld().getRegistryManager()
					.get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.INFINITY);
			if (!infinity.isPresent())
				return;

				ItemStack arrowStack = Items.ARROW.getDefaultStack();
			if (EnchantmentHelper.getLevel(infinity.get(), stack) > 0 &&
			(r.getProjectiles().test(arrowStack) || r.getHeldProjectiles().test(arrowStack)))
				info.setReturnValue(arrowStack);
		}
	}
}