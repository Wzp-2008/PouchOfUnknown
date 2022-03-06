package cn.wzpmc.pouchofunknown;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemLoader {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,"pouchofunknown");
    public static final RegistryObject<Item> PouchOfUnknown = ITEMS.register("pouch",ItemPouchofunknown::new);
}
