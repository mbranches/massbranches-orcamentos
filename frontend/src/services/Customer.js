import api from "./Api";

async function listAllCustomers(personal = false) {
    try {
        const url = personal ? `/customers?personal=${personal}` : "/customers";
        const response = await api.get(url);
        
        return response.data;
    } catch(error) {
        throw error;
    }
}

export default listAllCustomers;