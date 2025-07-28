import api from "./Api";

export async function listMyAllCustomers() {
    const url = "/customers/my";
    const response = await api.get(url);

    return response.data;
}

export async function filterMyAllCustomers(name, type) {
    const params = new URLSearchParams();

    if(name) {
        params.append("name", name);
    }

    if(type) {
        params.append("type", type);
    }

    const url = `/customers/my?${params}`

    return api(url);
}

export async function listBudgetsByCustomer(customer) {
    return api(`/customers/my/${customer.id}/budgets`);
    
}

export async function createCustomer(customer) {
    return api.post("/customers", customer);
}

export async function updateCustomer(customer) {
    return api.put(`/customers/${customer.id}`, customer);
}

export async function findCustomerById(id) {
    return api(`/customers/${id}`);
}

export async function findMyCustomerQuantity() {
    return api("/customers/quantity");
}

export async function deleteCustomerById(id) {
    return api.delete(`/customers/${id}`);
}
