package cn.wzpmc.pouchofunknown.Utils;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class ItemUtils {
    public static int getPouch(PlayerInventory playerInventory){
        int result = -1;
        for (ItemStack itemStack : playerInventory.mainInventory) {
            ResourceLocation registryName = itemStack.getItem().getRegistryName();
            ResourceLocation resourceLocation = new ResourceLocation("pouchofunknown","pouch");
            if(Objects.equals(registryName, resourceLocation)) {
                result = playerInventory.getSlotFor(itemStack);
            }
        }
        return result;
    }
    public static boolean isFull(PlayerInventory playerInventory){
        for (ItemStack stack : playerInventory.mainInventory) {
            if (stack.getItem().equals(Items.AIR)) {
                return true;
            }
        }
        return false;
    }
}
