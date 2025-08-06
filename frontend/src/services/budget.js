import api from "./Api";

export async function createBudget(customerId, description, proposalNumber, status, bdi) {
    const request = {
        customerId,
        description,
        proposalNumber, 
        status,
        bdi
    };

    return api.post("/budgets", request);

}

export async function findBudgetById(id) {
    return api(`/budgets/${id}`);
}

export async function findAllMyBudgets() {
    const url = "/budgets/my";

    return api(url);
}

export async function filterMyBudgets(description, status) {
    const params = new URLSearchParams();

    if(description) {
        params.append("description", description);
    }

    if(status) {
        params.append("status", status);
    }

    const url = `/budgets/my?${params.toString()}`;

    return api(url);
}

export async function updateBudget(budgetToUpdate) {
    return api.put(`/budgets/${budgetToUpdate.id}`, budgetToUpdate); 
}

export async function deleteBudgetById(id) {
    return api.delete(`/budgets/${id}`);
}

export async function exportBudgetById(id) {
    const response = await api(`/budgets/${id}/export`, {
      responseType: "blob"
    });

    const blob = new Blob([response.data], {
      type: response.headers["content-type"],
    });

    const fileName = response.headers["content-disposition"].replace("attachment; filename=", "").replace(/"/g, "");

    const downloadUrl = window.URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = downloadUrl;
    link.setAttribute("download", fileName);

    document.body.appendChild(link);
    link.click();

    link.remove();
    window.URL.revokeObjectURL(downloadUrl);
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

export async function createBudgetItem(budgetId, stageId, order, itemId, unitMeasurement, unitPrice, quantity) {
    const request = {
        order,
        stageId,
        itemId: itemId,
        unitMeasurement,
        unitPrice, 
        quantity
    };

    return api.post(`/budgets/${budgetId}/items`, request);
}

export async function editBudgetItem(budgetId, request) {
    return api.put(`/budgets/${budgetId}/items/${request.id}`, request);
}

export async function deleteItemByBudgetId(budgetId, itemId) {
    return api.delete(`/budgets/${budgetId}/items/${itemId}`);
}

export async function deleteStageByBudgetId(budgetId, stageId) {
    return api.delete(`/budgets/${budgetId}/stages/${stageId}`);
}
