import api from "./Api";

export async function listMyAllCustomers() {
  const url = "/customers/my";
  const response = await api.get(url);

  return response.data;
}

export async function findCustomerById(id) {
    return api(`/customers/${id}`);
}

export async function findMyCustomerQuantity() {
    return api("/customers/quantity");
}
