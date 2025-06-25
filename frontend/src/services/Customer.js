import api from "./Api";

async function listAllCustomers() {
    try {
        const response = await api.get("customers");
        
        return response.data;
    } catch(error) {

    }
}

export default listAllCustomers;