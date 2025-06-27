import api from "./Api";

async function createBudget(customerId, description, proposalNumber, bdi) {
    const request = {
        customerId,
        description,
        proposalNumber, 
        bdi
    }

    return api.post("/budgets", request);

}

async function findBudgetById(id) {
    return api(`/budgets/${id}`);
}

export {createBudget, findBudgetById};