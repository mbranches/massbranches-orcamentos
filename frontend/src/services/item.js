import api from "./Api";

export async function searchItemsByName(name, personal = false) {
    const params = new URLSearchParams({ name });

    if(personal) {
        params.append("personal", "true");
    }

    return api(`/items?${params.toString()}`);
}

export async function createItem(name, unitMeasurement, unitPrice) {
  const request = {
    name,
    unitMeasurement,
    unitPrice,
  };

  return api.post("/items", request);
}