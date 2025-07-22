import api from "./Api";

export async function createBudget(customerId, description, proposalNumber, bdi) {
    const request = {
        customerId,
        description,
        proposalNumber, 
        bdi
    };

    return api.post("/budgets", request);

}

export async function findBudgetById(id) {
    return api(`/budgets/${id}`);
}

export async function findAllBudgets(personal = false) {
    let url = `/budgets`;

    const params = new URLSearchParams();
    if(personal) {
        params.append("personal", true);

        url += `?${params.toString()}`
    }

    return api(url);
}

export async function findAllBudgetsByDescription(description, personal = false) {
    let url = `/budgets`;

    const params = new URLSearchParams();
    params.append("description", description);
    if(personal) {
        params.append("personal", true);
    }

    url += `?${params.toString()}`;

    return api(url);
}

export async function updateBudget(budgetToUpdate) {
    return api.put(`/budgets/${budgetToUpdate.id}`, budgetToUpdate); 
}

export async function deleteBudgetById(id) {
    return api.delete(`/budgets/${id}`);
}

export async function findMyBudgetQuantity() {
    return api("/budgets/quantity");
}

export async function listElementsByBudgetId(id) {
    return api(`budgets/${id}/elements`);
}

export async function createStage(budgetId, order, name) {
    const request = {
      order,
      name
    };

    return api.post(`/budgets/${budgetId}/stages`, request);
}

export async function createBudgetItem(budgetId, stageId, order, itemId, unitPrice, quantity) {
    const request = {
        order,
        stageId,
        itemId: itemId,
        unitPrice, 
        quantity
    };

    return api.post(`/budgets/${budgetId}/items`, request);
}

export async function deleteItemByBudgetId(budgetId, itemId) {
    return api.delete(`/budgets/${budgetId}/items/${itemId}`);
}

export async function deleteStageByBudgetId(budgetId, stageId) {
    return api.delete(`/budgets/${budgetId}/stages/${stageId}`);
}
