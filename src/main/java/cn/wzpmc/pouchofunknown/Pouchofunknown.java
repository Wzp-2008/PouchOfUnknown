package cn.wzpmc.pouchofunknown;

import cn.wzpmc.pouchofunknown.Utils.ItemUtils;
import com.google.common.primitives.Ints;
import net.darkhax.itemstages.Restriction;
import net.darkhax.itemstages.RestrictionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pouchofunknown")
public class Pouchofunknown {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Pouchofunknown() {
        MinecraftForge.EVENT_BUS.addListener(this::OnPlayerPickupItem);
        MinecraftForge.EVENT_BUS.register(this);
        ItemLoader.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void OnPlayerPickupItem(EntityItemPickupEvent event){
        System.out.println("pickup");
        ItemStack item = event.getItem().getItem();
        PlayerEntity player = event.getPlayer();
        Restriction restriction = RestrictionManager.INSTANCE.getRestriction(player,item);
        LOGGER.info(restriction);
        if (restriction != null && restriction.shouldPreventPickup()){
            int i = ItemUtils.getPouch(player.inventory);
            if (i == -1){
                player.sendMessage(new TranslationTextComponent("message.pouchofunknown.none"), UUID.randomUUID());
            }else{
                ItemStack pouch = player.inventory.getStackInSlot(i);
                CompoundNBT ChildTag = pouch.getChildTag(String.valueOf(Item.getIdFromItem(item.getItem())));
                if(ChildTag != null){
                    int ItemAmount = ChildTag.getInt("amount");
                    ChildTag.putInt("amount",ItemAmount + item.getCount());
                }else{
                    ChildTag = pouch.getOrCreateChildTag(String.valueOf(Item.getIdFromItem(item.getItem())));
                    if (pouch.getTag() != null) {
                        int amount = pouch.getTag().getInt("amount");
                        pouch.getTag().putInt("amount",amount + 1);
                    }else{
                        pouch.setTag(new CompoundNBT());
                        pouch.getTag().putInt("amount",1);
                    }
                    ChildTag.putInt("amount",item.getCount());
                    int[] items = pouch.getTag().getIntArray("items");
                    ArrayList<Integer> items_ = new ArrayList<>();
                    if (items.length != 0) {
                        for (int item1 : items) {
                            items_.add(item1);
                        }
                    }
                    items_.add(Item.getIdFromItem(item.getItem()));
                    pouch.getTag().putIntArray("items",items_);
                }
                player.sendMessage(new TranslationTextComponent("message.pouchofunknown.pickup",item.getCount(),new TranslationTextComponent(item.getTranslationKey())),UUID.randomUUID());
                event.getItem().remove();
            }
        }

    }
}
