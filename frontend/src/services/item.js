import api from "./Api";

export async function searchItemsByName(name) {
    const params = new URLSearchParams({ name });

    return api(`/items/my?${params.toString()}`);
}

export async function listMyAllItems() {
    const url = "/items/my";

    return api(url);
}

export async function createItem(name, unitMeasurement, unitPrice) {
  const request = {
    name,
    unitMeasurement,
    unitPrice,
  };

  return api.post("/items", request);
}

export async function updateItem(item) {
  return api.put(`/items/${item.id}`, item);
}

export async function deleteItemById(itemId) {
  return api.delete(`/items/${itemId}`);
}