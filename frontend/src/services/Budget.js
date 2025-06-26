import api from "./Api";

function createBudget(customerId, description, proposalNumber, bdi) {
    const request = {
        customerId,
        description,
        proposalNumber, 
        bdi
    }

    console.log(request)

    api.post("/budgets", request);
}

export default createBudget;