package com.mass_branches.utils;

import com.mass_branches.dto.request.ItemPostRequest;
import com.mass_branches.dto.response.ItemGetResponse;
import com.mass_branches.dto.response.ItemPostResponse;
import com.mass_branches.model.Item;
import com.mass_branches.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    public static List<Item> newItemList() {
        User user1 = UserUtils.newUserList().getFirst();
        Item item1 = Item.builder().id(1L).user(user1).name("Item 1").unitMeasurement("m2").unitPrice(BigDecimal.valueOf(120.25)).active(true).build();

        User user2 = UserUtils.newUserList().get(1);
        Item item2 = Item.builder().id(2L).user(user2).name("Item 2").unitMeasurement("cm").unitPrice(BigDecimal.valueOf(200.00)).active(false).build();

        User user3 = UserUtils.newUserList().getLast();
        Item item3 = Item.builder().id(3L).user(user3).name("Item 3").unitMeasurement("cm").unitPrice(BigDecimal.valueOf(300.00)).active(true).build();

        return new ArrayList<>(List.of(item1, item2, item3));
    }

//    public static List<ItemGetResponse> newItemGetResponseList() {
//        return newItemList().stream().map(item ->
//                new ItemGetResponse(item.getId(), item.getName(), item.getUnitMeasurement(), item.getUnitPrice())
//        ).toList();
//    }

    public static Item newItemToSave() {
        User user = UserUtils.newUserList().getFirst();

        return Item.builder()
                .name("Item 4")
                .unitMeasurement("un")
                .unitPrice(BigDecimal.valueOf(255.25))
                .active(true)
                .user(user)
                .build();
    }

    public static Item newItemSaved() {
        return newItemToSave().withId(4L);
    }

    public static ItemPostRequest newItemPostRequest() {
        Item item = newItemToSave();

        return new ItemPostRequest(
                item.getName(),
                item.getUnitMeasurement(),
                item.getUnitPrice()
        );
    }

    public static ItemPostResponse newItemPostResponse() {
        Item item = newItemSaved();

        return new ItemPostResponse(
                item.getId(),
                item.getName(),
                item.getUnitMeasurement(),
                item.getUnitPrice()
        );
    }
}
