package com.llyycci.create_tweaked_controllers.item;

import com.llyycci.create_tweaked_controllers.Create_Tweaked_Controllers;
import com.llyycci.create_tweaked_controllers.tabs.CTC_CreativeTabs;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

public class ModItems {
	private static final CreateRegistrate REGISTRATE = Create_Tweaked_Controllers.registrate();

	static {
		REGISTRATE.setCreativeTab(CTC_CreativeTabs.BASE.key());
	}
	//参考内容：Create:LinkedControllerItem & CreateTweakedControllers:TweakedLinkedControllerItem
	public static final ItemEntry<TweakedLinkedControllerItem> TWEAKED_LINKED_CONTROLLER =
			REGISTRATE.item("tweaked_linked_controller", TweakedLinkedControllerItem::new)
					.properties(p -> p.stacksTo(1))//设置最大堆叠数
					.transform(CreateRegistrate.customRenderedItem(() -> TweakedLinkedControllerItemRenderer::new))//设置渲染器
					.model(AssetLookup.itemModelWithPartials())//设置模型
					.register();
	public static void register(){}
}
