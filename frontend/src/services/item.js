import api from "./Api";

export async function searchItemsByName(name) {
    const params = new URLSearchParams({ name });

    return api(`/items/my?${params.toString()}`);
}

export async function createItem(name, unitMeasurement, unitPrice) {
  const request = {
    name,
    unitMeasurement,
    unitPrice,
  };

  return api.post("/items", request);
}