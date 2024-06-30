package ru.smole.mifmoney.net;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import ru.smole.mifmoney.MIFMoney;
import ru.smole.mifmoney.net.message.client.*;
import ru.smole.mifmoney.net.message.server.S2CBuyRewardResponseMessage;
import ru.smole.mifmoney.net.message.server.S2CSyncShopMessage;

public interface MIFMoneyCommonNetwork {

    SimpleNetworkManager NET = SimpleNetworkManager.create(MIFMoney.MOD_ID);
    MessageType BUY_ITEM = NET.registerC2S("buy_item", C2SBuyItemMessage::new);
    MessageType BUY_REWARD = NET.registerC2S("buy_reward", C2SBuyRewardMessage::new);
    MessageType CREATE_CATEGORY_COMPONENT = NET.registerC2S("create_category_component", C2SCreateCategoryMessage::new);
    MessageType CREATE_ITEM_ORDER_COMPONENT = NET.registerC2S("create_item_order_component", C2SCreateItemOrderMessage::new);
    MessageType EDIT_CATEGORY_COMPONENT = NET.registerC2S("edit_category_component", C2SEditCategoryMessage::new);
    MessageType EDIT_ITEM_ORDER_COMPONENT = NET.registerC2S("edit_item_order_component", C2SEditItemOrderMessage::new);
    MessageType DELETE_CATEGORY_COMPONENT = NET.registerC2S("delete_category_component", C2SDeleteCategoryMessage::new);
    MessageType DELETE_ITEM_ORDER_COMPONENT = NET.registerC2S("delete_item_order_component", C2SDeleteItemOrderMessage::new);

    MessageType SYNC_SHOP = NET.registerS2C("sync_shop", S2CSyncShopMessage::new);
    MessageType BUY_REWARD_RESPONSE = NET.registerS2C("buy_reward_response", S2CBuyRewardResponseMessage::new);

    static void init() {}
}
