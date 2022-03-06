package cn.wzpmc.pouchofunknown;

import cn.wzpmc.pouchofunknown.Utils.ItemUtils;
import net.darkhax.itemstages.Restriction;
import net.darkhax.itemstages.RestrictionManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemPouchofunknown extends Item {
    public ItemPouchofunknown() {
        super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1));
    }
    public ActionResult<ItemStack> onItemRightClick(@Nullable World worldIn, PlayerEntity playerIn,Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(stack.getTag() == null){
            playerIn.sendMessage(new TranslationTextComponent("message.pouchofunknown.nocanget"), UUID.randomUUID());
        }else{
            int[] ids = stack.getTag().getIntArray("items");
            boolean full = false;
            int num = 0;
            for (int id : ids) {
                CompoundNBT childTag = stack.getChildTag(String.valueOf(id));
                assert childTag != null;
                int amount = childTag.getInt("amount");
                Item item = Item.getItemById(id);
                ItemStack stack1 = new ItemStack(item, amount);
                Restriction restriction = RestrictionManager.INSTANCE.getRestriction(playerIn,stack1);
                if(!(restriction != null && restriction.shouldPreventPickup())){
                    boolean b = ItemUtils.isFull(playerIn.inventory);
                    if(!b){
                        worldIn.addEntity(new ItemEntity(worldIn,playerIn.getPosX(),playerIn.getPosY(),playerIn.getPosZ(),stack1));
                        full = true;
                    }else{
                        playerIn.inventory.addItemStackToInventory(stack1);
                    }
                    stack.removeChildTag(String.valueOf(id));
                    stack.getTag().remove(String.valueOf(id));
                    int amount1 = stack.getTag().getInt("amount");
                    stack.getTag().putInt("amount",amount1 - 1);
                    int[] items = stack.getTag().getIntArray("items");
                    ArrayList<Integer> ints = new ArrayList<>();
                    for (int i : items) {
                        ints.add(i);
                    }
                    ints.remove(Integer.valueOf(id));
                    stack.getTag().putIntArray("items",ints);
                    num += 1;
                }
            }
            if(num != 0){
                playerIn.sendMessage(new TranslationTextComponent("message.pouchofunknown.get",num),UUID.randomUUID());
            }else{
                playerIn.sendMessage(new TranslationTextComponent("message.pouchofunknown.nocanget"), UUID.randomUUID());
            }
            if(full){
                playerIn.sendMessage(new TranslationTextComponent("message.pouchofunknown.full"),UUID.randomUUID());
            }
        }
        return ActionResult.resultPass(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
        CompoundNBT tag = stack.getTag();
        if(tag == null){
            tooltip.add(new TranslationTextComponent("tooltip.pouchofunknown.pouch",0));
        }else{
            tooltip.add(new TranslationTextComponent("tooltip.pouchofunknown.pouch",tag.getInt("amount")));
        }

    }
}
